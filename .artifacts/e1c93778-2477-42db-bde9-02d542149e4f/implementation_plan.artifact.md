# Implementation Plan - FitLife Activity Tracker App

Develop a complete Android Studio application named "FitLife – Activity Tracker" using Kotlin and XML layouts, designed for a MAD lab practical/viva.

## User Review Required

> [!IMPORTANT]
> - **Permissions**: Added `android.permission.POST_NOTIFICATIONS` in `AndroidManifest.xml` for Android 13+ notification support, along with runtime permission check in `MainActivity`.
> - **Lifecycle Logging**: `Log.d()` calls for `onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, and `onDestroy` will be added to both `MainActivity` and `ResultActivity` for Logcat demonstration during viva.

## Open Questions

- None. All requirements are fully specified in the prompt.

## Proposed Changes

### [Manifest & Build Configuration]

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/AndroidManifest.xml)
- Add `android.permission.POST_NOTIFICATIONS` permission.
- Register `ResultActivity`.

#### [MODIFY] [strings.xml](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/res/values/strings.xml)
- Add string resources for app title, labels, hint texts, radio buttons, toggle button, checkbox, calculate button, result activity titles, etc.

#### [MODIFY] [colors.xml](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/res/values/colors.xml)
- Add fitness-themed colors (primary green, accent colors, surface colors).

#### [MODIFY] [themes.xml](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/res/values/themes.xml)
- Ensure clean Material components styling.

---

### [Drawables & Layouts]

#### [NEW] [ic_fitness_logo.xml](file:///C:/Users/rukses/AndroidStudioProjects/FitLife/app/src/main/res/drawable/ic_fitness_logo.xml) (or vector drawable)
- Fitness header image/icon.

#### [MODIFY] [activity_main.xml](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/res/layout/activity_main.xml)
- Implement ScrollView containing:
  - Fitness image and Title
  - Name EditText
  - Steps EditText (numeric input)
  - RadioGroup with RadioButtons: Walking, Running, Cycling
  - ToggleButton for Daily Goal
  - CheckBox for "Receive Activity Notification"
  - Button for "Calculate Activity"

#### [NEW] [activity_result.xml](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/res/layout/activity_result.xml)
- Implement UI for ResultActivity displaying:
  - Title
  - Student Name
  - Number of Steps
  - Selected Activity
  - Daily Goal Status
  - Back / Return button

---

### [Kotlin Source Code]

#### [MODIFY] [MainActivity.kt](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/java/com/example/fitlife/MainActivity.kt)
- Implement Activity lifecycle logging (`onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onDestroy`).
- Handle "Calculate Activity" click validation (Name not empty, Steps not empty & numeric, Activity type selected).
- Read ToggleButton state and CheckBox state.
- Handle runtime `POST_NOTIFICATIONS` permission request for API 33+.
- Send explicit Intent with extras (`EXTRA_NAME`, `EXTRA_STEPS`, `EXTRA_ACTIVITY`, `EXTRA_GOAL`, `EXTRA_NOTIFY`).

#### [NEW] [ResultActivity.kt](file:///C:/Users/ruksa/AndroidStudioProjects/FitLife/app/src/main/java/com/example/fitlife/ResultActivity.kt)
- Implement Activity lifecycle logging (`onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onDestroy`).
- Retrieve intent extras and display student name, steps, activity type, and daily goal status.
- Generate notification channel and notification if notification was requested (`EXTRA_NOTIFY` == true).

---

## Verification Plan

### Automated Tests
- Gradle build verification (`gradle_build` tool) to ensure compilation without errors.

### Manual Verification
- Instructions for running the app in Android Emulator/Device.
- Testing validation (empty name/steps, unselected activity).
- Testing successful navigation and result display.
- Demonstrating notification generation and handling `POST_NOTIFICATIONS`.
- Demonstrating Activity Lifecycle methods in Logcat.
