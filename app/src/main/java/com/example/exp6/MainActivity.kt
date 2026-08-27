package com.example.exp6

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Binding Views
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val spCountryCode = findViewById<Spinner>(R.id.spCountryCode)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val tbNotifications = findViewById<ToggleButton>(R.id.tbNotifications)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnProfile = findViewById<ImageButton>(R.id.btnProfile)

        // Set up Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.country_codes,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCountryCode.adapter = adapter

        btnProfile.setOnClickListener {
            Toast.makeText(this, "Profile photo selection clicked", Toast.LENGTH_SHORT).show()
        }

        btnRegister.setOnClickListener {
            val name = etFullName.text.toString()
            val email = etEmail.text.toString()
            val countryCode = spCountryCode.selectedItem.toString()
            val phoneNumber = etPhone.text.toString()
            val phone = "$countryCode $phoneNumber"
            val password = etPassword.text.toString()
            
            val selectedGenderId = rgGender.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                "Not specified"
            }
            
            val termsAccepted = if (cbTerms.isChecked) "Yes" else "No"
            val notificationsEnabled = if (tbNotifications.isChecked) "ON" else "OFF"

            if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show details in a pop-up (AlertDialog)
            val message = """
                Registered successfully with the following details:
                
                Full Name: $name
                Email: $email
                Phone: $phone
                Gender: $gender
                Terms Accepted: $termsAccepted
                Notifications: $notificationsEnabled
                Password Length: ${password.length}
            """.trimIndent()

            AlertDialog.Builder(this)
                .setTitle("Registration Successful")
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}
