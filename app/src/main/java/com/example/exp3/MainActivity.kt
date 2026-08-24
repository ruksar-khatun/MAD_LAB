package com.example.exp3

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.slidingpanelayout.widget.SlidingPaneLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        val slidingPaneLayout = findViewById<SlidingPaneLayout>(R.id.slidingPaneLayout)

        // Handle back button for SlidingPaneLayout
        val callback = object : OnBackPressedCallback(slidingPaneLayout.isOpen && slidingPaneLayout.isSlideable) {
            override fun handleOnBackPressed() {
                slidingPaneLayout.closePane()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        
        slidingPaneLayout.addPanelSlideListener(object : SlidingPaneLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {}
            override fun onPanelOpened(panel: View) {
                callback.isEnabled = true
            }
            override fun onPanelClosed(panel: View) {
                callback.isEnabled = false
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}