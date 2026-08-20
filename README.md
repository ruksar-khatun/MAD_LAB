# Experiment 1: Develop a simple "Hello World" Android application

## Overview
This project demonstrates the creation of a basic Android application using Kotlin and XML layouts. It serves as an introductory experiment to understand the Android development environment, project structure, and UI design using `ConstraintLayout`.

## Experiment Description

### Concept and Technology
- **Android SDK**: The primary framework for building Android applications.
- **Kotlin**: The modern, recommended programming language for Android development.
- **XML Layouts**: Used to define the user interface structure declaratively.
- **ConstraintLayout**: A powerful layout manager that enables complex UI designs with a flat view hierarchy, improving performance and flexibility.
- **Activity**: Represents a single screen with a user interface.

### Scenario
In this experiment, we develop a "Hello World" application that goes beyond the default template by including personalized information. The application displays:
1.  A bold **"Hello World!"** message.
2.  The developer's name: **Ruksar Khatun**.
3.  A specific identifier: **25MCAR0115**.

The elements are arranged in a vertical chain, centered on the screen, and styled for clarity using bold text.

## Project Structure

Below is a breakdown of the key files and directories in this project:

```text
Exp1/
├── app/                        # Main application module
│   ├── src/                    # Source files
│   │   ├── main/               # Main source set
│   │   │   ├── java/           # Kotlin/Java source code
│   │   │   │   └── com/example/exp1/
│   │   │   │       └── MainActivity.kt      # Main entry point of the app
│   │   │   ├── res/            # Resources (layouts, strings, images)
│   │   │   │   └── layout/
│   │   │   │       └── activity_main.xml    # UI layout definition
│   │   │   └── AndroidManifest.xml          # App metadata and configuration
│   └── build.gradle.kts        # Module-level build configuration
├── img.png                     # Screenshot of the application output
├── build.gradle.kts            # Project-level build configuration
├── settings.gradle.kts         # Project settings and module inclusion
└── README.md                   # Project documentation
```

## Source Code

### UI Layout (`activity_main.xml`)
The layout uses `ConstraintLayout` to center the text elements and a vertical chain to space them evenly.

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

### Main Activity (`MainActivity.kt`)
The activity class that loads the layout and enables edge-to-edge display.

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

## Output Screenshot
![Application Output](img.png)
