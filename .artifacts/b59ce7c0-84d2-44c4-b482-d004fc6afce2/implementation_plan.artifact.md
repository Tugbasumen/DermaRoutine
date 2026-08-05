# Phase 15: Skin Journal Implementation - Part 2: Android Domain & Data

This part focuses on establishing the Android data layer for the Skin Journal, linking the app to the recently created backend API.

## Proposed Changes

### 1. Domain Layer

#### [NEW] [JournalEntry.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/domain/model/JournalEntry.kt)
- Domain model for journal records: `id`, `content`, `mood`, `createdAt`.

#### [NEW] [JournalRepository.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/domain/repository/JournalRepository.kt)
- Interface with methods: `getJournalEntries()`, `addJournalEntry(content, mood)`, `deleteJournalEntry(id)`.

#### [NEW] UseCases
- `GetJournalEntriesUseCase.kt`
- `AddJournalEntryUseCase.kt`
- `DeleteJournalEntryUseCase.kt`

### 2. Data Layer

#### [NEW] [JournalDtos.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/remote/dto/JournalDtos.kt)
- Retrofit request/response DTOs corresponding to backend models.

#### [NEW] [JournalApiService.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/remote/JournalApiService.kt)
- Retrofit interface for `/api/journal` endpoints.

#### [NEW] [JournalMapper.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/mapper/JournalMapper.kt)
- Extension functions to convert DTOs to Domain models.

#### [NEW] [JournalRepositoryImpl.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/repository/JournalRepositoryImpl.kt)
- Concrete implementation using `JournalApiService`.

### 3. Dependency Injection

#### [NEW] [JournalModule.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/di/JournalModule.kt)
- Hilt module to provide the API service and Repository.

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to ensure compilation.

### Manual Verification
- This layer will be verified in Part 3 once the UI is connected.
