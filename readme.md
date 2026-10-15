# Experiment 10: Implement Data Sharing in Android Using the Contacts Content Provider

**ContactShare — Android Contact Management and Sharing Application**

---

## Overview

**ContactShare** is a native Android application developed using **Kotlin** and **XML** layouts. The application connects to the device's **Contacts Content Provider** via `ContentResolver` to query and display real contacts saved on the user's phone. Users can search through their contacts in real time, view individual contact details, and share contact information with other applications using Android's native Sharesheet interface.

To ensure privacy and security, ContactShare implements Android **runtime permissions** (`READ_CONTACTS`), requesting user authorization before reading any device contact data and providing clean explanation screens when permission is needed or denied.

---

## Objectives

- Access device contacts securely using the **Contacts Content Provider**.
- Query contact display names and phone numbers using `ContentResolver` and `Cursor`.
- Display retrieved contacts in a responsive `RecyclerView` list sorted alphabetically.
- Implement live contact search filtering by name or phone number.
- Display detailed contact information on a dedicated details screen.
- Share contact information using an implicit `Intent` and Android's native Sharesheet chooser.
- Handle Android runtime permissions safely following security best practices.

---

## Technologies Used

| Technology | Purpose |
|---|---|
| Kotlin | Application logic and asynchronous background queries |
| XML | Layout design and visual styling |
| Android Studio | Official Android Integrated Development Environment (IDE) |
| ContactsContract | System contract API for accessing Android contact data |
| ContentResolver | Client interface used to query the Contacts Content Provider |
| RecyclerView | Efficiently rendering the list of contacts in cards |
| Intents | Executing implicit share and call actions across apps |
| Android Runtime Permissions | Requesting dynamic `READ_CONTACTS` authorization |

---

## Key Features

- **Real Device Contact Querying**: Retrieves live contact names and phone numbers from the phone's address book without modifying or storing duplicate copies.
- **Initial-Based Avatar Generation**: Automatically derives circular initial avatars from each contact's display name.
- **Live Search Filtering**: Filters contacts instantaneously by name or phone number as the user types into the search bar.
- **Contact Details View**: Tapping any contact card opens a dedicated `ContactDetailsActivity` displaying contact name, phone number, and optional email address.
- **Native Contact Sharing**: Integrates with Android's native Sharesheet chooser (`Intent.ACTION_SEND`) to share contact details via messaging, email, or social applications.
- **Runtime Permission Safeguards**: Requests `READ_CONTACTS` at runtime with an explanation card and an option to open app settings if permission is denied.
- **Empty & Error States**: Displays informative empty-state views when no contacts match a search query or when permissions are missing.
- **Modern Lavender & Purple Theme**: Styled with a light lavender background (`#F4F0FA`), primary purple headers (`#7652C8`), white rounded cards (`#FFFFFF`), and footer attribution.
- **Automatic Refresh**: Refreshes the contact list when resuming the application to reflect changes made in the phone's Contacts app.

---

## Code Snippets

### A. Reading Contacts via ContentResolver

Queries `ContactsContract.CommonDataKinds.Phone.CONTENT_URI` using `ContentResolver` off the UI thread and reads contact names and phone numbers safely using a `Cursor`:

```kotlin
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
```

---

### B. Sharing Contact Details via Implicit Intent

Prepares a formatted contact message and triggers Android's system Sharesheet chooser using `Intent.ACTION_SEND`:

```kotlin
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
```

---

### C. Runtime Permission Declaration and Request

Declares `READ_CONTACTS` in `AndroidManifest.xml` and requests it dynamically at runtime using `ActivityResultContracts.RequestPermission`:

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

```kotlin
// MainActivity.kt
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
```

---

## Project Structure

```
Exp10/
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── example/
│           │           └── exp10/
│           │               ├── Contact.kt                  # Contact data model
│           │               ├── ContactAdapter.kt           # RecyclerView adapter
│           │               ├── ContactDetailsActivity.kt   # Contact details activity
│           │               └── MainActivity.kt             # Main Activity & ContentProvider query
│           ├── res/
│           │   ├── drawable/
│           │   │   ├── bg_avatar_circle.xml        # Oval purple avatar background
│           │   │   ├── bg_card_rounded.xml         # White rounded card background
│           │   │   ├── bg_search_bar.xml           # Search bar input background
│           │   │   ├── ic_back.xml                 # Back navigation icon
│           │   │   ├── ic_contacts.xml             # Contacts header icon
│           │   │   ├── ic_email.xml                # Email icon
│           │   │   ├── ic_lock.xml                 # Permission explanation icon
│           │   │   ├── ic_person.xml               # Person avatar icon
│           │   │   ├── ic_phone.xml                # Phone icon
│           │   │   ├── ic_search.xml               # Search bar icon
│           │   │   └── ic_share.xml                # Share action icon
│           │   ├── layout/
│           │   │   ├── activity_contact_details.xml# Contact details layout
│           │   │   ├── activity_main.xml           # Main contacts screen layout
│           │   │   └── item_contact.xml            # Individual contact row card
│           │   └── values/
│           │       ├── colors.xml                  # Theme color definitions
│           │       ├── strings.xml                 # String resources & templates
│           │       └── themes.xml                  # Application Material3 theme
│           └── AndroidManifest.xml                 # Application manifest & permissions
├── img.jpeg                                        # Screenshot: Main contacts list screen
├── img1.jpeg                                       # Screenshot: Live search & system Sharesheet
└── readme.md                                       # Project documentation
```

