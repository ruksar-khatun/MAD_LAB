package com.example.exp10

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ContactDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CONTACT_NAME = "extra_contact_name"
        const val EXTRA_CONTACT_PHONE = "extra_contact_phone"
        const val EXTRA_CONTACT_EMAIL = "extra_contact_email"
    }

    private lateinit var contactName: String
    private lateinit var contactPhone: String
    private lateinit var contactEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contact_details)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        contactName = intent.getStringExtra(EXTRA_CONTACT_NAME) ?: "Unknown Name"
        contactPhone = intent.getStringExtra(EXTRA_CONTACT_PHONE) ?: "No Phone Number"
        contactEmail = intent.getStringExtra(EXTRA_CONTACT_EMAIL) ?: ""

        val tvAvatar: TextView = findViewById(R.id.tv_detail_avatar)
        val tvName: TextView = findViewById(R.id.tv_detail_name)
        val tvPhone: TextView = findViewById(R.id.tv_detail_phone)
        val llEmailContainer: LinearLayout = findViewById(R.id.ll_email_container)
        val tvEmail: TextView = findViewById(R.id.tv_detail_email)
        val btnBack: ImageButton = findViewById(R.id.btn_back)
        val btnShare: Button = findViewById(R.id.btn_share_contact)
        val btnCall: Button = findViewById(R.id.btn_call_contact)

        tvAvatar.text = contactName.trim().firstOrNull()?.uppercase() ?: "?"
        tvName.text = contactName
        tvPhone.text = contactPhone

        if (contactEmail.isNotBlank()) {
            llEmailContainer.visibility = View.VISIBLE
            tvEmail.text = contactEmail
        } else {
            llEmailContainer.visibility = View.GONE
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnShare.setOnClickListener {
            shareContact(contactName, contactPhone, contactEmail)
        }

        btnCall.setOnClickListener {
            if (contactPhone.isNotBlank() && (contactPhone != "No Phone Number")) {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${contactPhone.trim()}")
                }
                startActivity(dialIntent)
            } else {
                Toast.makeText(this, "No valid phone number available to call.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareContact(name: String, phone: String, email: String) {
        val shareMessage = getString(R.string.share_contact_text, name, phone, email.ifBlank { "N/A" })
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Contact Details - $name")
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        val chooser = Intent.createChooser(shareIntent, getString(R.string.share_via))
        try {
            startActivity(chooser)
        } catch (_: Exception) {
            Toast.makeText(this, "No compatible app found to share contact.", Toast.LENGTH_SHORT).show()
        }
    }
}