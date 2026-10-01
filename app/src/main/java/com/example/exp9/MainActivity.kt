package com.example.exp9

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var draftManager: DraftManager
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)

        sessionManager = SessionManager(this)
        draftManager = DraftManager(this)
        dbHelper = DatabaseHelper(this)

        val targetIntent = when {
            // 1. Active login session -> Dashboard directly
            sessionManager.isLoggedIn() -> {
                Intent(this, DashboardActivity::class.java)
            }
            // 2. Unfinished registration draft -> Registration screen with draft restored
            draftManager.hasDraft() -> {
                Intent(this, RegisterActivity::class.java)
            }
            // 3. Registered accounts exist (after registration or logout) -> Login screen
            dbHelper.hasRegisteredUsers() -> {
                Intent(this, LoginActivity::class.java)
            }
            // 4. Fresh installation / first launch with no draft & no accounts -> Registration screen
            else -> {
                Intent(this, RegisterActivity::class.java)
            }
        }.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(targetIntent)
        finish()
    }
}
