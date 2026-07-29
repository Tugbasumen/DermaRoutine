# Phase 3 Extension: Google Sign-In Implementation Plan

This plan extends the current Authentication phase to include Google Sign-In using the modern Android Credential Manager API.

## User Review Required

> [!IMPORTANT]
> **Firebase Configuration**:
> 1. You MUST enable **Google** as a sign-in provider in the Firebase Console (**Build > Authentication > Sign-in method**).
> 2. You MUST add your **SHA-1 fingerprint** to your Android app settings in the Firebase Console.
> 3. After enabling Google Sign-In, you will get a **Web Client ID**. Please provide this ID to me or update it in `res/values/strings.xml` as `default_web_client_id`.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/kotlinproje/DermaRoutine/gradle/libs.versions.toml)
- Add dependencies for Credential Manager:
    - `androidx.credentials:credentials`
    - `androidx.credentials:credentials-play-services-auth`
    - `com.google.android.libraries.identity.googleid:googleid`

#### [MODIFY] [build.gradle.kts (app)](file:///C:/kotlinproje/DermaRoutine/app/build.gradle.kts)
- Add the new dependencies.

### Domain Layer

#### [MODIFY] [AuthRepository.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/domain/repository/AuthRepository.kt)
- Add `suspend fun signInWithGoogle(idToken: String): Result<User>`.

#### [NEW] [SignInWithGoogleUseCase.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/domain/usecase/SignInWithGoogleUseCase.kt)
- A new use case to handle the Google authentication logic.

### Data Layer

#### [MODIFY] [FirebaseAuthRepository.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/repository/FirebaseAuthRepository.kt)
- Implement `signInWithGoogle` using `AuthCredential` from the Google ID token.

### Presentation Layer

#### [MODIFY] [LoginViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/auth/LoginViewModel.kt)
- Add `signInWithGoogle(idToken: String)` to handle the result from Credential Manager.

#### [MODIFY] [LoginScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/auth/LoginScreen.kt)
- Integrate Android `CredentialManager` to launch the Google Sign-In selector.
- Add a "Sign in with Google" button using the Design System.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to ensure all dependencies and code are correctly integrated.

### Manual Verification
- **Google Sign-In Flow**: Click the Google button, select an account, and verify successful login and navigation to the Profile placeholder.
- **Error Handling**: Verify behavior when sign-in is cancelled or fails.
