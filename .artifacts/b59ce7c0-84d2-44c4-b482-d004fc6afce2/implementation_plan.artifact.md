# Phase 2: Splash Screen & Onboarding Implementation Plan

This phase implements the initial user experience: a modern Splash Screen and a 3-step Onboarding flow. We will also implement simple state persistence to ensure onboarding is only shown once.

## User Review Required

> [!NOTE]
> **Dependencies**: I will add `androidx.core:core-splashscreen` for the official Splash Screen API and `androidx.datastore:datastore-preferences` for persisting the onboarding completion state. These are standard Jetpack libraries.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/kotlinproje/DermaRoutine/gradle/libs.versions.toml)
- Add `core-splashscreen` and `datastore-preferences` libraries.

#### [MODIFY] [build.gradle.kts (app)](file:///C:/kotlinproje/DermaRoutine/app/build.gradle.kts)
- Add the new dependencies.

### Resources & Styling

#### [MODIFY] [themes.xml](file:///C:/kotlinproje/DermaRoutine/app/src/main/res/values/themes.xml)
- Define a Splash Screen theme that inherits from `Theme.SplashScreen`.
- Set the background to `Background` (#F8FBF8) and the brand icon.

### Core Architecture (DataStore)

#### [NEW] [OnboardingDataStore.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/util/OnboardingDataStore.kt)
- A simple utility class using DataStore to save and read the `is_onboarding_completed` flag.

### Presentation Layer

#### [NEW] Onboarding Components
- `OnboardingPage.kt`: Data model for onboarding pages.
- `OnboardingScreen.kt`: The main UI with `HorizontalPager`, indicators, and buttons.
- `OnboardingViewModel.kt`: Manages the current page and interacts with `OnboardingDataStore`.

#### [NEW] [ProfileScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/profile/ProfileScreen.kt)
- A placeholder screen as the destination after onboarding.

### Navigation & Main Entry

#### [MODIFY] [MainActivity.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/MainActivity.kt)
- Initialize the Splash Screen API (`installSplashScreen()`).
- Collect the onboarding state to decide the starting destination.

#### [MODIFY] [NavGraph.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/NavGraph.kt)
- Add `Onboarding` and `Profile` destinations.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify no build errors.

### Manual Verification
- **First Launch**: App should show Splash Screen, then transition to Onboarding.
- **Onboarding Flow**: Navigate through 3 pages, verify "Skip" and "Get Started" buttons.
- **Persistence**: Complete onboarding, close app, and reopen. It should go directly to the Profile placeholder (or Home if decided later).
