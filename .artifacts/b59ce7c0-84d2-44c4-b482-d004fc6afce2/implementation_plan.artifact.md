# Dynamic Home Screen Data Integration Plan

This plan focuses on replacing all remaining static/dummy data on the Home Screen with real, dynamic data from the backend and local storage (DataStore). Specifically, we will make the "Next Reminder" card and "Featured Routine" logic reflect the user's actual settings and current time.

## Proposed Changes

### 1. Presentation Layer (Android)

#### [MODIFY] [HomeUiState.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/home/HomeUiState.kt)
- Add `nextReminderTitle: Int?` (Resource ID for localized titles like "Sabah Rutini").
- Add `nextReminderTime: String?` (The actual formatted time, e.g., "08:30").

#### [MODIFY] [HomeViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/home/HomeViewModel.kt)
- Inject `GetReminderSettingsUseCase` from `domain.notifications`.
- Implement `calculateNextReminder()`:
    - Observe the reminder settings from DataStore.
    - Compare the current time with the configured Morning/Evening reminder times.
    - Determine which one is "next" and update the state.
- Refine `loadDashboardData()`:
    - Ensure `featuredRoutine` selection is intelligent (showing the routine the user should be doing *now*).

#### [MODIFY] [HomeScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/home/HomeScreen.kt)
- Update `NextReminderCard`:
    - Accept dynamic parameters for title and time.
    - If no reminders are enabled, hide the card or show a "No reminders set" prompt.
- Update `WeeklyProgressSection`:
    - Ensure it handles an empty activity list gracefully (using the new backend data).
- Update `FeaturedRoutineCard`:
    - Fix the "Complete All" button logic or hide it if not yet implemented to avoid confusion.

## Verification Plan

### Automated Tests
- `gradlew assembleDebug` to verify compilation.

### Manual Verification
1. **Reminder Change**: Go to Profile -> Reminders, change the Morning time to 10 minutes in the future. Return Home and verify the `NextReminderCard` shows the new time.
2. **Routine Priority**: Set your system clock to 09:00 AM. Verify the Morning routine is featured. Set it to 09:00 PM. Verify the Evening routine takes priority.
3. **Weekly Progress**: Check a routine item today. Verify the last bar in the "Haftalık İlerleme" chart increases immediately.
4. **Skin Score**: Verify the score reflects the current streak and completion rates calculated by the backend.
