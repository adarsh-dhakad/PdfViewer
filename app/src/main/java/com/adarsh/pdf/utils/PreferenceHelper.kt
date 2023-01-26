package com.adarsh.pdf.utils

import android.content.SharedPreferences

class PreferenceHelper {
    companion  object {
    private lateinit var preferences: SharedPreferences
    fun getSkipUpdate(key: String): String {
        return preferences.getString(key, "0")!!
    }

}
}