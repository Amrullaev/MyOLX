package com.amrullaev.myolx.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


object SharedPref {
    fun changLanguage(context: Context, lang: String) {
        val sharedPreferences = context.getSharedPreferences("Diamond", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("lang", lang).apply()
    }

    fun getLanguage(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("Diamond", MODE_PRIVATE)
        return sharedPreferences.getString("lang", "")
    }

    /***************************THEME**************************************************************************/
    fun changTheme(context: Context, theme: String) {
        val sharedPreferences = context.getSharedPreferences("Diamond", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("theme", theme).apply()
    }

    fun getTheme(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("Diamond", MODE_PRIVATE)
        return sharedPreferences.getString("theme", "")
    }

}