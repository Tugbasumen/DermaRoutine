# Phase 12: Skin Photo Journal & Progress Tracking Implementation Plan

This phase implements a comprehensive skin progress tracking system. Users can take or select photos, add assessments, and view their journey over time, including side-by-side comparisons and basic statistics.

## User Review Required

> [!IMPORTANT]
> **Firebase Storage Rules**: You MUST configure your Firebase Storage rules to ensure privacy. I will provide the rules that allow only the authenticated owner to access their `skin-photos/{uid}/*` folder.
>
> **Image Quality**: Photos will be compressed on the device before upload to save storage space and bandwidth while maintaining enough detail for progress comparison.

## Proposed Changes

### 1. Build Configuration & Setup

#### [MODIFY] [libs.versions.toml](file:///C:/kotlinproje/DermaRoutine/gradle/libs.versions.toml)
- Add `firebase-storage-ktx` for image storage.
- Add `coil-compose` for efficient image loading and caching.

#### [MODIFY] [build.gradle.kts (app)](file:///C:/kotlinproje/DermaRoutine/app/build.gradle.kts)
- Add the new dependencies.

### 2. Backend (DermaRoutine.Api)

#### [NEW] [SkinPhotoDtos.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/DTOs/SkinPhotoDtos.cs)
- `SkinPhotoDto`, `CreateSkinPhotoRequest`, `UpdateSkinPhotoRequest`.

#### [NEW] [SkinPhotoController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/SkinPhotoController.cs)
- `GET /api/skin-photos`: List history sorted by date (newest first).
- `GET /api/skin-photos/{id}`: Detailed metadata.
- `POST /api/skin-photos`: Save metadata after successful Firebase upload.
- `PUT /api/skin-photos/{id}`: Update notes or assessment.
- `DELETE /api/skin-photos/{id}`: Remove metadata (Android will handle Storage deletion).

### 3. Android Data & Domain Layer

#### [NEW] [SkinPhotoStorage.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/storage/SkinPhotoStorage.kt)
- Wrapper for Firebase Storage to handle `uploadPhoto` and `deletePhoto`.

#### [NEW] Domain Layer
- `SkinPhoto` domain model.
- `SkinPhotoRepository` interface and implementation.
- Use Cases: `GetSkinPhotosUseCase`, `AddSkinPhotoUseCase`, `DeleteSkinPhotoUseCase`, etc.

### 4. Presentation Layer (UI)

#### [NEW] [ProgressScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/progress/ProgressScreen.kt)
- Dashboard showing:
    - Current tracking stats (total photos, days tracking).
    - A chronological list of photo thumbnails with dates and ratings.
    - Navigation to "Add Photo" and "Compare".

#### [NEW] [AddSkinPhotoScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/progress/AddSkinPhotoScreen.kt)
- Integration with **CameraX** and **Photo Picker**.
- Image preview with a form for:
    - **Date & Time** (Default to now).
    - **Skin Assessment** (Rating 1-5 or descriptive chips).
    - **Personal Note**.

#### [NEW] [SkinPhotoDetailScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/progress/SkinPhotoDetailScreen.kt)
- Full-screen image viewer with metadata display and delete action.

#### [NEW] [ComparePhotosScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/progress/ComparePhotosScreen.kt)
- UI to select two photos from history and view them side-by-side.

### 5. Navigation

#### [MODIFY] [Screen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/Screen.kt)
- Add `AddSkinPhoto`, `SkinPhotoDetail`, and `ComparePhotos` routes.

#### [MODIFY] [NavGraph.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/NavGraph.kt)
- Integrate all new progress-related routes.

## Verification Plan

### Automated Tests
- `dotnet build` & `gradlew assembleDebug`.
- Backend tests for user isolation on photo metadata.

### Manual Verification
1. **Take Photo**: Capture a skin photo via camera, fill out the form, and save.
2. **Gallery**: Select an image from the gallery and verify it uploads correctly.
3. **History**: Verify the new photo appears at the top of the `ProgressScreen` list.
4. **Compare**: Select two photos from different dates and verify the side-by-side view works.
5. **Security**: Log in with User A and ensure you cannot see or delete User B's photos by manually testing API endpoints.
