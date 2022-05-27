package com.qnape.instaking.util

import android.content.Context
import androidx.preference.PreferenceManager

class Preferences {
    companion object{
        fun saveStringData(context: Context?, key: String?, value: String?) {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val editor = sharedPreferences?.edit()
            editor?.putString(key, value)
            editor?.apply()
        }

        fun getSavedString(context: Context?, key: String?, defalut: String?): String? {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            return sharedPreferences?.getString(key, defalut)
        }

        fun saveStringSetData(context: Context?, key: String?, value: Set<String?>?) {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val editor = sharedPreferences?.edit()
            editor?.putStringSet(key, value)
            editor?.apply()
        }

        fun getSavedSetString(context: Context?, key: String?): Set<String?>? {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            return sharedPreferences?.getStringSet(key, null)
        }

        fun saveBooleanData(context: Context?, key: String?, value: Boolean) {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val editor = sharedPreferences?.edit()
            editor?.putBoolean(key, value)
            editor?.apply()
        }

        fun removeKey(context: Context?, key: String?) {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val editor = sharedPreferences?.edit()
            editor?.remove(key)
            editor?.apply()
        }

        fun removeKeyList(context: Context?, keyList: ArrayList<String?>) {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val editor = sharedPreferences?.edit()
            for (i in keyList.indices) {
                editor?.remove(keyList[i])
            }
            editor?.apply()
        }

        fun getSavedBoolean(context: Context?, key: String?, defalut: Boolean): Boolean? {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            return sharedPreferences?.getBoolean(key, defalut)
        }

        fun saveIntData(context: Context?, key: String?, value: Int) {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val editor = sharedPreferences?.edit()
            editor?.putInt(key, value)
            editor?.apply()
        }

        fun getIntString(context: Context?, key: String?, defalut: Int): Int? {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            return sharedPreferences?.getInt(key, defalut)
        }

        fun saveLongData(context: Context?, key: String?, value: Long) {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val editor = sharedPreferences?.edit()
            editor?.putLong(key, value)
            editor?.apply()
        }

        fun getLongString(context: Context?, key: String?, defalut: Long): Long? {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            return sharedPreferences?.getLong(key, defalut)
        }

        fun hasKey(context: Context?, key: String?): Boolean? {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            return sharedPreferences?.contains(key)
        }

        fun clearPreference(context: Context?) {
            val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            sharedPreferences?.edit()?.clear()?.apply()
        }
    }
    enum class PreferencesKey{
        UserID
    }
}