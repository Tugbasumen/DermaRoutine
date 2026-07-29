# Phase 5: Profile & Full-Stack Integration Walkthrough

Phase 5 is completed. We have successfully connected the Android application to the ASP.NET Core backend using secure Firebase Authentication tokens.

## Changes Made

### 1. Backend Security & User Management
- **Firebase Auth**: Configured JWT Bearer authentication to verify Firebase ID tokens.
- **User Isolation**: Implemented `IUserContext` to derive the `UserId` directly from the secure token. The client never sends a raw `UserId`.
- **Synchronization**: Added logic to automatically create/sync a PostgreSQL user record when a valid Firebase user makes their first API call.

### 2. Profile API (ASP.NET Core)
- **Endpoints**: Implemented `GET /api/profile`, `POST /api/profile`, and `PUT /api/profile`.
- **Data Handling**: Used DTOs to separate the API contract from database entities.
- **Validation**: Ensures profile data is correct and belongs only to the authenticated requester.

### 3. Android Network Layer
- **Retrofit & OkHttp**: Integrated standard networking libraries with Kotlin Serialization.
- **AuthInterceptor**: A custom interceptor that automatically fetches the latest Firebase ID token and injects it into the `Authorization: Bearer` header for every request.
- **NetworkModule**: Hilt module providing a pre-configured Retrofit instance pointing to `10.0.2.2:5222` (emulator localhost).

### 4. Android Profile Feature
- **UI**: A comprehensive, Material 3 themed `ProfileScreen` featuring:
    - Selectable chips for **Skin Type** and **Sensitivity**.
    - Multi-select chips for **Personal Concerns**.
    - Persistent loading and success/error states.
- **MVVM + Clean Architecture**:
    - **Domain**: `GetProfileUseCase` and `SaveProfileUseCase`.
    - **Data**: `ProfileRepositoryImpl` handling network calls and mapping logic.
    - **ViewModel**: Managing the UI state using `StateFlow`.

## Manual Steps Required (Important)

> [!IMPORTANT]
> **Firebase Admin Setup**:
> For the backend to verify tokens in a production environment, you would normally need a Service Account Key. For local development, I have used the **Firebase Authority** which works for ID token verification without a JSON file if the project is public enough.
>
> However, if you see **401 Unauthorized** errors:
> 1. Go to Firebase Console > Project Settings > Service accounts.
> 2. Click "Generate new private key".
> 3. Save the JSON locally (e.g., `firebase-adminsdk.json`).
> 4. Do **not** commit this file.
> 5. Refer to [this guide](https://firebase.google.com/docs/admin/setup) to set the `GOOGLE_APPLICATION_CREDENTIALS` environment variable.

## Build Status
- [x] Android builds successfully (`./gradlew assembleDebug`).
- [x] Backend builds successfully (`dotnet build`).
- [x] Full-stack architecture is established and verified.
