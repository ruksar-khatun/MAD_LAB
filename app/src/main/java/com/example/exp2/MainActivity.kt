package com.example.exp2

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvCurrentState: TextView

    companion object {
        private const val TAG = "MainActivityLifecycle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCurrentState = findViewById(R.id.tvCurrentState)
        updateState("onCreate()")
        Log.d(TAG, "onCreate called")
        AppToaster.show(this, "Activity Created")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        updateState("onStart()")
        Log.d(TAG, "onStart called")
        AppToaster.show(this, "Application Started")
    }

    override fun onResume() {
        super.onResume()
        updateState("onResume()")
        Log.d(TAG, "onResume called")
        AppToaster.show(this, "Name : Ruksar Khatun")
    }

    override fun onPause() {
        super.onPause()
        updateState("onPause()")
        Log.d(TAG, "onPause called")
        AppToaster.show(this, "ID : 25MCAR0115")
    }

    override fun onRestart() {
        super.onRestart()
        updateState("onRestart()")
        Log.d(TAG, "onRestart called")
        AppToaster.show(this, "Activity Restarted")
    }

    override fun onStop() {
        super.onStop()
        updateState("onStop()")
        Log.d(TAG, "onStop called")
        AppToaster.show(this, "Activity Stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
        AppToaster.show(applicationContext, "Activity Destroyed")
    }

    private fun updateState(stateName: String) {
        if (::tvCurrentState.isInitialized) {
            tvCurrentState.text = "Current State: $stateName"
        }
    }
}