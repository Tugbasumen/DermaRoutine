# Phase 13: Routine Notifications & WorkManager Implementation Plan

This phase implements local morning and evening routine reminders using Android notifications and WorkManager for background scheduling. Users will be able to customize reminder times and toggle them on/off.

## User Review Required

> [!NOTE]
> **WorkManager Timing**: WorkManager is optimized for battery life and is not an "exact" alarm API. Reminders will be delivered as close to the target time as possible, but might be slightly delayed depending on system conditions (Doze mode, battery saver, etc.).
>
> **Deep Linking**: Tapping a notification will launch the app and navigate directly to the Morning or Evening routine detail screen.

## Proposed Changes

### 1. Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/kotlinproje/DermaRoutine/gradle/libs.versions.toml)
- Add `androidx.work:work-runtime-ktx` version and library.

#### [MODIFY] [build.gradle.kts (app)](file:///C:/kotlinproje/DermaRoutine/app/build.gradle.kts)
- Add `implementation(libs.androidx.work.runtime.ktx)`.

### 2. Notification Infrastructure (Data Layer)

#### [NEW] [RoutineNotificationManager.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/notifications/RoutineNotificationManager.kt)
- Responsible for creating the `routine_reminders` notification channel.
- Building notifications with titles ("Morning skincare routine 🌿", "Evening skincare routine 🌙") and deep-link PendingIntents.

#### [NEW] [RoutineReminderWorker.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/notifications/RoutineReminderWorker.kt)
- The WorkManager `CoroutineWorker` that triggers the notification when executed.

#### [NEW] [RoutineReminderScheduler.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/notifications/RoutineReminderScheduler.kt)
- Handles scheduling and cancelling work.
- Uses `PeriodicWorkRequestBuilder` with a 24-hour interval and an initial delay calculated to hit the user's selected time.

#### [NEW] [RoutineReminderPreferences.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/util/RoutineReminderPreferences.kt)
- DataStore implementation to persist:
    - `morningEnabled`: Boolean
    - `morningTime`: String (HH:mm)
    - `eveningEnabled`: Boolean
    - `eveningTime`: String (HH:mm)

### 3. Domain Layer

#### [NEW] UseCases
- `GetReminderSettingsUseCase.kt`: Reads settings from DataStore.
- `UpdateReminderSettingUseCase.kt`: Saves setting and triggers (re)scheduling via the Scheduler.

### 4. Presentation Layer (UI)

#### [NEW] [RoutineReminderSettingsScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/notifications/RoutineReminderSettingsScreen.kt)
- UI for toggling reminders and selecting times.
- Integration with Material 3 `TimePicker`.
- Requesting `POST_NOTIFICATIONS` permission when a reminder is enabled.

#### [NEW] [RoutineReminderViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/notifications/RoutineReminderViewModel.kt)
- Manages the UI state and coordinates between UseCases and UI.

### 5. Integration

#### [MODIFY] [NavGraph.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/NavGraph.kt)
- Add the reminder settings route.
- Configure deep link handling for `Screen.RoutineDetail`.

#### [MODIFY] [MainActivity.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/MainActivity.kt)
- Ensure the app handles incoming intents from notifications correctly to navigate to the right screen.

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to verify dependency integration.
- Unit tests for time delay calculation logic (ensuring the initial delay correctly targets the next occurrence).

### Manual Verification
1. **Enable Reminder**: Toggle "Morning Reminder" ON, grant permission, and set time to 1 minute in the future.
2. **Delivery**: Verify the notification appears (might take a few minutes due to WorkManager nature).
3. **Deep Link**: Tap the notification while the app is closed and verify it opens the Morning Routine detail.
4. **Disable**: Toggle OFF and verify the WorkManager job is cancelled (`adb shell dumpsys jobscheduler`).
5. **Persistence**: Change the time, restart the app, and verify the new time is saved.
