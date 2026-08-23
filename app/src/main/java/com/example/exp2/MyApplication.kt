package com.example.exp2

import android.app.Application
import android.util.Log

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("AppLifecycle", "Application Started")
    }
}