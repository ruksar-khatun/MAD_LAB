package com.example.exp4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        val rootView = findViewById<android.view.View>(R.id.main)
        rootView?.let { v ->
            ViewCompat.setOnApplyWindowInsetsListener(v) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            when {
                username.isEmpty() -> {
                    usernameInput.error = getString(R.string.error_empty_username)
                }
                password.isEmpty() -> {
                    passwordInput.error = getString(R.string.error_empty_password)
                }
                username == "ruksar26" && password == "Ru@123" -> {
                    Toast.makeText(this, R.string.login_successful, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DepartmentActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    Toast.makeText(this, R.string.error_invalid_credentials, Toast.LENGTH_SHORT).show()
                    usernameInput.error = getString(R.string.error_invalid_credentials)
                    passwordInput.error = getString(R.string.error_invalid_credentials)
                }
            }
        }
    }
}