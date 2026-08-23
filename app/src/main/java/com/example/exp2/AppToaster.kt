package com.example.exp2

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast

object AppToaster {
    fun show(context: Context, message: String) {
        try {
            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.layout_custom_toast, null)
            
            val text: TextView = layout.findViewById(R.id.toastMessage)
            text.text = message

            val toast = Toast(context.applicationContext)
            toast.setGravity(Gravity.BOTTOM, 0, 100)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout
            toast.show()
        } catch (e: Exception) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}