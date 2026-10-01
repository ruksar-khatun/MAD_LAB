package com.example.exp9

import android.content.Context
import android.content.SharedPreferences

class DraftManager(context: Context) {

    companion object {
        private const val PREF_NAME = "SignupDraftPrefs"
        private const val KEY_FULL_NAME = "draft_full_name"
        private const val KEY_EMAIL = "draft_email"
        private const val KEY_USERNAME = "draft_username"
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveDraft(fullName: String, email: String, username: String) {
        val editor = pref.edit()
        val fName = fullName.trim()
        val em = email.trim()
        val uName = username.trim()

        if (fName.isEmpty() && em.isEmpty() && uName.isEmpty()) {
            editor.clear()
        } else {
            editor.putString(KEY_FULL_NAME, fName)
            editor.putString(KEY_EMAIL, em)
            editor.putString(KEY_USERNAME, uName)
        }
        editor.apply()
    }

    fun hasDraft(): Boolean {
        val fullName = pref.getString(KEY_FULL_NAME, null)
        val email = pref.getString(KEY_EMAIL, null)
        val username = pref.getString(KEY_USERNAME, null)
        return !fullName.isNullOrBlank() || !email.isNullOrBlank() || !username.isNullOrBlank()
    }

    fun getDraftFullName(): String {
        return pref.getString(KEY_FULL_NAME, "") ?: ""
    }

    fun getDraftEmail(): String {
        return pref.getString(KEY_EMAIL, "") ?: ""
    }

    fun getDraftUsername(): String {
        return pref.getString(KEY_USERNAME, "") ?: ""
    }

    fun clearDraft() {
        pref.edit().clear().apply()
    }
}
