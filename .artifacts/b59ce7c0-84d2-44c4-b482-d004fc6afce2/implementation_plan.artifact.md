# Phase 7: Routine Management, Completion & Streak Implementation Plan

This phase implements the core skincare routine system, allowing users to manage daily habits, track completion, and maintain a consistency streak.

## User Review Required

> [!NOTE]
> **Streak Logic**: A day is considered "completed" if the user has completed **all active routines** (Morning and Evening) for that specific day. The streak represents the number of consecutive completed days.
>
> **Product Dependency**: Since the full Products feature isn't implemented yet, I will add a simple "Quick Add" product modal or a basic internal product list so you can test adding items to your routines without waiting for Phase 8.

## Proposed Changes

### 1. Backend (DermaRoutine.Api)

#### [NEW] [IRoutineStreakService](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Services/IRoutineStreakService.cs)
- Logic to calculate the current streak based on `RoutineCompletion` history and active routines.

#### [NEW] [RoutineController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/RoutineController.cs)
- `GET /api/routines`: List user's routines.
- `POST /api/routines`: Create a new routine.
- `GET /api/routines/{id}`: Detailed routine with items and products.
- `POST /api/routines/{id}/items`: Add product to routine.
- `DELETE /api/routines/{id}/items/{itemId}`: Remove item.
- `POST /api/routines/{id}/complete`: Record today's completion.
- `GET /api/routines/streak`: Fetch current streak and today's status.

### 2. Android (app)

#### [NEW] Domain Layer
- **Models**: `Routine`, `RoutineItem`, `StreakInfo`.
- **Use Cases**: `GetRoutinesUseCase`, `CreateRoutineUseCase`, `CompleteRoutineUseCase`, `GetStreakUseCase`, etc.

#### [NEW] Data Layer
- **DTOs**: `RoutineDto`, `RoutineItemDto`, `CompletionRequest`.
- **Repository**: `RoutineRepositoryImpl` using Retrofit `RoutineApiService`.

#### [NEW] Presentation Layer
- **Routine Screen**: Overview of Morning and Evening routines.
- **Create/Edit Routine**: Form to set routine name and type.
- **Routine Detail**: List of products, reordering, and the "Complete Routine" button.
- **Home Integration**: Update `HomeViewModel` to fetch real streak and completion data.

### 3. UI/UX Components
- **RoutineCard**: Themed card showing completion status.
- **ProductItemRow**: Displaying product info within a routine.
- **CompletionFeedback**: Visual confirmation (e.g., checkmark animation or snackbar) when a routine is finished.

## Verification Plan

### Automated Tests
- `dotnet build` & `gradlew assembleDebug`.
- Unit test for `RoutineStreakService` logic (consecutive days, breaking streak).

### Manual Verification
1. **Create Routine**: Create a "Morning Cleanse" routine.
2. **Add Item**: Add a placeholder product to the routine.
3. **Complete**: Tap "Complete Routine" and verify the card updates to "Completed".
4. **Streak**: Verify the Home screen shows "1 day streak".
5. **Security**: Ensure User A cannot view or complete User B's routines using Swagger tests with different tokens.
