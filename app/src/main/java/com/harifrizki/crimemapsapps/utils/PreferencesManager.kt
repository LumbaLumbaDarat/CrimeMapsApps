package com.harifrizki.crimemapsapps.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.orhanobut.logger.Logger

class PreferencesManager {
    companion object {
        private val TAG: String =
            PreferencesManager::javaClass.name

        private lateinit var preferencesManager: PreferencesManager
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor

        fun getInstance(context: Context): PreferencesManager {
            preferencesManager = PreferencesManager()
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()
            return preferencesManager
        }
    }

    fun setPreferences(key: String, value: String?) {
        editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setPreferences(key: String, boolean: Boolean) {
        editor = sharedPreferences.edit()
        editor.putBoolean(key, boolean)
        editor.apply()
    }

    fun <T> setPreferences(key: String, classModel: T) {
        editor = sharedPreferences.edit()
        editor.putString(key, Gson().toJson(classModel))
        editor.apply()
    }

    fun getPreferences(key: String): String? {
        return try {
            sharedPreferences.getString(key, EMPTY_STRING)
        } catch (e: Exception) {
            Logger.e(TAG, e.printStackTrace().toString())
            EMPTY_STRING
        }
    }

    fun getPreferences(key: String, boolean: Boolean): Boolean {
        return try {
            sharedPreferences.getBoolean(key, boolean)
        } catch (e: Exception) {
            Logger.e(TAG, e.printStackTrace().toString())
            boolean
        }
    }

    fun <T> getPreferences(key: String, clazz: Class<T>): T {
        return Gson().fromJson<T>(
            sharedPreferences.getString(
                key,
                EMPTY_STRING
            ), clazz
        )
    }

    fun removePreferences(key: String) {
        editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}