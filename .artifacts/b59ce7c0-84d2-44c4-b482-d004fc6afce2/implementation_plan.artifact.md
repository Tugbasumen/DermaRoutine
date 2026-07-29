# Phase 1: Foundation Implementation Plan

This phase focuses on setting up the core architecture, design system, and navigation foundation for the DermaRoutine application. We will establish a Clean Architecture structure and integrate essential libraries like Hilt and Navigation Compose.

## User Review Required

> [!IMPORTANT]
> **Package Renaming**: The project currently uses `com.tugba.dermaroutine`. To match your specification exactly (`app/src/main/java/com/dermaroutine/`), I will rename the package to `com.dermaroutine`. This involves updating `AndroidManifest.xml`, `build.gradle.kts`, and all existing source files.
>
> **Monorepo Structure**: Your spec mentions a `DermaRoutine/android` folder. Currently, the Android project is at the root `C:/kotlinproje/DermaRoutine`. For Phase 1, I will work within the current root to ensure stability in the IDE. Reorganizing into an `android/` subfolder can be done later if desired, but might require IDE reconfiguration.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/kotlinproje/DermaRoutine/gradle/libs.versions.toml)
- Add versions and libraries for:
    - Hilt (Dependency Injection)
    - Navigation Compose
    - Lifecycle ViewModel Compose
    - Material Icons Extended

#### [MODIFY] [build.gradle.kts (root)](file:///C:/kotlinproje/DermaRoutine/build.gradle.kts)
- Add Hilt plugin.

#### [MODIFY] [build.gradle.kts (app)](file:///C:/kotlinproje/DermaRoutine/app/build.gradle.kts)
- Apply Hilt and Kotlin Serialization plugins.
- Update `namespace` and `applicationId` to `com.dermaroutine`.
- Add dependencies for Hilt, Navigation, and Lifecycle.

### Architecture Setup

#### [NEW] Directory Structure
Create the following packages under `com.dermaroutine`:
- `core/` (common, designsystem, navigation, network, util)
- `data/` (local, remote, repository, mapper)
- `domain/` (model, repository, usecase)
- `presentation/` (onboarding, profile, home, routine, etc.)

#### [NEW] [DermaRoutineApp.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/DermaRoutineApp.kt)
- Base Application class with `@HiltAndroidApp`.

### Design System

#### [NEW] [Color.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/designsystem/Color.kt)
- Define the pastel green, sage green, and cream palette.

#### [NEW] [Theme.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/designsystem/Theme.kt)
- Set up Material 3 Theme using the custom color palette.

#### [NEW] [Type.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/designsystem/Type.kt)
- Define typography for headings, body, and captions.

### Navigation

#### [NEW] [Screen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/Screen.kt)
- Sealed class defining all major app destinations.

#### [NEW] [NavGraph.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/NavGraph.kt)
- Initial Navigation setup.

### UI Components

#### [NEW] Reusable Components
- `PrimaryButton.kt`, `AppTopBar.kt`, `AppCard.kt`.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify the project builds with the new dependencies and package structure.

### Manual Verification
- Check that the application starts and displays a themed initial screen.
- Verify that Hilt is correctly initialized via the Application class.
