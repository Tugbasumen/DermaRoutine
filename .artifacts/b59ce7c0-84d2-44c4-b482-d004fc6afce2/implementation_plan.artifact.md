# Google-Only Simplified Authentication Plan

This plan refactors the entire authentication flow to use Google Sign-In as the **exclusive** method for accessing the application. This simplifies the UX and ensures a secure, single-tap entry for all users.

## User Review Required

> [!CAUTION]
> **Data Access**: If you have already created test users via Email/Password, they will no longer be able to log in using those credentials. They must use their Google account. If the emails match, Firebase can automatically link them, but for this phase, we are moving to a pure Google flow.
>
> **Cleanup**: I will be deleting `LoginScreen.kt`, `RegisterScreen.kt`, and their respective ViewModels (consolidating into a single `AuthViewModel`).

## Proposed Changes

### 1. Domain & Data Layer (Android)

#### [MODIFY] [AuthRepository.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/domain/repository/AuthRepository.kt)
- Remove `login(email, password)` and `register(email, password)`.
- Keep `signInWithGoogle` and `logout`.

#### [MODIFY] [FirebaseAuthRepository.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/repository/FirebaseAuthRepository.kt)
- Remove email/password implementations.
- Retain and verify the `signInWithGoogle` logic.

#### [DELETE] Email UseCases
- Delete `LoginUseCase.kt` and `RegisterUseCase.kt`.

### 2. Presentation Layer (Android)

#### [NEW] [AuthScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/auth/AuthScreen.kt)
- A clean, wellness-themed entry screen.
- Large DermaRoutine logo/icon.
- A single, prominent "Continue with Google" button.
- Educational subtitle: "Your skincare journey starts with a single tap."

#### [NEW] [AuthViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/auth/AuthViewModel.kt)
- Consolidate Google sign-in and state management.

#### [DELETE] Old Auth Files
- `LoginScreen.kt`, `RegisterScreen.kt`, `LoginViewModel.kt`, `RegisterViewModel.kt`.

### 3. Navigation & Main Entry

#### [MODIFY] [NavGraph.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/NavGraph.kt)
- Update routes to use `Screen.Auth` instead of separate login/register screens.

#### [MODIFY] [MainActivity.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/MainActivity.kt)
- Ensure the initial routing logic correctly points to the new `Auth` screen if the user is not logged in.

### 4. Backend (DermaRoutine.Api)

- No changes required. The backend already validates Firebase tokens independently of the provider (it just sees a valid UID).

## Verification Plan

### Automated Tests
- `gradlew assembleDebug` to ensure no broken references to deleted files.

### Manual Verification
1. **New User**: Tap "Continue with Google". Verify the account is created in the database and you land on the Home/Profile screen.
2. **Returning User**: Tap "Continue with Google". Verify you land on the Home screen immediately.
3. **Logout**: Verify that logging out returns you to the simplified Google-only screen.
