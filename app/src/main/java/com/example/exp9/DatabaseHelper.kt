package com.example.exp9

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_FULL_NAME = "full_name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD_HASH = "password_hash"
        const val COLUMN_CREATED_AT = "created_at"
    }

    sealed class RegisterResult {
        data class Success(val userId: Long) : RegisterResult()
        object EmailExists : RegisterResult()
        object UsernameExists : RegisterResult()
        object Failed : RegisterResult()
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FULL_NAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_USERNAME TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD_HASH TEXT NOT NULL,
                $COLUMN_CREATED_AT TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun hasRegisteredUsers(): Boolean {
        val db = this.readableDatabase
        val query = "SELECT 1 FROM $TABLE_USERS LIMIT 1"
        val cursor = db.rawQuery(query, null)
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun checkEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT 1 FROM $TABLE_USERS WHERE LOWER($COLUMN_EMAIL) = LOWER(?)"
        val cursor = db.rawQuery(query, arrayOf(email.trim()))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun checkUsernameExists(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT 1 FROM $TABLE_USERS WHERE LOWER($COLUMN_USERNAME) = LOWER(?)"
        val cursor = db.rawQuery(query, arrayOf(username.trim()))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun registerUser(user: User, rawPassword: String): RegisterResult {
        if (checkEmailExists(user.email)) {
            return RegisterResult.EmailExists
        }
        if (checkUsernameExists(user.username)) {
            return RegisterResult.UsernameExists
        }

        val db = this.writableDatabase
        val passwordHash = PasswordUtils.hashPassword(rawPassword)
        val createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val values = ContentValues().apply {
            put(COLUMN_FULL_NAME, user.fullName.trim())
            put(COLUMN_EMAIL, user.email.trim())
            put(COLUMN_USERNAME, user.username.trim())
            put(COLUMN_PASSWORD_HASH, passwordHash)
            put(COLUMN_CREATED_AT, createdAt)
        }

        val result = db.insert(TABLE_USERS, null, values)
        return if (result != -1L) {
            RegisterResult.Success(result)
        } else {
            RegisterResult.Failed
        }
    }

    fun authenticateUser(usernameOrEmail: String, rawPassword: String): User? {
        val db = this.readableDatabase
        val query = """
            SELECT * FROM $TABLE_USERS 
            WHERE LOWER($COLUMN_USERNAME) = LOWER(?) OR LOWER($COLUMN_EMAIL) = LOWER(?)
        """.trimIndent()

        val input = usernameOrEmail.trim()
        val cursor = db.rawQuery(query, arrayOf(input, input))

        var user: User? = null
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID)
            val nameIndex = cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)
            val emailIndex = cursor.getColumnIndexOrThrow(COLUMN_EMAIL)
            val usernameIndex = cursor.getColumnIndexOrThrow(COLUMN_USERNAME)
            val hashIndex = cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_HASH)
            val createdIndex = cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)

            val storedHash = cursor.getString(hashIndex)
            if (PasswordUtils.verifyPassword(rawPassword, storedHash)) {
                user = User(
                    id = cursor.getInt(idIndex),
                    fullName = cursor.getString(nameIndex),
                    email = cursor.getString(emailIndex),
                    username = cursor.getString(usernameIndex),
                    passwordHash = "", // Don't expose hash
                    createdAt = cursor.getString(createdIndex)
                )
            }
        }
        cursor.close()
        return user
    }

    fun getUserById(userId: Int): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        var user: User? = null
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID)
            val nameIndex = cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)
            val emailIndex = cursor.getColumnIndexOrThrow(COLUMN_EMAIL)
            val usernameIndex = cursor.getColumnIndexOrThrow(COLUMN_USERNAME)
            val createdIndex = cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)

            user = User(
                id = cursor.getInt(idIndex),
                fullName = cursor.getString(nameIndex),
                email = cursor.getString(emailIndex),
                username = cursor.getString(usernameIndex),
                passwordHash = "",
                createdAt = cursor.getString(createdIndex)
            )
        }
        cursor.close()
        return user
    }
}
