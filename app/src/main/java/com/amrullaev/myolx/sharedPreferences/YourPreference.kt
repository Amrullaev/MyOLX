package com.amrullaev.myolx.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

class YourPreference private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("userData", Context.MODE_PRIVATE)

    fun saveData(key: String?, value: String?) {
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
        prefsEditor.commit()
    }

    fun setTheme(value: Boolean) {
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.putBoolean("theme", value)
        prefsEditor.apply()
        prefsEditor.commit()
    }

    fun getData(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getTheme(): Boolean {
        return sharedPreferences.getBoolean("theme", false)
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.remove("phone")
        editor.remove("username")
        editor.apply()
        editor.commit()
    }

    companion object {
        private var yourPreference: YourPreference? = null
        fun getInstance(context: Context): YourPreference {
            if (yourPreference == null) {
                yourPreference = YourPreference(context)
            }
            return yourPreference as YourPreference
        }
    }

}