package com.example.exp5

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "exp5_notification_channel"
    private val NOTIFICATION_ID = 501

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            sendNotificationAndProceed()
        } else {
            Toast.makeText(this, "Permission denied to show notifications", Toast.LENGTH_SHORT).show()
            proceedToDepartmentActivity()
        }
    }

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

        createNotificationChannel()

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // Autologin with credentials
        usernameInput.setText("ruksar26")
        passwordInput.setText("Ru@123")

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
                    checkPermissionAndNotify()
                }
                else -> {
                    Toast.makeText(this, R.string.error_invalid_credentials, Toast.LENGTH_SHORT).show()
                    usernameInput.error = getString(R.string.error_invalid_credentials)
                    passwordInput.error = getString(R.string.error_invalid_credentials)
                }
            }
        }

        // Automatically trigger login on start
        loginButton.performClick()
    }

    private fun checkPermissionAndNotify() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                sendNotificationAndProceed()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            sendNotificationAndProceed()
        }
    }

    private fun sendNotificationAndProceed() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())

        proceedToDepartmentActivity()
    }

    private fun proceedToDepartmentActivity() {
        val intent = Intent(this, DepartmentActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Exp5 Notification Channel"
            val descriptionText = "Channel for Exp5 login notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}