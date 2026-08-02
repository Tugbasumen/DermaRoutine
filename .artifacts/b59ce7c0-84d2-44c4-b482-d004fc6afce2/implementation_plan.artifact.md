# Phase 14: Crashlytics, Error Handling & App Stabilization Plan

This phase focuses on making DermaRoutine production-ready by integrating Firebase Crashlytics, implementing centralized error handling on both Android and Backend, and stabilizing the overall user experience.

## User Review Required

> [!NOTE]
> **Crashlytics Verification**: I will add a temporary, hidden way to trigger a test crash in Debug builds to verify that Crashlytics is working.
>
> **Global Result Pattern**: I will refactor the Android repository layers to use a consistent `AppResult` and `AppError` pattern, which might require small changes in ViewModels to handle the new result types.

## Proposed Changes

### 1. Build Configuration (Android)

#### [MODIFY] [libs.versions.toml](file:///C:/kotlinproje/DermaRoutine/gradle/libs.versions.toml)
- Add `firebase-crashlytics` library and `firebase-crashlytics-gradle` plugin.

#### [MODIFY] [build.gradle.kts (root)](file:///C:/kotlinproje/DermaRoutine/build.gradle.kts)
- Add the Crashlytics plugin to the plugins block (apply false).

#### [MODIFY] [build.gradle.kts (app)](file:///C:/kotlinproje/DermaRoutine/app/build.gradle.kts)
- Apply `com.google.firebase.crashlytics` plugin.
- Add `implementation(libs.firebase.crashlytics)` dependency.

### 2. Android Core: Error Handling

#### [NEW] [AppError.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/error/AppError.kt)
- Sealed class defining standard errors: `Network`, `Unauthorized`, `Server`, `NotFound`, `Validation`, `Unknown`.

#### [NEW] [ErrorMapper.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/error/ErrorMapper.kt)
- Helper to map HTTP codes and Exceptions to `AppError`.

#### [NEW] [ModifierExt.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/util/ModifierExt.kt)
- Extension for `Modifier.clickable` to add a debounce period, preventing duplicate button taps.

### 3. Backend: Global Exception Handling

#### [NEW] [ErrorResponse.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/DTOs/ErrorResponse.cs)
- Standardized error DTO: `Code`, `Message`, `TraceId`.

#### [NEW] [ExceptionHandlingMiddleware.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Middleware/ExceptionHandlingMiddleware.cs)
- Middleware to catch all unhandled exceptions, log them with a Trace ID, and return a safe `ErrorResponse`.

#### [MODIFY] [Program.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Program.cs)
- Register the `ExceptionHandlingMiddleware`.

### 4. Stabilization & UX Review

#### [MODIFY] UI Components
- Update `PrimaryButton` and `SecondaryButton` in `Components.kt` to use debounced click logic.
- Review `ProfileScreen`, `RoutineDetailScreen`, and `HomeScreen` to ensure all `Error` states show a "Try Again" button.

#### [MODIFY] Network Layer
- Update `AuthInterceptor.kt` to log custom Crashlytics keys (e.g., current screen) when a network failure occurs.

## Verification Plan

### Automated Tests
- `dotnet build` & `gradlew assembleDebug`.
- Verify the API returns the correct JSON error structure when a manual exception is thrown.

### Manual Verification
1. **Crashlytics**: Trigger a test crash in Debug and verify it appears in the Firebase Console.
2. **Offline Mode**: Disable internet and verify the app shows a friendly "No internet" message with a retry button instead of a technical error.
3. **Double Taps**: Rapidly tap "Save Profile" and verify only one network request is sent.
4. **401 Handling**: Manually invalidate a token and verify the app handles the unauthorized state gracefully.
5. **Backend Security**: Attempt to access a resource without a token and verify the `ErrorResponse` structure.
