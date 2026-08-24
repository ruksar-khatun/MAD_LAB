<<<<<<< HEAD
# University Department Explorer - Experiment 3

## Overview
This Android application demonstrates the implementation of a flexible and adaptive user interface using **Fragments**. The app provides a master-detail navigation pattern tailored for a university setting, allowing users to browse various departments and view detailed information about each.

## Technology and Concepts
The project leverages several core Android development technologies:
- **Fragments**: Used to create modular and reusable UI components. The app features a `ListFragment` for browsing and a `DetailFragment` for information display.
- **SlidingPaneLayout**: An adaptive UI component that automatically manages dual-pane layouts on large screens (tablets) and single-pane navigation on small screens (phones).
- **ViewModel**: A `DepartmentViewModel` is used to share state between fragments, ensuring data persistence across configuration changes.
- **RecyclerView**: Efficiently displays the list of university departments.
- **Material Design**: Uses Material Card components and a clean color palette for a professional look.

## Core Experiments & Debugging
The application is designed as a laboratory for Android Studio's debugging tools:
1.  **Lifecycle Observation**: By placing a breakpoint in `DetailFragment#onViewCreated`, developers can inspect the Fragment's lifecycle and local variable state during creation.
2.  **Conditional Breakpoints**: A conditional breakpoint on the department selection event allows pausing execution only when a specific item (e.g., "Computer Science & IT") is clicked, demonstrating advanced troubleshooting techniques.

## Project Scenario
The app showcases a **University Management** scenario with the following departments:
- Computer Science & Information Technology
- Business
- Finance
- Law
- Psychology

## Project Structure
```text
Exp3/
├── app/
│   ├── src/main/java/com/example/exp3/
│   │   ├── MainActivity.kt        # Main entry point, handles adaptive UI logic
│   │   ├── ListFragment.kt        # Handles the list of departments
│   │   ├── DetailFragment.kt      # Displays department descriptions
│   │   ├── DepartmentViewModel.kt # Shared data between fragments
│   │   ├── DepartmentAdapter.kt   # Adapter for RecyclerView
│   │   └── Department.kt          # Data model for Department entity
│   └── src/main/res/layout/
│       ├── activity_main.xml      # Root layout with Header and Footer
│       ├── fragment_list.xml      # Layout for the list view
│       ├── fragment_detail.xml    # Layout for the detail view
│       └── item_department.xml    # RecyclerView item design
└── README.md                      # This documentation
```

## Output & Test Cases

### Main Application UI & Identity Verification
The screenshot below shows the primary interface with the centered header "University Department". The footer correctly displays the developer's credentials.
- **Verification**: **Name: Ruksar Khatun | USN: 25MCAR0115**

![Main Output](img.png)

---

### Test Case: Adaptive Layout & Department Selection
- **Scenario**: Running the app and selecting a department.
- **Expected Result**: The app adapts its layout based on screen size. Selecting a department (e.g., "Computer Science & IT") displays its specific details and background color.
- **Screenshot**: ![Adaptive Layout](img_1.png)

---
**Developer**: Ruksar Khatun  
**USN**: 25MCAR0115
=======
# Experiment 1: Develop a simple "Hello World" Android application

## Overview
This project is a foundational Android application designed to introduce the basics of Android development. It demonstrates how to set up a project, design a simple user interface using XML, and handle the basic activity lifecycle in Kotlin. The app displays a "Hello World" message along with specific user details (Name and ID) centered on the screen.

## Project Explanation
### Concept/Technology
- **Android Studio**: The official IDE for Android app development.
- **Kotlin**: The modern, statically typed programming language used for Android development, offering conciseness and safety.
- **XML (Extensible Markup Language)**: Used to define the UI layout of the application.
- **ConstraintLayout**: A flexible layout manager that allows for complex UI designs with a flat view hierarchy, improving performance and responsiveness.

### Scenario
In this experiment, we create a single-activity application. The primary goal is to use `TextView` components within a `ConstraintLayout` to display text information. We use a "packed chain" configuration to ensure all text elements are grouped together and centered vertically and horizontally on the device screen.

## Project Structure
A clear breakdown of the project's folder and file structure:

```text
Exp1/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/exp1/
│   │   │   │   └── MainActivity.kt        # The main logic and entry point of the app
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   └── activity_main.xml  # UI layout definition
│   │   │   │   ├── values/                # App resources like strings and colors
│   │   │   │   └── mipmap/                # App icons
│   │   │   └── AndroidManifest.xml        # Essential information about the app for the Android system
│   └── build.gradle.kts                   # App-level build configuration
├── img.png                                # Screenshot of the running application
├── build.gradle.kts                       # Project-level build configuration
├── settings.gradle.kts                    # Gradle settings for project structure
└── README.md                              # Project documentation
```

## Source Code

### MainActivity.kt
```kotlin
package com.example.exp1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
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
    }
}
```

### activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/tv_hello"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        android:textStyle="bold"
        app:layout_constraintBottom_toTopOf="@+id/tv_name"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_chainStyle="packed" />

    <TextView
        android:id="@+id/tv_name"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Ruksar Khatun"
        android:textStyle="bold"
        android:layout_marginTop="8dp"
        app:layout_constraintBottom_toTopOf="@+id/tv_id"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/tv_hello" />

    <TextView
        android:id="@+id/tv_id"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="25MCAR0115"
        android:textStyle="bold"
        android:layout_marginTop="8dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/tv_name" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Output
Below is the visual representation of the application running on an Android device:

![Application Output](img.png)
>>>>>>> c8609e24304c3219bf2274bc643369e99d486488
