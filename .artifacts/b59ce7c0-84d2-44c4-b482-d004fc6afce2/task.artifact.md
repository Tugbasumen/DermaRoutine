# Phase: Google-Only Authentication Simplification - Task List

- [x] Domain & Data Layer Cleanup
    - [x] Modify `AuthRepository.kt` (Remove email/password methods)
    - [x] Modify `FirebaseAuthRepository.kt` (Remove email/password logic)
    - [x] Delete `LoginUseCase.kt` and `RegisterUseCase.kt`
- [x] Implement Unified Presentation Layer
    - [x] Create `AuthViewModel.kt` (Consolidated logic)
    - [x] Create `AuthScreen.kt` (Modern Google-only UI)
    - [x] Delete `LoginScreen.kt`, `RegisterScreen.kt`, `LoginViewModel.kt`, `RegisterViewModel.kt`
- [x] Navigation & Integration
    - [x] Update `Screen.kt` (Replace Login/Register with Auth)
    - [x] Update `NavGraph.kt` (Simplified routing)
    - [x] Update `MainActivity.kt` (Starting destination logic)
- [x] Build and Verify
    - [x] Successful `gradlew assembleDebug`
    - [x] Verify single-tap Google login/registration flow
