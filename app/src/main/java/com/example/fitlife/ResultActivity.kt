package com.example.fitlife

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class ResultActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ResultActivityLifecycle"
        private const val CHANNEL_ID = "fitlife_activity_channel"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called")
        setContentView(R.layout.activity_result)

        val tvResultName = findViewById<TextView>(R.id.tvResultName)
        val tvResultSteps = findViewById<TextView>(R.id.tvResultSteps)
        val tvResultActivity = findViewById<TextView>(R.id.tvResultActivity)
        val tvResultGoal = findViewById<TextView>(R.id.tvResultGoal)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // Retrieve extras from Intent
        val name = intent.getStringExtra(MainActivity.EXTRA_NAME) ?: "N/A"
        val steps = intent.getStringExtra(MainActivity.EXTRA_STEPS) ?: "0"
        val activityType = intent.getStringExtra(MainActivity.EXTRA_ACTIVITY) ?: "N/A"
        val goal = intent.getStringExtra(MainActivity.EXTRA_GOAL) ?: "OFF"
        val notify = intent.getBooleanExtra(MainActivity.EXTRA_NOTIFY, false)

        Log.d(TAG, "Received Extras - Name: $name, Steps: $steps, Activity: $activityType, Goal: $goal, Notify: $notify")

        // Display in UI
        tvResultName.text = name
        tvResultSteps.text = steps
        tvResultActivity.text = activityType
        tvResultGoal.text = goal

        btnBack.setOnClickListener {
            finish()
        }

        // Trigger notification if requested
        if (notify) {
            sendActivityNotification(name, activityType)
        }
    }

    private fun sendActivityNotification(name: String, activityType: String) {
        Log.d(TAG, "Generating activity notification for $name ($activityType)")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Notification Channel for Android 8.0+ (API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameChannel = "Activity Notifications"
            val descriptionText = "Notifications for recorded fitness activities"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, nameChannel, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_fitness_logo)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText("Hello $name, your $activityType activity has been successfully recorded!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}
