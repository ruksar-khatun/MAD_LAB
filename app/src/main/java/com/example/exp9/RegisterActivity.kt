package com.example.exp9

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.exp9.databinding.ActivityRegisterBinding
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var draftManager: DraftManager

    private var isRestoringDraft = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        draftManager = DraftManager(this)

        restoreDraftIfAvailable()
        setupTextChangeListeners()

        binding.btnSignUp.setOnClickListener {
            handleRegistration()
        }

        binding.tvLoginNav.setOnClickListener {
            navigateToLogin()
        }

        binding.tvClearForm.setOnClickListener {
            clearFormAndDraft()
        }
    }

    private fun restoreDraftIfAvailable() {
        if (draftManager.hasDraft()) {
            isRestoringDraft = true

            val fullName = draftManager.getDraftFullName()
            val email = draftManager.getDraftEmail()
            val username = draftManager.getDraftUsername()

            if (fullName.isNotEmpty()) binding.etFullName.setText(fullName)
            if (email.isNotEmpty()) binding.etEmail.setText(email)
            if (username.isNotEmpty()) binding.etUsername.setText(username)

            binding.tvDraftBanner.visibility = View.VISIBLE
            Toast.makeText(
                this,
                getString(R.string.draft_restored),
                Toast.LENGTH_SHORT
            ).show()

            isRestoringDraft = false
        } else {
            binding.tvDraftBanner.visibility = View.GONE
        }
    }

    private fun setupTextChangeListeners() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!isRestoringDraft) {
                    saveCurrentDraft()
                }
            }
        }

        binding.etFullName.addTextChangedListener(watcher)
        binding.etEmail.addTextChangedListener(watcher)
        binding.etUsername.addTextChangedListener(watcher)
    }

    private fun saveCurrentDraft() {
        val fullName = binding.etFullName.text.toString()
        val email = binding.etEmail.text.toString()
        val username = binding.etUsername.text.toString()

        draftManager.saveDraft(fullName, email, username)
    }

    private fun clearFormAndDraft() {
        isRestoringDraft = true
        binding.etFullName.setText("")
        binding.etEmail.setText("")
        binding.etUsername.setText("")
        binding.etPassword.setText("")
        binding.etConfirmPassword.setText("")
        clearErrors()

        draftManager.clearDraft()
        binding.tvDraftBanner.visibility = View.GONE

        isRestoringDraft = false
        Toast.makeText(this, "Form cleared.", Toast.LENGTH_SHORT).show()
    }

    private fun handleRegistration() {
        clearErrors()

        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        var isValid = true

        if (fullName.isEmpty()) {
            binding.tilFullName.error = "Full Name is required"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email Address is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Enter a valid email address"
            isValid = false
        }

        if (username.isEmpty()) {
            binding.tilUsername.error = "Username is required"
            isValid = false
        } else if (username.length < 3) {
            binding.tilUsername.error = "Username must be at least 3 characters"
            isValid = false
        }

        val passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")
        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            isValid = false
        } else if (!passwordPattern.matcher(password).matches()) {
            binding.tilPassword.error =
                "Password must be at least 8 characters with at least one letter and one number"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Passwords do not match"
            isValid = false
        }

        if (!isValid) return

        val user = User(
            fullName = fullName,
            email = email,
            username = username
        )

        when (val result = dbHelper.registerUser(user, password)) {
            is DatabaseHelper.RegisterResult.Success -> {
                // Clear draft on successful registration
                draftManager.clearDraft()

                Toast.makeText(
                    this,
                    "Account registered successfully! Please log in.",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, LoginActivity::class.java).apply {
                    putExtra("EXTRA_USERNAME_OR_EMAIL", username)
                }
                startActivity(intent)
                finish()
            }
            is DatabaseHelper.RegisterResult.EmailExists -> {
                binding.tilEmail.error = "An account with this email already exists"
            }
            is DatabaseHelper.RegisterResult.UsernameExists -> {
                binding.tilUsername.error = "This username is already taken"
            }
            is DatabaseHelper.RegisterResult.Failed -> {
                Toast.makeText(
                    this,
                    "Registration failed due to a database error. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun clearErrors() {
        binding.tilFullName.error = null
        binding.tilEmail.error = null
        binding.tilUsername.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
