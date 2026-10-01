package com.example.exp9

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.exp9.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            redirectToLogin()
            return
        }

        loadUserData()

        binding.btnLogout.setOnClickListener {
            handleLogout()
        }

        binding.tvViewStoredData.setOnClickListener {
            showStoredDataDialog()
        }
    }

    private fun loadUserData() {
        val userId = sessionManager.getUserId()
        if (userId == -1) {
            redirectToLogin()
            return
        }

        val user = dbHelper.getUserById(userId)
        if (user != null) {
            binding.tvWelcomeHeader.text = getString(R.string.welcome_user, user.fullName)
            binding.tvFullNameValue.text = user.fullName
            binding.tvEmailValue.text = user.email
            binding.tvUsernameValue.text = user.username
        } else {
            Toast.makeText(this, "User profile not found in database.", Toast.LENGTH_SHORT).show()
            redirectToLogin()
        }
    }

    private fun showStoredDataDialog() {
        val sessionXml = UserDataExporter.generateSessionPrefsXml(
            sessionManager.isLoggedIn(),
            sessionManager.getUserId(),
            sessionManager.getUsername()
        )
        val dbDump = UserDataExporter.generateUserDatabaseDump(this)

        val message = """
            --- Active Session (UserPrefs.xml) ---
            $sessionXml
            
            --- Database Dump (UserDatabase.db) ---
            $dbDump
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Stored Persistence Files Data")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun handleLogout() {
        // Clear session from SharedPreferences
        sessionManager.logoutUser()

        Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show()

        redirectToLogin()
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
