# Experiment 4: Linking Activities using Intents & Responsive List-Detail UI

## Overview
This project demonstrates the implementation of an Android application that links multiple activities using explicit Intents. It features a secure login system that transitions into a responsive "University Department Explorer." The explorer utilizes a List-Detail pattern to provide a seamless user experience across different device form factors.

## Core Experiments
1.  **Activity Navigation**: Using explicit Intents to navigate from a Login screen to a main content area.
2.  **Credential Verification**: Implementing logic to validate user input (Username: `ruksar26`, Password: `Ru@123`) before granting access.
3.  **Responsive UI (List-Detail)**: Using `SlidingPaneLayout` to show a side-by-side view on large screens and a sliding overlay on smaller devices.
4.  **Fragment Communication**: Using a shared `ViewModel` to synchronize state between the `ListFragment` and `DetailFragment`.
5.  **Custom Theming**: Implementing a cohesive "Olive & Beige" color palette with center-aligned UI elements.

## Concepts and Technologies
- **Kotlin**: Primary programming language.
- **Explicit Intents**: For navigating between `MainActivity` and `DepartmentActivity`.
- **Fragments**: Modular UI components for the list and detail views.
- **SlidingPaneLayout**: For adaptive two-pane layouts.
- **ViewModel & LiveData**: For lifecycle-aware state management and fragment communication.
- **Material Design 3**: For modern UI components and theming.

## Key Code Files
- [MainActivity.kt](app/src/main/java/com/example/exp4/MainActivity.kt): Handles login logic and Intent navigation.
- [DepartmentActivity.kt](app/src/main/java/com/example/exp4/DepartmentActivity.kt): Hosts the fragments and handles adaptive layout back-navigation.
- [ListFragment.kt](app/src/main/java/com/example/exp4/ListFragment.kt): Displays the list of university departments.
- [DetailFragment.kt](app/src/main/java/com/example/exp4/DetailFragment.kt): Displays detailed information about a selected department.
- [DepartmentViewModel.kt](app/src/main/java/com/example/exp4/DepartmentViewModel.kt): Manages the shared state between fragments.

## Output & Working Demo
The application follows an elegant Olive Green and Beige Cream theme. All elements are center-aligned to provide a balanced and professional aesthetic.

### Working Recording
You can view the full working demonstration of the app, including the login verification and the responsive department explorer, in the video below:

![Working Demo](Exp4.mp4)

---
**Developer**: Ruksar Khatun
**USN**: 25MCAR0115
