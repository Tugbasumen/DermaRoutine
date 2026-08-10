# Phase 20: Mandatory Profile Setup Flow Plan

This phase ensures a "Google-First" personalized experience by requiring all new users to complete their Skin Profile before accessing the main dashboard. This aligns with the project vision: "Kullanıcı uygulamaya ilk girdiğinde kısa bir cilt profili oluşturur."

## Proposed Changes

### 1. Navigation & Routing (Android Core)

#### [MODIFY] [MainActivity.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/MainActivity.kt)
- Observe the user's profile status alongside their authentication state.
- Update `startDestination` logic:
    - If `currentUser != null` AND `profile == null` -> **`Screen.Profile`**
    - If `currentUser != null` AND `profile != null` -> **`Screen.Home`**

#### [MODIFY] [NavGraph.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/NavGraph.kt)
- Pass the profile status to the `ProfileScreen` to determine if the back button should be shown.

### 2. Presentation Layer (Android UI)

#### [MODIFY] [ProfileScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/profile/ProfileScreen.kt)
- **Conditional UI**: If it's the user's first time (no profile exists):
    - Hide the Top Bar's back button and the "Reminders" icon.
    - Show a welcoming title: "Cilt Yolculuğuna Başlayalım 🌿".
    - Add a subtitle explaining why the profile is important.
- **Auto-Navigation**: Once the profile is saved successfully for the first time, navigate automatically to the Home screen and clear the navigation stack.

#### [MODIFY] [ProfileViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/profile/ProfileViewModel.kt)
- Add a state or event to notify the UI when the initial setup is complete.

### 3. Backend (Optional Cleanup)

#### [MODIFY] [ProfileController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/ProfileController.cs)
- Ensure the `GET /api/profile` endpoint returns a clean 404 when no profile exists, which the app will interpret as "Setup Required".

## Verification Plan

### Manual Verification
1. **New User Path**:
    - Log in with a new Google account that has no profile.
    - Verify the app lands on the Profile screen immediately after the Auth screen.
    - Try to go back (system back or UI). Verify it's disabled or correctly handled.
2. **Setup Completion**:
    - Fill out the profile and tap "Kaydet".
    - Verify the app navigates to the Home dashboard automatically.
3. **Returning User Path**:
    - Close and reopen the app.
    - Verify the app lands directly on the Home dashboard.
