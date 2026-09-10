# Walkthrough - FitLife Activity Tracker App

We have successfully created and verified the **FitLife – Activity Tracker** Android application in Kotlin using XML layouts, strictly adhering to all requirements for MAD lab practical and viva.

---

## 1. Project Overview & Package Name
- **Project Name**: FitLife
- **Package Name**: `com.example.fitlife`
- **Technology Stack**: Kotlin only, XML layouts, Standard Android APIs, Material 3 components.

---

## 2. Complete File Listings & Code

### 2.1 Gradle Configuration (`app/build.gradle.kts`)
```kotlin
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.fitlife"
    compileSdk {
        version = release(37) {
            minorApiLevel = 0
        }
    }

    defaultConfig {
        applicationId = "com.example.fitlife"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
```

### 2.2 AndroidManifest.xml (`app/src/main/AndroidManifest.xml`)
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.FitLife">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity
            android:name=".ResultActivity"
            android:exported="false"
            android:parentActivityName=".MainActivity" />
    </application>

</manifest>
```

### 2.3 Resources (`res/values/strings.xml`)
```xml
<resources>
    <string name="app_name">FitLife</string>
    <string name="activity_tracker_title">FitLife Activity Tracker</string>
    <string name="prompt_name">Enter Your Name</string>
    <string name="prompt_steps">Number of Steps (e.g. 5000)</string>
    <string name="label_activity_type">Select Activity Type:</string>
    <string name="radio_walking">Walking</string>
    <string name="radio_running">Running</string>
    <string name="radio_cycling">Cycling</string>
    <string name="label_daily_goal">Daily Goal</string>
    <string name="label_notification">Receive Activity Notification</string>
    <string name="btn_calculate">Calculate Activity</string>
    <string name="title_result">Activity Result</string>
    <string name="label_student_name">Student Name:</string>
    <string name="label_steps_result">Steps Completed:</string>
    <string name="label_activity_result">Activity Type:</string>
    <string name="label_goal_result">Daily Goal Status:</string>
    <string name="btn_back">Back</string>
    <string name="validation_name_empty">Please enter your name</string>
    <string name="validation_steps_empty">Please enter number of steps</string>
    <string name="validation_steps_invalid">Please enter a valid numeric number of steps</string>
    <string name="validation_activity_empty">Please select an activity type</string>
    <string name="notification_title">FitLife Activity Recorded</string>
    <string name="notification_text">Your fitness activity has been successfully recorded!</string>
</resources>
```

### 2.4 Resources (`res/values/colors.xml`)
```xml
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    <color name="fit_primary">#2E7D32</color>
    <color name="fit_primary_dark">#1B5E20</color>
    <color name="fit_accent">#81C784</color>
    <color name="fit_background">#F1F8E9</color>
    <color name="card_background">#FFFFFF</color>
    <color name="text_dark">#212121</color>
    <color name="text_muted">#757575</color>
</resources>
```

### 2.5 Drawable (`res/drawable/ic_fitness_logo.xml`)
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="96dp"
    android:height="96dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
  <path
      android:fillColor="#2E7D32"
      android:path="M12,2A10,10 0 0,1 22,12A10,10 0 0,1 12,22A10,10 0 0,1 2,12A10,10 0 0,1 12,2M12,4A8,8 0 0,0 4,12A8,8 0 0,0 12,20A8,8 0 0,0 20,12A8,8 0 0,0 12,4M12,6A6,6 0 0,1 18,12A6,6 0 0,1 12,18A6,6 0 0,1 6,12A6,6 0 0,1 12,6M12,8A4,4 0 0,0 8,12A4,4 0 0,0 12,16A4,4 0 0,0 16,12A4,4 0 0,0 12,8Z"/>
</vector>
```

### 2.6 Layouts (`res/layout/activity_main.xml` & `res/layout/activity_result.xml`)
- [activity_main.xml](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/res/layout/activity_main.xml)
- [activity_result.xml](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/res/layout/activity_result.xml)

### 2.7 Source Code (`MainActivity.kt` & `ResultActivity.kt`)
- [MainActivity.kt](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/java/com/example/fitlife/MainActivity.kt)
- [ResultActivity.kt](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/java/com/example/fitlife/ResultActivity.kt)

