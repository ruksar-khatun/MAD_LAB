# Experiment 6: Basic Android Views - Student Registration Form

## Overview
This experiment demonstrates the implementation of a professional, single-screen **Student Registration Form** in Android using a variety of basic UI components. The application focuses on creating a clean, minimal academic-style interface using a specific color palette: **Deep Sea (#156874)** and **Steam (#EDE6E6)**.

## Core Experiments
- Creating a scrollable registration form with multiple input types.
- Implementing a custom-styled UI with rounded corners and circular icons.
- Handling user interactions for various views like Buttons, CheckBoxes, and RadioGroups.
- Displaying a summary of user input in a modal pop-up (AlertDialog).

## Concept and Technology
- **Layouts**: `ScrollView`, `LinearLayout`, `RelativeLayout`.
- **UI Components**: `TextView`, `EditText`, `Button`, `ImageButton`, `CheckBox`, `ToggleButton`, `RadioButton`, `RadioGroup`, `Spinner`.
- **Material Design**: `TextInputLayout` for password visibility toggle.
- **Language**: Kotlin.
- **Styling**: XML Drawables for custom shapes (rounded inputs, circular profile).

## Code Snippet

### MainActivity.kt
```kotlin
// Logic for binding views and handling registration
val btnRegister = findViewById<Button>(R.id.btnRegister)
btnRegister.setOnClickListener {
    val name = etFullName.text.toString()
    val phone = "${spCountryCode.selectedItem} ${etPhone.text}"
    
    // Show details in a pop-up
    AlertDialog.Builder(this)
        .setTitle("Registration Successful")
        .setMessage("Registered successfully with details: \nName: $name\nPhone: $phone...")
        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        .show()
}
```

### activity_main.xml
```xml
<!-- Example of the custom styled Password field with toggle -->
<com.google.android.material.textfield.TextInputLayout
    android:id="@+id/tilPassword"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:endIconMode="password_toggle">
    
    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/etPassword"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/rounded_input"
        android:inputType="textPassword" />
</com.google.android.material.textfield.TextInputLayout>
```

## Folder Structure
```
Exp6/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/exp6/
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/
│   │   │       ├── drawable/
│   │   │       │   ├── circular_profile.xml
│   │   │       │   └── rounded_input.xml
│   │   │       ├── layout/
│   │   │       │   └── activity_main.xml
│   │   │       └── values/
│   │   │           ├── colors.xml
│   │   │           └── strings.xml
├── img.png
└── img_1.png
```

## Output
The application provides a seamless registration experience. Below are the visual representations:

| Screenshot | Description |
| :--- | :--- |
| ![Main UI](img.png) | **Registration Form**: Shows the professional academic UI with the circular profile icon, country code picker, and password toggle. |
| ![Success Pop-up](img_1.png) | **Success Pop-up**: Displays the final registration summary in an AlertDialog after clicking the Register button. |
