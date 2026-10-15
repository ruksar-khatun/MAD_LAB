package com.example.exp10

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var tvContactsCount: TextView
    private lateinit var etSearch: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var llPermissionContainer: LinearLayout
    private lateinit var btnGrantPermission: Button
    private lateinit var btnOpenSettings: Button
    private lateinit var llEmptyContainer: LinearLayout
    private lateinit var rvContacts: RecyclerView

    private val allContacts = mutableListOf<Contact>()
    private val filteredContacts = mutableListOf<Contact>()
    private lateinit var contactAdapter: ContactAdapter

    private val executor = Executors.newSingleThreadExecutor()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            hidePermissionUI()
            loadContacts()
        } else {
            showPermissionUI(showSettings = !shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupRecyclerView()
        setupSearchListener()

        btnGrantPermission.setOnClickListener {
            checkAndRequestPermission()
        }

        btnOpenSettings.setOnClickListener {
            openAppSettings()
        }

        checkAndRequestPermission()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            hidePermissionUI()
            loadContacts()
        }
    }

    private fun initViews() {
        tvContactsCount = findViewById(R.id.tv_contacts_count)
        etSearch = findViewById(R.id.et_search)
        progressBar = findViewById(R.id.progress_bar)
        llPermissionContainer = findViewById(R.id.ll_permission_container)
        btnGrantPermission = findViewById(R.id.btn_grant_permission)
        btnOpenSettings = findViewById(R.id.btn_open_settings)
        llEmptyContainer = findViewById(R.id.ll_empty_container)
        rvContacts = findViewById(R.id.rv_contacts)
    }

    private fun setupRecyclerView() {
        contactAdapter = ContactAdapter(
            contactsList = filteredContacts,
            onContactClick = { contact ->
                openContactDetails(contact)
            },
            onShareClick = { contact ->
                shareContact(contact)
            }
        )
        rvContacts.layoutManager = LinearLayoutManager(this)
        rvContacts.adapter = contactAdapter
    }

    private fun setupSearchListener() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterContacts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkAndRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                hidePermissionUI()
                loadContacts()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                showPermissionUI(showSettings = false)
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun showPermissionUI(showSettings: Boolean = false) {
        llPermissionContainer.visibility = View.VISIBLE
        btnOpenSettings.visibility = if (showSettings) View.VISIBLE else View.GONE
        rvContacts.visibility = View.GONE
        llEmptyContainer.visibility = View.GONE
        progressBar.visibility = View.GONE
        tvContactsCount.text = getString(R.string.contacts_count, 0)
    }

    private fun hidePermissionUI() {
        llPermissionContainer.visibility = View.GONE
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    private fun loadContacts() {
        progressBar.visibility = View.VISIBLE
        rvContacts.visibility = View.GONE
        llEmptyContainer.visibility = View.GONE

        executor.execute {
            val contactList = queryContactsFromProvider()

            runOnUiThread {
                progressBar.visibility = View.GONE
                allContacts.clear()
                allContacts.addAll(contactList)

                val currentSearchQuery = etSearch.text.toString()
                filterContacts(currentSearchQuery)
            }
        }
    }

    private fun queryContactsFromProvider(): List<Contact> {
        val contactsMap = mutableMapOf<String, Contact>()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        try {
            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                null,
                null,
                "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
            )

            cursor?.use {
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (it.moveToNext()) {
                    val name = if (nameIndex != -1) it.getString(nameIndex) ?: "" else ""
                    val number = if (numberIndex != -1) it.getString(numberIndex) ?: "" else ""

                    if (name.isNotBlank() && number.isNotBlank()) {
                        val key = "$name|$number"
                        if (!contactsMap.containsKey(key)) {
                            contactsMap[key] = Contact(name = name, number = number)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return contactsMap.values.sortedBy { it.name.lowercase() }
    }

    private fun filterContacts(query: String) {
        val trimmedQuery = query.trim().lowercase()
        filteredContacts.clear()

        if (trimmedQuery.isEmpty()) {
            filteredContacts.addAll(allContacts)
        } else {
            for (contact in allContacts) {
                if (contact.name.lowercase().contains(trimmedQuery) ||
                    contact.number.lowercase().contains(trimmedQuery) ||
                    contact.email.lowercase().contains(trimmedQuery)
                ) {
                    filteredContacts.add(contact)
                }
            }
        }

        tvContactsCount.text = getString(R.string.contacts_count, filteredContacts.size)
        contactAdapter.updateData(filteredContacts)

        if (filteredContacts.isEmpty()) {
            rvContacts.visibility = View.GONE
            llEmptyContainer.visibility = View.VISIBLE
        } else {
            rvContacts.visibility = View.VISIBLE
            llEmptyContainer.visibility = View.GONE
        }
    }

    private fun openContactDetails(contact: Contact) {
        val intent = Intent(this, ContactDetailsActivity::class.java).apply {
            putExtra(ContactDetailsActivity.EXTRA_CONTACT_NAME, contact.name)
            putExtra(ContactDetailsActivity.EXTRA_CONTACT_PHONE, contact.number)
            putExtra(ContactDetailsActivity.EXTRA_CONTACT_EMAIL, contact.email)
        }
        startActivity(intent)
    }

    private fun shareContact(contact: Contact) {
        val emailFormatted = contact.email.ifBlank { "N/A" }
        val shareMessage = getString(R.string.share_contact_text, contact.name, contact.number, emailFormatted)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Contact Details - ${contact.name}")
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        val chooser = Intent.createChooser(shareIntent, getString(R.string.share_via))
        try {
            startActivity(chooser)
        } catch (_: Exception) {
            Toast.makeText(this, "No compatible application found to share contact.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}