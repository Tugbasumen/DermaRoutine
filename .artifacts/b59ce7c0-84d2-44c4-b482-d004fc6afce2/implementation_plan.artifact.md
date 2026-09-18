# Phase 22: Offline Support with Room Persistence

This phase aligns the application with the "Kullanılacak Teknolojiler" list by implementing **Room Database**. We will move towards an "Offline-First" architecture, allowing users to view their routines and products even without an active internet connection.

## User Review Required

> [!IMPORTANT]
> **Single Source of Truth**: I will refactor the Repositories to use Room as the primary data source. The app will fetch from the API and update the local database, while the UI always observes the local database.

## Proposed Changes

### 1. Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/kotlinproje/DermaRoutine/gradle/libs.versions.toml)
- Add Room version and library definitions.

#### [MODIFY] [build.gradle.kts (app)](file:///C:/kotlinproje/DermaRoutine/app/build.gradle.kts)
- Add Room dependencies (`room-runtime`, `room-ktx`, `room-compiler`).

### 2. Local Data Layer (Room)

#### [NEW] Entities
- `ProductEntity.kt`: Local representation of skincare products.
- `RoutineEntity.kt`: Local representation of routines.
- `RoutineItemEntity.kt`: Local representation of products within a routine.

#### [NEW] DAOs
- `ProductDao.kt`
- `RoutineDao.kt`

#### [NEW] [DermaRoutineDatabase.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/local/DermaRoutineDatabase.kt)
- The main Room database class.

### 3. Repository Refactoring

#### [MODIFY] [ProductRepositoryImpl.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/repository/ProductRepositoryImpl.kt)
- Update to save fetched products into Room and provide a Flow of products from the local DB.

#### [MODIFY] [RoutineRepositoryImpl.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/repository/RoutineRepositoryImpl.kt)
- Implement caching for routines and their completion status.

### 4. UI/UX Polish

#### [MODIFY] State Management
- Ensure ViewModels use `collectAsStateWithLifecycle()` to observe the Flow from Room, providing instant UI updates even when offline.

## Verification Plan

### Automated Tests
- `gradlew assembleDebug` to verify Room annotation processing.

### Manual Verification
1. **Offline Mode**: Turn off Wi-Fi/Mobile Data. Open the app. Verify that your products and routines are still visible.
2. **Syncing**: Turn Wi-Fi back on. Add a product. Verify it is sent to the API and then updated in the local database.
3. **Data Integrity**: Verify that checklist states (Morning/Evening) are persisted correctly across app restarts while offline.