### Purpose of Important Files

- `MainActivity.kt`: Initializes the application, handles `READ_CONTACTS` runtime permissions, queries the `ContactsContract` ContentProvider asynchronously, manages search filtering, and binds the contact `RecyclerView`.
- `ContactDetailsActivity.kt`: Displays detailed information (name, phone, email) for a selected contact with quick call and share action buttons.
- `ContactAdapter.kt`: Binds individual contact items to rounded card views in the `RecyclerView`.
- `Contact.kt`: Data class representing a contact with properties for `name`, `number`, `email`, and dynamic `initial`.
- `AndroidManifest.xml`: Declares the required `READ_CONTACTS` permission and activity components.
- `activity_main.xml`: Contains the header bar, welcome section card, live search input bar, progress spinner, permission explanation container, empty state container, contact `RecyclerView`, and footer attribution.

---

## Application Screenshots

### Screenshot 1 — Main Contacts List View

![ContactShare Main Contacts List](img.jpeg)

*Caption: Main screen of ContactShare displaying real device contacts retrieved via ContentResolver in a structured RecyclerView list with avatar initials and total contact count.*

**Description**:
This screenshot shows the main interface of **ContactShare** running on a physical Android device. It demonstrates:
- Top purple header bar with the application icon, title, and subtitle ("Your contacts, shared simply").
- **Welcome Section**: Displays "Your Contacts" and the dynamic total contact count badge ("368 contacts found").
- **Search Bar**: Input field for real-time contact filtering.
- **Structured Contact Cards**: Shows contacts sorted alphabetically (e.g., ABDUL, Abhinav, Abhishek, Abtab, Adnan, Adrish Chowdhury, Adut Phupha) inside clean white rounded cards with purple circular initial avatars, phone numbers, and individual Share action buttons.

---

### Screenshot 2 — Live Search and System Sharesheet Integration

![ContactShare Search and Native Sharesheet](img1.jpeg)

*Caption: Live contact search filtering by name ("heta") and system Sharesheet chooser launched via implicit Intent.*

**Description**:
This screenshot demonstrates live search filtering and data sharing via implicit Intents:
- **Live Search**: Filtering contacts by typing `"heta"` into the search bar updates the list instantaneously to show matching contacts ("Hetal") and updates the count badge ("2 contacts found").
- **Native Sharing**: Tapping the Share button on a contact opens Android's system Sharesheet chooser ("Sharing text") displaying the structured contact details (`Contact Details: Name: Hetal, Phone: 70415 04232`) along with target sharing apps such as Quick Share, WhatsApp, Files, ChatGPT, and Instagram.

---

## Expected Output

When running **ContactShare**:
1. **Permission Request**: Upon first launch, the app prompts for the `READ_CONTACTS` runtime permission. If granted, contacts load immediately. If denied, a friendly explanation card appears with a **Grant Permission** button and an option to open settings.
2. **Contact Display**: Contacts retrieved from the phone's address book are displayed alphabetically in white rounded cards with circular initial avatars and phone numbers.
3. **Live Search**: Typing a name or phone number into the search bar dynamically updates the displayed contact list and total count.
4. **Contact Details**: Tapping a contact row opens `ContactDetailsActivity`, showing full contact details with options to call or share.
5. **Contact Sharing**: Tapping the Share button on any contact card or details screen opens the native Android Sharesheet pre-populated with the contact's details.

---

## Conclusion

This experiment successfully demonstrates accessing Android system data through the **Contacts Content Provider** using `ContentResolver` and `Cursor`-based asynchronous queries. It highlights best practices in Android application development, including dynamic runtime permission handling (`READ_CONTACTS`), live data filtering with `TextWatcher`, responsive UI design using `RecyclerView` and Material Design cards, and inter-app communication using implicit `Intent` actions and system chooser dialogs.

---

*Footer: Ruksar Khatun | 25MCAR0115*
