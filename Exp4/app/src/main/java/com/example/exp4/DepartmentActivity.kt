package com.example.exp4

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.slidingpanelayout.widget.SlidingPaneLayout

class DepartmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_department)

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

        val departmentRoot = findViewById<View>(R.id.departmentRoot)
        departmentRoot?.let { v ->
            ViewCompat.setOnApplyWindowInsetsListener(v) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}