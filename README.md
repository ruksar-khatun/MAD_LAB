# Experiment 5: High-Priority Notification System & Autologin Integration

## Overview
This experiment demonstrates the implementation of a **High-Priority Notification System** in Android, combined with **Automated Authentication** and a **Multi-pane UI** for data management. The project showcases modern Android development practices, including runtime permission handling for Android 13+ and notification channels for Android 8.0+.

## Core Experiments
1.  **Notification Channels**: Creating and configuring a high-importance notification channel to ensure visibility on modern Android versions.
2.  **Runtime Permissions**: Implementing the `POST_NOTIFICATIONS` permission request flow required for Android 13 (API 33) and above.
3.  **High-Priority Notifications**: Using `NotificationCompat.Builder` to fire notifications with `PRIORITY_HIGH`.
4.  **Autologin Logic**: Automating the login process by pre-filling credentials and programmatically triggering UI events.
5.  **Multi-pane Navigation**: Utilizing `SlidingPaneLayout` to manage a list-detail view of university departments.

## Concepts and Technology
- **Language**: Kotlin
- **UI Toolkit**: XML Layouts with Material Design 3
- **Architecture**: MVVM (ViewModel + LiveData)
- **Key Components**:
    - `NotificationManager` & `NotificationChannel`
    - `ActivityResultLauncher` for permissions
    - `SlidingPaneLayout` for adaptive layouts
    - `FragmentContainerView` & Fragment communication

## Folder Structure
```text
Exp5/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/exp5/
│   │   │   ├── MainActivity.kt          (Login & Notification Logic)
│   │   │   ├── DepartmentActivity.kt    (Main Container)
│   │   │   ├── ListFragment.kt          (Department List)
│   │   │   ├── DetailFragment.kt        (Department Details)
│   │   │   └── ...                      (Adapters & ViewModels)
│   │   ├── res/
│   │   │   ├── layout/                  (UI XML files)
│   │   │   └── values/                  (Colors, Strings, Themes)
│   │   └── AndroidManifest.xml          (Permissions & Activity registration)
├── img1.png                             (Output Screenshot 1)
├── img2.png                             (Output Screenshot 2)
└── README.md
```

## Code Snippet: High-Priority Notification
```kotlin
private fun sendNotificationAndProceed() {
    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(getString(R.string.notification_title))
        .setContentText(getString(R.string.notification_content))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(NOTIFICATION_ID, builder.build())

    proceedToDepartmentActivity()
}
```

## Output Screenshots

### 1. Login & Notification
![img1.png](img1.png)
*Figure 1: The automated login screen pre-filling credentials and triggering the high-priority notification.*

### 2. Department Management (Multi-pane)
![img2.png](img2.png)
*Figure 2: The SlidingPaneLayout displaying the department list and detailed information after a successful login.*

---
**Author**: RUKSAR KHATUN | 25MCAR0115
