package com.alexyuzefovich.expertsys.util

import android.content.Context
import android.widget.Toast

object WidgetUtil {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}