---

## 3. Running & Testing Instructions

### Android Studio Setup Steps
1. Open Android Studio and select **Open** -> Choose folder `C:\Users\ruksa\AndroidStudioProjects\FitLife`.
2. Wait for Gradle sync to complete successfully.
3. Connect an Android device (API 24+) or start an Android Emulator (API 33+ recommended for notification permission testing).

### How to Run
- Click the **Run** (green play) button in Android Studio or execute `gradle_build(app:assembleDebug)`.

### How to Test Requirements
1. **Validation**:
   - Leave Name empty and click **Calculate Activity** -> Verify error prompt.
   - Leave Steps empty or enter non-numeric text -> Verify error prompt.
   - Do not select any Activity RadioButton -> Verify Toast prompt.
2. **Explicit Intent & Navigation**:
   - Fill in valid data and click **Calculate Activity** -> Navigates smoothly to `ResultActivity` displaying student name, steps, activity type, and goal status.
3. **Notification**:
   - Check **Receive Activity Notification**, fill details, and click Calculate -> Generates a status bar notification (`NotificationChannel` ID `fitlife_activity_channel`).
4. **Activity Lifecycle**:
   - Open Logcat in Android Studio, filter by tag `MainActivityLifecycle` or `ResultActivityLifecycle`.
   - Observe `onCreate()`, `onStart()`, `onResume()` on launch.
   - Press Home or back to observe `onPause()`, `onStop()`, and `onDestroy()`.

### Sample Test Case
- **Name**: `Ruks`
- **Steps**: `5000`
- **Activity**: `Running`
- **Daily Goal**: `ON`
- **Notification**: `Checked (ON)`

---

## 4. Requirement Checklist

| Requirement | Status | Implementation Details |
| :--- | :---: | :--- |
| Kotlin only (No Java) | [x] | 100% Kotlin source files (`MainActivity.kt`, `ResultActivity.kt`) |
| XML Layouts (No Compose) | [x] | `activity_main.xml` and `activity_result.xml` using ScrollView & Material widgets |
| App title & fitness image | [x] | Header ImageView with `ic_fitness_logo` and title TextView |
| Name EditText | [x] | `etName` with `inputType="textPersonName"` |
| Steps EditText (numeric) | [x] | `etSteps` with `inputType="number"` |
| Activity Type RadioButtons | [x] | RadioGroup (`rgActivityType`) with Walking, Running, Cycling |
| Daily Goal ToggleButton | [x] | `tbDailyGoal` (ON/OFF) |
| Notification CheckBox | [x] | `cbNotification` ("Receive Activity Notification") |
| Calculate Activity Button | [x] | `btnCalculate` with input validations & explicit intent |
| ResultActivity display | [x] | Student Name, Steps, Activity, Goal Status |
| Notification generation | [x] | `NotificationChannel` (API 26+) + `POST_NOTIFICATIONS` permission |
| Activity Lifecycle Logging | [x] | `Log.d()` for `onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onDestroy` |

---

## 5. MAD Viva Questions & Simple Answers

1. **What is an Intent in Android?**
   - An Intent is a messaging object used to request an action from another app component, such as starting an Activity (`ResultActivity`).
2. **What is the difference between Explicit and Implicit Intent?**
   - An Explicit Intent specifies the exact target component class (e.g., `ResultActivity::class.java`), whereas an Implicit Intent declares a general action to be performed by any app capable of handling it.
3. **How do you pass data between activities?**
   - Using Intent extras with `.putExtra(key, value)` in the sender activity and retrieving them via `intent.getStringExtra(key)` in the receiver activity.
4. **Explain the Activity Lifecycle states.**
   - `onCreate()` (created), `onStart()` (visible), `onResume()` (interactive/focused), `onPause()` (losing focus), `onStop()` (no longer visible), `onDestroy()` (destroyed).
5. **What is the purpose of `NotificationChannel`?**
   - Introduced in Android 8.0 (API 26), notification channels allow you to group notifications and let users customize notification categories and priority settings.
