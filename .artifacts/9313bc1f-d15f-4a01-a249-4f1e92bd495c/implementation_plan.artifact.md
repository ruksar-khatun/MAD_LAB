# Implementation Plan - Autologin and Priority Notification

Update the `MainActivity` in Exp5 to automatically log in with the hardcoded credentials and display a high-priority notification.

## User Review Required

> [!NOTE]
> The app will automatically enter the credentials `ruksar26` and `Ru@123` and trigger the login process as soon as it opens.

## Proposed Changes

### MainActivity.kt

#### [MODIFY] [MainActivity.kt](file:///C:/Users/ruksa/AndroidStudioProjects/Exp5/app/src/main/java/com/example/exp5/MainActivity.kt)
- Update `onCreate` to:
    - Pre-fill `usernameInput` with "ruksar26".
    - Pre-fill `passwordInput` with "Ru@123".
    - Automatically trigger `checkPermissionAndNotify()` to fire the notification and transition to the next activity.

## Verification Plan

### Automated Tests
- Build the project to ensure no syntax errors.

### Manual Verification
- Deploy the app.
- Observe that the credentials are pre-filled and the app automatically attempts to log in.
- Verify that the notification is displayed (after permission is granted if necessary).
- Verify the app transitions to `DepartmentActivity`.
