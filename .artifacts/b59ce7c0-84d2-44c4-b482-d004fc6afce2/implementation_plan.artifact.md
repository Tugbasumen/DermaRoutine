# Phase 5: Profile & Full-Stack Integration Plan

This phase connects the Android app to the ASP.NET Core backend to manage the User Profile. We will implement secure authentication, user synchronization, and a full-featured Profile UI.

## User Review Required

> [!IMPORTANT]
> **Firebase Admin SDK Setup**:
> 1. You need to generate a **Service Account Key** (JSON) from the Firebase Console (**Project Settings > Service accounts**).
> 2. **DO NOT** share this file or commit it to Git.
> 3. For local development, I will show you how to point to this file using an environment variable or .NET User Secrets.
>
> **Backend URL**: In development, Android needs to connect to the backend. If using an emulator, `http://10.0.2.2:5222` is typically used. For physical devices, you'll need your local IP.

## Proposed Changes

### 1. Backend (DermaRoutine.Api)

#### [MODIFY] NuGet Packages
- Add `FirebaseAdmin` for token verification.
- Add `Microsoft.AspNetCore.Authentication.JwtBearer` for standard authentication middleware.

#### [NEW] [FirebaseAuthenticationHandler](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Middleware/FirebaseAuthenticationHandler.cs)
- A custom authentication handler or standard JWT config to verify Firebase tokens.

#### [NEW] [IUserContext](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Services/IUserContext.cs)
- A service to get the current user's UID and database ID from the security context.

#### [MODIFY] [ProfileController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/ProfileController.cs)
- `GET /api/profile`: Returns `SkinProfileDto`.
- `POST /api/profile`: Creates a new profile.
- `PUT /api/profile`: Updates existing profile.
- **Security**: All endpoints will use `[Authorize]` and derive the `userId` from the token.

### 2. Android (app)

#### [MODIFY] [libs.versions.toml](file:///C:/kotlinproje/DermaRoutine/gradle/libs.versions.toml)
- Add versions for `Retrofit`, `OkHttp`, and `Kotlin Serialization Converter`.

#### [NEW] [AuthInterceptor.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/network/AuthInterceptor.kt)
- Fetches the Firebase ID Token using `user.getIdToken(forceRefresh = false).await()` and adds it to the headers.

#### [NEW] [ProfileApiService.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/remote/ProfileApiService.kt)
- Retrofit interface for profile endpoints.

#### [NEW] Domain Layer
- `SkinProfile` (Domain Model).
- `ProfileRepository` (Interface).
- `GetProfileUseCase`, `SaveProfileUseCase`.

#### [NEW] [ProfileScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/profile/ProfileScreen.kt)
- Comprehensive UI with:
    - Selection for **Skin Type** (Dry, Oily, etc.).
    - Selection for **Sensitivity** (Low, Medium, High).
    - Multi-selection for **Concerns** (Redness, Dullness, etc.).
    - Notes text field.

## Verification Plan

### Automated Tests
- `dotnet build` and `gradlew assembleDebug`.
- Verify user isolation: Attempt to access `/api/profile` with an invalid token (should return 401).

### Manual Verification
1. Register/Login on Android.
2. Navigate to Profile.
3. Fill out the profile and click "Save".
4. Check PostgreSQL to verify the user and profile records are created.
5. Restart the app and verify the profile loads correctly.
