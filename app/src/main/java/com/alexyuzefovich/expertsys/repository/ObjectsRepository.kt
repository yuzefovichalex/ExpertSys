package com.alexyuzefovich.expertsys.repository

import android.content.Context
import com.alexyuzefovich.expertsys.model.Object
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

object ObjectsRepository {

    private const val FILE_NAME = "objects.json"

    fun readObjects(context: Context): ArrayList<Object> {
        var streamReader: InputStreamReader? = null
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = context.openFileInput(FILE_NAME)
            streamReader = InputStreamReader(fileInputStream)
            val gson = Gson()
            val listType = TypeToken.getParameterized(List::class.java, Object::class.java).type
            return gson.fromJson<ArrayList<Object>>(streamReader, listType)
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            try {
                streamReader?.close()
                fileInputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ArrayList()
    }

    fun writeObjects(context: Context, objects: List<Object>) {
        val gson = Gson()
        val jsonString = gson.toJson(objects)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            fileOutputStream.write(jsonString.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}