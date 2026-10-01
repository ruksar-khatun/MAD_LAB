package com.example.exp9

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.exp9.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        // Pre-fill username if passed from registration
        intent.getStringExtra("EXTRA_USERNAME_OR_EMAIL")?.let { prefill ->
            binding.etUsernameOrEmail.setText(prefill)
            binding.etPassword.requestFocus()
        }

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        binding.tvSignUpNav.setOnClickListener {
            navigateToRegister()
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(
                this,
                "Please enter your registered credentials or sign up for a new account.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handleLogin() {
        clearErrors()

        val input = binding.etUsernameOrEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        var isValid = true

        if (input.isEmpty()) {
            binding.tilUsernameOrEmail.error = "Email or Username is required"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            isValid = false
        }

        if (!isValid) return

        val user = dbHelper.authenticateUser(input, password)
        if (user != null) {
            // Store session in SharedPreferences
            sessionManager.createLoginSession(user.id, user.username)

            Toast.makeText(
                this,
                "Login successful! Welcome back, ${user.fullName}.",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, DashboardActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        } else {
            binding.tilPassword.error = "Invalid username/email or password"
            Toast.makeText(
                this,
                "Invalid credentials. Please check your details and try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun clearErrors() {
        binding.tilUsernameOrEmail.error = null
        binding.tilPassword.error = null
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }
}
