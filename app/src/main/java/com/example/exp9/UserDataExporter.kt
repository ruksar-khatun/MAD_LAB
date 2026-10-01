package com.example.exp9

import android.content.Context

object UserDataExporter {

    /**
     * Generates the XML content representing SharedPreferences session storage (`UserPrefs.xml`)
     */
    fun generateSessionPrefsXml(isLoggedIn: Boolean, userId: Int, username: String?): String {
        val uname = username ?: ""
        return """
            <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
            <map>
                <boolean name="isLoggedIn" value="$isLoggedIn" />
                <int name="userId" value="$userId" />
                <string name="username">$uname</string>
            </map>
        """.trimIndent()
    }

    /**
     * Generates the XML content representing SharedPreferences draft storage (`SignupDraftPrefs.xml`)
     */
    fun generateDraftPrefsXml(fullName: String, email: String, username: String): String {
        return """
            <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
            <map>
                <string name="draft_full_name">$fullName</string>
                <string name="draft_email">$email</string>
                <string name="draft_username">$username</string>
            </map>
        """.trimIndent()
    }

    /**
     * Generates the SQL schema and data dump for SQLite `UserDatabase.db`
     */
    fun generateUserDatabaseDump(context: Context): String {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_USERS}", null)

        val sb = StringBuilder()
        sb.append("-- SQLite UserDatabase.db Export --\n")
        sb.append("CREATE TABLE ${DatabaseHelper.TABLE_USERS} (\n")
        sb.append("    id INTEGER PRIMARY KEY AUTOINCREMENT,\n")
        sb.append("    full_name TEXT NOT NULL,\n")
        sb.append("    email TEXT UNIQUE NOT NULL,\n")
        sb.append("    username TEXT UNIQUE NOT NULL,\n")
        sb.append("    password_hash TEXT NOT NULL,\n")
        sb.append("    created_at TEXT NOT NULL\n")
        sb.append(");\n\n")

        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)
            val nameIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)
            val emailIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL)
            val unameIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME)
            val hashIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD_HASH)
            val createdIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_AT)

            do {
                val id = cursor.getInt(idIdx)
                val name = cursor.getString(nameIdx)
                val email = cursor.getString(emailIdx)
                val uname = cursor.getString(unameIdx)
                val hash = cursor.getString(hashIdx)
                val created = cursor.getString(createdIdx)

                sb.append("INSERT INTO ${DatabaseHelper.TABLE_USERS} VALUES ($id, '$name', '$email', '$uname', '$hash', '$created');\n")
            } while (cursor.moveToNext())
        } else {
            sb.append("-- No registered users found in database yet.\n")
        }
        cursor.close()

        return sb.toString()
    }
}
