package com.alexyuzefovich.expertsys.core

import com.alexyuzefovich.expertsys.model.Object
import kotlin.collections.ArrayList

class ExpertSystem(
    objectList: List<Object>,
    var onResultListener: OnResultListener? = null
) {

    interface OnResultListener {
        fun onQuestionReady(characteristic: String)
        fun onAnswerReady(answer: String)
        fun onUnknownAnswer()
    }

    companion object {
        const val ANSWER_YES = 1 // when object has characteristic
        const val ANSWER_NO = 0 // when object doesn't have characteristic
    }

    private lateinit var objects: ArrayList<String>
    private lateinit var characteristics: ArrayList<String>
    private lateinit var matrix: ArrayList<IntArray>

    private var currentMinWeightRowIndex = 0

    private var answerFound = false

    init {
        initValues(objectList)
    }

    private fun initValues(objectList: List<Object>) {
        check(objectList.isNotEmpty())
        objects = ArrayList()
        characteristics = ArrayList()
        objectList.forEach {
            objects.add(it.name)
            characteristics.addAll(it.characteristics)
        }
        characteristics = characteristics.distinct() as ArrayList

        matrix = ArrayList()
        characteristics.forEach { characteristic ->
            val row = IntArray(objects.size) { 0 }
            objectList.forEachIndexed { index, obj ->
                row[index] = if (obj.characteristics.contains(characteristic)) {
                    1
                } else {
                    0
                }
            }
            matrix.add(row)
        }
    }

    fun startSearch() {
        answerFound = false
        val removingCharacteristicsIndices = matrix.findIndicesOf { row -> row.all { it == 0 } }
        characteristics.removeIndices(removingCharacteristicsIndices)
        matrix.removeIndices(removingCharacteristicsIndices)

        currentMinWeightRowIndex = matrix.indexOf(matrix.minBy { it.sum() })

        onResultListener?.onQuestionReady(characteristics[currentMinWeightRowIndex])
    }

    fun applyAnswer(answer: Int) {
        require(answer == ANSWER_NO || answer == ANSWER_YES)
        val type = when (answer) {
            ANSWER_NO -> ANSWER_YES
            ANSWER_YES -> ANSWER_NO
            else -> ANSWER_YES
        }
        if (!answerFound) {
            val isFiltered = filterMatrix(type)
            if (objects.size > 1) {
                if (isFiltered) {
                    startSearch()
                } else {
                    finishSearch()
                }
            } else {
                finishSearch()
            }
        } else {
            if (matrix.isNotEmpty() && objects.isNotEmpty()) {
                matrix.removeAt(0)
                objects.removeAt(0)
            }
            finishSearch()
        }
    }

    private fun filterMatrix(valueType: Int): Boolean{
        val filteringMatrix = matrix.map { it.toMutableList() }
        val characteristicRow = matrix[currentMinWeightRowIndex].toList()
        val removingObjectsIndices = characteristicRow.findIndicesOf { value -> value == valueType }
        filteringMatrix.forEach { row ->
            (row as ArrayList).removeIndices(removingObjectsIndices)
        }
        objects.removeIndices(removingObjectsIndices)
        matrix = filteringMatrix.map { it.toIntArray() } as ArrayList
        return removingObjectsIndices.isNotEmpty()
    }

    private fun finishSearch() {
        if (objects.isEmpty()) {
            onResultListener?.onUnknownAnswer()
        } else {
            onResultListener?.onAnswerReady(objects[0])
        }
        answerFound = true
    }

    fun resetObjects(objectList: List<Object>) {
        initValues(objectList)
    }

    private inline fun <T> Iterable<T>.findIndicesOf(predicate: (T) -> Boolean): IntArray {
        val indices = ArrayList<Int>()
        forEachIndexed { index, t ->
            if (predicate(t)) {
                indices.add(index)
            }
        }
        return indices.toIntArray()
    }

    private fun <T> ArrayList<T>.removeIndices(indices: IntArray) {
        indices.sortDescending()
        indices.forEach { idx ->
            removeAt(idx)
        }
    }

}