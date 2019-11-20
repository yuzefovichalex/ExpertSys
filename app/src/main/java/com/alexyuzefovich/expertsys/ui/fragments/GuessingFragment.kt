package com.alexyuzefovich.expertsys.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexyuzefovich.expertsys.R
import com.alexyuzefovich.expertsys.core.ExpertSystem
import com.alexyuzefovich.expertsys.model.Object
import com.alexyuzefovich.expertsys.repository.ObjectsRepository
import kotlinx.android.synthetic.main.dialog_guessing_answer.view.*
import kotlinx.android.synthetic.main.fragment_guessing.*

class GuessingFragment : Fragment() {

    private lateinit var expertSystem: ExpertSystem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guessing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initClickListeners()
        initButtons()
    }

    private fun initClickListeners() {
        startButton.setOnClickListener {
            val context = this.context ?: return@setOnClickListener
            val objectList = ObjectsRepository.readObjects(context)
            if (objectList.isNotEmpty()) {
                initExpertSystem(objectList)
                start()
            } else {
                createNoKnowledgeDialog()?.show()
            }
        }
        yesButton.setOnClickListener {
            expertSystem.applyAnswer(ExpertSystem.ANSWER_YES)
        }
        noButton.setOnClickListener {
            expertSystem.applyAnswer(ExpertSystem.ANSWER_NO)
        }
    }

    private fun initExpertSystem(objectList: List<Object>) {
        expertSystem = ExpertSystem(
            objectList,
            object : ExpertSystem.OnResultListener {
                override fun onQuestionReady(characteristic: String) {
                    message.text = getString(R.string.question, characteristic)
                }

                override fun onAnswerReady(answer: String) {
                    createAnswerDialog(answer)?.show()
                }

                override fun onUnknownAnswer() {
                    createUnknownAnswerDialog()?.show()
                }
            }
        )
        expertSystem.startSearch()
    }

    @SuppressLint("InflateParams")
    private fun createAnswerDialog(answer: String): AlertDialog? {
        val context = this.context ?: return null
        val dialogLayout = LayoutInflater.from(context)
            .inflate(R.layout.dialog_guessing_answer, null)
        dialogLayout.answer.text = answer
        return AlertDialog.Builder(context)
            .setTitle(R.string.i_think_it_is)
            .setView(dialogLayout)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                expertSystem.resetObjects(ObjectsRepository.readObjects(context))
                resetViews()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                expertSystem.applyAnswer(ExpertSystem.ANSWER_NO)
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
    }

    @SuppressLint("InflateParams")
    private fun createUnknownAnswerDialog(): AlertDialog? {
        val context = this.context ?: return null
        val dialogLayout = LayoutInflater.from(context)
            .inflate(R.layout.dialog_guessing_answer, null)
        dialogLayout.answer.text = getString(R.string.unknown_object)
        return AlertDialog.Builder(context)
            .setTitle(R.string.sorry)
            .setView(dialogLayout)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                expertSystem.resetObjects(ObjectsRepository.readObjects(context))
                resetViews()
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
    }

    @SuppressLint("InflateParams")
    private fun createNoKnowledgeDialog(): AlertDialog? {
        val context = this.context ?: return null
        val dialogLayout = LayoutInflater.from(context)
            .inflate(R.layout.dialog_guessing_answer, null)
        dialogLayout.answer.text = getString(R.string.no_knowledge)
        return AlertDialog.Builder(context)
            .setTitle(R.string.sorry)
            .setView(dialogLayout)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
    }

    private fun initButtons() {
        yesButton.visibility = View.GONE
        noButton.visibility = View.GONE
    }

    private fun start() {
        startButton.visibility = View.INVISIBLE
        yesButton.visibility = View.VISIBLE
        noButton.visibility = View.VISIBLE
    }

    private fun resetViews() {
        message.text = getString(R.string.guessing_welcome_message)
        startButton.visibility = View.VISIBLE
        yesButton.visibility = View.GONE
        noButton.visibility = View.GONE
    }

}