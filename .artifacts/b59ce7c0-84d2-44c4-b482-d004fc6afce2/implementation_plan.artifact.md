# Phase 6: Home Dashboard Implementation Plan

This phase implements the central hub of the DermaRoutine application: the Home Dashboard. We will also establish the main navigation structure with a Bottom Navigation Bar and placeholder screens for upcoming features.

## User Review Required

> [!NOTE]
> **Navigation Structure**: The Bottom Bar will feature Home, Routine, Products, Progress, and Profile. Screens like the AI Assistant and Skin Journal will be accessible via Quick Actions on the Home screen.
>
> **Mock Data**: Since Routine and Streak features are not yet implemented on the backend, the Home screen will show appropriate "Empty States" with Call-to-Action (CTA) buttons as specified.

## Proposed Changes

### 1. Navigation & Routing

#### [MODIFY] [Screen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/Screen.kt)
- Add missing destinations: `Journal`, `Assistant`.
- Add a list of bottom navigation items.

#### [MODIFY] [NavGraph.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/NavGraph.kt)
- Add composable routes for all new screens.
- Implement placeholders for `Routine`, `Products`, `Progress`, `Journal`, and `Assistant`.

#### [MODIFY] [MainActivity.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/MainActivity.kt)
- Wrap the `NavGraph` in a `Scaffold`.
- Implement a `BottomNavigationBar` that only appears on main app screens (Home, Routine, Products, Progress, Profile).
- Update the authenticated start destination to `Screen.Home`.

### 2. Presentation Layer (Home)

#### [NEW] [HomeUiState.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/home/HomeUiState.kt)
- State for greeting, user name, date, and routine summary placeholders.

#### [NEW] [HomeViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/home/HomeViewModel.kt)
- Logic for time-aware greetings ("Good morning", etc.).
- Retrieving display name from the currently authenticated user.
- Managing placeholder states for routines and streaks.

#### [MODIFY] [HomeScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/home/HomeScreen.kt)
- Build the scrollable dashboard UI:
    - **Header**: Time-aware greeting and dynamic date.
    - **Routine Cards**: Morning and Evening summary cards with "Create Routine" CTAs.
    - **Streak Card**: Visual 🔥 icon and current streak status (initially 0).
    - **Skin Photo Card**: CTA for adding today's photo.
    - **Quick Actions**: "Add Product", "Add Photo", "Ask AI" navigation buttons.

### 3. Placeholder Screens
Create basic themed placeholder screens in:
- `presentation/routine/RoutineScreen.kt`
- `presentation/products/ProductsScreen.kt`
- `presentation/progress/ProgressScreen.kt`
- `presentation/journal/JournalScreen.kt`
- `presentation/assistant/AssistantScreen.kt`

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to ensure all new navigation paths and components compile correctly.

### Manual Verification
1. **Authenticated Launch**: Verify the user lands on the Home screen after login.
2. **Greeting & Date**: Verify the greeting matches the current time and the date is correct.
3. **Bottom Navigation**: Test switching between Home, Routine, Products, Progress, and Profile.
4. **Quick Actions**: Verify buttons on the Home screen navigate to the correct placeholder screens.
5. **UI Consistency**: Ensure all cards and buttons follow the Pastel Green / Sage / Cream design system.
6. **No Fake Data**: Verify that empty states are shown instead of hardcoded fake numbers for routines/streaks.
