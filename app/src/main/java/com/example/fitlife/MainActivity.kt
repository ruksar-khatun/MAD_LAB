package com.example.fitlife

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ToggleButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivityLifecycle"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_STEPS = "extra_steps"
        const val EXTRA_ACTIVITY = "extra_activity"
        const val EXTRA_GOAL = "extra_goal"
        const val EXTRA_NOTIFY = "extra_notify"
    }

    private lateinit var etName: EditText
    private lateinit var etSteps: EditText
    private lateinit var rgActivityType: RadioGroup
    private lateinit var tbDailyGoal: ToggleButton
    private lateinit var cbNotification: CheckBox
    private lateinit var btnCalculate: Button

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "Notification permission granted")
        } else {
            Log.d(TAG, "Notification permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called")
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        etSteps = findViewById(R.id.etSteps)
        rgActivityType = findViewById(R.id.rgActivityType)
        tbDailyGoal = findViewById(R.id.tbDailyGoal)
        cbNotification = findViewById(R.id.cbNotification)
        btnCalculate = findViewById(R.id.btnCalculate)

        // Request focus on Name field automatically on launch
        etName.requestFocus()

        // Request notification permission on Android 13+ if needed
        checkNotificationPermission()

        btnCalculate.setOnClickListener {
            validateAndNavigate()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun validateAndNavigate() {
        val name = etName.text.toString().trim()
        val stepsStr = etSteps.text.toString().trim()

        // 1. Validate name
        if (name.isEmpty()) {
            etName.error = getString(R.string.validation_name_empty)
            etName.requestFocus()
            return
        }

        // 2. Validate steps
        if (stepsStr.isEmpty()) {
            etSteps.error = getString(R.string.validation_steps_empty)
            etSteps.requestFocus()
            return
        }

        val steps = stepsStr.toIntOrNull()
        if (steps == null || steps < 0) {
            etSteps.error = getString(R.string.validation_steps_invalid)
            etSteps.requestFocus()
            return
        }

        // 3. Validate activity type
        val selectedId = rgActivityType.checkedRadioButtonId
        if (selectedId == -1) {
            android.widget.Toast.makeText(
                this,
                getString(R.string.validation_activity_empty),
                android.widget.Toast.LENGTH_SHORT
            ).show()
            return
        }

        val selectedRadioButton = findViewById<RadioButton>(selectedId)
        val activityType = selectedRadioButton.text.toString()

        // 4. Read ToggleButton and CheckBox status
        val dailyGoal = if (tbDailyGoal.isChecked) "ON" else "OFF"
        val receiveNotification = cbNotification.isChecked

        Log.d(TAG, "Validation passed. Navigating to ResultActivity. Name: $name, Steps: $steps, Activity: $activityType, Goal: $dailyGoal, Notify: $receiveNotification")

        // 5. Navigate to ResultActivity using Explicit Intent
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(EXTRA_NAME, name)
            putExtra(EXTRA_STEPS, stepsStr)
            putExtra(EXTRA_ACTIVITY, activityType)
            putExtra(EXTRA_GOAL, dailyGoal)
            putExtra(EXTRA_NOTIFY, receiveNotification)
        }
        startActivity(intent)
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
