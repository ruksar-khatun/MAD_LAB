# FitLife – Activity Tracker

A complete, beginner-friendly Android application developed in **Kotlin** and **XML layouts** for Mobile Application Development (MAD) lab practicals and viva voce.

---

## 📋 Overview

**FitLife** is an activity tracking application designed to demonstrate fundamental Android development concepts, including UI design with XML, explicit intents with data passing, input validation, status bar notifications with notification channels, and activity lifecycle monitoring via Logcat.

---

## 🛠️ Core Experiments & Components

1. **User Interface (XML Layouts)**:
   - Built using `ScrollView`, `LinearLayout`, `EditText`, `RadioGroup`, `RadioButton`, `ToggleButton`, `CheckBox`, and `Button`.
   - Fitness-themed Material Design styling with custom primary colors and vector assets.

2. **Input Validation**:
   - Ensures student name is not empty.
   - Validates that the number of steps is entered and strictly numeric.
   - Ensures an activity type (Walking, Running, Cycling) is selected from the RadioGroup.

3. **Explicit Intents & Navigation**:
   - Navigates from `MainActivity` to `ResultActivity` upon clicking **Calculate Activity**.
   - Passes data securely using Intent extras (`EXTRA_NAME`, `EXTRA_STEPS`, `EXTRA_ACTIVITY`, `EXTRA_GOAL`, `EXTRA_NOTIFY`).

4. **Notifications**:
   - Generates a notification informing the student that their activity has been recorded when the notification checkbox is checked.
   - Utilizes `NotificationChannel` (API 26+) and handles runtime `POST_NOTIFICATIONS` permission (API 33+).

5. **Activity Lifecycle**:
   - Implements and logs `onCreate()`, `onStart()`, `onResume()`, `onPause()`, `onStop()`, and `onDestroy()` using `Log.d()` in both activities to demonstrate state transitions in Android Studio Logcat.

---

## 📂 Project Folder Structure

```text
FitLife/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/
│           │   └── com/
│           │       └── example/
│           │           └── fitlife/
│           │               ├── MainActivity.kt
│           │               └── ResultActivity.kt
│           └── res/
│               ├── drawable/
│               │   └── ic_fitness_logo.xml
│               ├── layout/
│               │   ├── activity_main.xml
│               │   └── activity_result.xml
│               └── values/
│                   ├── colors.xml
│                   ├── strings.xml
│                   └── themes.xml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 💻 Key Code Snippets

### 1. MainActivity Validation & Intent Navigation (`MainActivity.kt`)
```kotlin
private fun validateAndNavigate() {
    val name = etName.text.toString().trim()
    val stepsStr = etSteps.text.toString().trim()

    if (name.isEmpty()) {
        etName.error = getString(R.string.validation_name_empty)
        etName.requestFocus()
        return
    }

    if (stepsStr.isEmpty()) {
        etSteps.error = getString(R.string.validation_steps_empty)
        etSteps.requestFocus()
        return
    }

    val steps = stepsStr.toIntOrNull()
    if (steps == null || steps < 0) {
        etSteps.error = getString(R.string.validation_steps_invalid)
        etSteps.requestFocus()
        return
    }

    val selectedId = rgActivityType.checkedRadioButtonId
    if (selectedId == -1) {
        Toast.makeText(this, getString(R.string.validation_activity_empty), Toast.LENGTH_SHORT).show()
        return
    }

    val selectedRadioButton = findViewById<RadioButton>(selectedId)
    val activityType = selectedRadioButton.text.toString()
    val dailyGoal = if (tbDailyGoal.isChecked) "ON" else "OFF"
    val receiveNotification = cbNotification.isChecked

    val intent = Intent(this, ResultActivity::class.java).apply {
        putExtra(EXTRA_NAME, name)
        putExtra(EXTRA_STEPS, stepsStr)
        putExtra(EXTRA_ACTIVITY, activityType)
        putExtra(EXTRA_GOAL, dailyGoal)
        putExtra(EXTRA_NOTIFY, receiveNotification)
    }
    startActivity(intent)
}
```

### 2. Result Activity & Notification Generation (`ResultActivity.kt`)
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_result)

    val name = intent.getStringExtra(MainActivity.EXTRA_NAME) ?: "N/A"
    val steps = intent.getStringExtra(MainActivity.EXTRA_STEPS) ?: "0"
    val activityType = intent.getStringExtra(MainActivity.EXTRA_ACTIVITY) ?: "N/A"
    val goal = intent.getStringExtra(MainActivity.EXTRA_GOAL) ?: "OFF"
    val notify = intent.getBooleanExtra(MainActivity.EXTRA_NOTIFY, false)

    findViewById<TextView>(R.id.tvResultName).text = name
    findViewById<TextView>(R.id.tvResultSteps).text = steps
    findViewById<TextView>(R.id.tvResultActivity).text = activityType
    findViewById<TextView>(R.id.tvResultGoal).text = goal

    if (notify) {
        sendActivityNotification(name, activityType)
    }
}
```

---

## 📱 App Output & Screenshots

Below are the execution screenshots of the **FitLife** app:

### 1. Main Activity Input Screen
![Main Input Activity](img1.jpeg)

### 2. Result Activity Display Screen
![Result Activity](img2.jpeg)

### 3. Activity Notification Output
![Notification Output](img3.jpeg)

---

## 🎯 Conclusion

The **FitLife – Activity Tracker** project successfully demonstrates core Android development concepts using modern Kotlin and traditional XML layouts without unnecessary complexity. It serves as an ideal template for Mobile Application Development (MAD) lab practical demonstrations, satisfying all requirements for input validation, explicit intents, notifications, and activity lifecycle management.
