# Phase 13: Routine Notifications & WorkManager Walkthrough

Phase 13 is completed. The DermaRoutine application now features a reliable background notification system to remind users of their morning and evening skincare routines.

## Changes Made

### 1. Reliable Background Scheduling (WorkManager)
- **WorkManager**: Integrated `androidx.work` to handle battery-efficient, persistent background tasks.
- **RoutineReminderWorker**: A background worker that triggers notifications even if the app is closed or the device is restarted.
- **Unique Work**: Used unique work names (`morning_routine_reminder`, `evening_routine_reminder`) and `ExistingPeriodicWorkPolicy.UPDATE` to ensure no duplicate reminders are scheduled and settings changes are applied immediately.

### 2. User-Controlled Settings
- **Notification Settings Screen**: A new screen in the Profile section allowing users to:
    - Toggle Morning and Evening reminders independently.
    - Set custom reminder times using a Material 3 **TimePicker**.
- **DataStore Persistence**: Used DataStore to persist reminder states and times locally on the device.

### 3. Notification Experience
- **Notification Channel**: Created a dedicated `routine_reminders` channel with appropriate priority.
- **Themed Content**: Short, friendly messages ("Morning skincare routine 🌿", "Evening skincare routine 🌙") that match the brand's wellness tone.
- **Deep Linking**: Configured a `PendingIntent` that launches the app and navigates directly to the Routine screen when the notification is tapped.

### 4. Modern Permission Handling
- **Android 13+ Support**: Implemented runtime requests for the `POST_NOTIFICATIONS` permission using Accompanist Permissions.
- **Contextual Request**: The app only asks for permission when the user attempts to enable a reminder, providing a better user experience.

### 5. Architecture
- **Clean Architecture**: Followed the established pattern with dedicated UseCases (`GetReminderSettingsUseCase`, `UpdateReminderSettingUseCase`) and a `RoutineReminderScheduler` abstraction.
- **Hilt Integration**: Correctly configured `HiltWorker` to allow dependency injection into the background worker.

## Verification Plan

### Automated Tests
- [x] `./gradlew assembleDebug` finished successfully.
- [x] Verified WorkManager scheduling logic with unit tests for time delay calculations.

### Manual Verification
- **Enable/Disable**: Toggled reminders and verified work was enqueued/cancelled using `adb shell dumpsys jobscheduler`.
- **Time Change**: Updated a reminder time and confirmed the initial delay was correctly recalculated.
- **Navigation**: Tapped a simulated notification and verified it opened the correct destination in the app.
- **Persistence**: Verified that settings are remembered after closing and reopening the app.

## Build Status
- [x] Android app builds successfully.
- [x] No regressions in previous features (Login, Profile, Routines, Products).
