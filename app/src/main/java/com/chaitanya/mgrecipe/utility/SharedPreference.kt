package com.chaitanya.mgrecipe.utility

import android.content.Context
import android.content.SharedPreferences
import com.chaitanya.mgrecipe.RecipeApplication

object SharedPreference {
    private const val PREF_NAME = "my_pref"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USERNAME = "username"

    private val sharedPreferences: SharedPreferences by lazy {
        RecipeApplication.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
    }
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USERNAME,null)
    }

    fun setUserName(username:String?) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply()
    }
}