# Walkthrough - Autologin and Priority Notification

I have successfully implemented the autologin feature and ensured a high-priority notification is fired upon startup.

## Changes Made

### MainActivity.kt
- **Autologin**: Updated the `onCreate` method to automatically pre-fill the username (`ruksar26`) and password (`Ru@123`) fields.
- **Auto-Trigger**: Added a call to `loginButton.performClick()` immediately after setup, which triggers the login logic, checks for notification permissions, and fires the high-priority notification.
- **Notification**: Maintained the high-priority notification configuration (`PRIORITY_HIGH`) and channel setup.

## Verification Results

### Build Success
- `app:assembleDebug` completed successfully.

### Manual Verification
1. Launch the **Exp5** app.
2. Observe that the login fields are pre-filled.
3. Observe that the app immediately transitions to the notification permission request (or directly to the Department list if permission was already granted).
4. Verify that the **High-Priority Notification** appears in the system tray.
