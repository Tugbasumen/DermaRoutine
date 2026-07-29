# Phase 2: Splash Screen & Onboarding Walkthrough

Phase 2 is completed. The app now features a modern entry flow with a splash screen and a 3-step onboarding process, with persistent state management.

## Changes Made

### 1. Splash Screen
- **Splash API**: Integrated `androidx.core:core-splashscreen` for a native launch experience.
- **Visuals**: Created `ic_splash_logo.xml` and configured a custom `Theme.App.Starting` in `styles.xml` with the pastel green background (#F8FBF8).
- **MainActivity**: Implemented `installSplashScreen()` and used `setKeepOnScreenCondition` to hold the splash screen until the onboarding state is loaded from DataStore.

### 2. Onboarding Flow
- **UI Components**:
    - `OnboardingScreen`: Uses `HorizontalPager` for smooth page transitions.
    - `OnboardingViewModel`: Handles completion logic.
    - `PrimaryButton` & `SecondaryButton`: Reusable styled buttons matching the design system.
- **Pages**: 3-step guide covering "Build Your Routine", "Track Your Skin Journey", and "Understand Your Products".
- **Navigation**:
    - Support for "Skip" (goes straight to Profile).
    - Support for "Continue" / "Get Started" buttons.

### 3. State Persistence
- **DataStore Preferences**: Implemented `OnboardingDataStore` to persist the `is_onboarding_completed` flag.
- **Smart Routing**: On app launch, `MainActivity` checks the DataStore. If onboarding is already completed, the user is navigated directly to the Profile placeholder; otherwise, the Onboarding screen is shown.

### 4. Navigation Updates
- Updated `NavGraph` and `Screen` sealed class to include `Onboarding` and `Profile` destinations.
- Created `ProfileScreen` as a temporary placeholder for the next phase.

## Dependencies Added
- `androidx.core:core-splashscreen`: Official API for splash screens.
- `androidx.datastore:datastore-preferences`: Modern, asynchronous way to store simple UI states.

## Build Status
- [x] Project builds successfully (`./gradlew assembleDebug`).
- [x] Onboarding state correctly persists across app restarts.
