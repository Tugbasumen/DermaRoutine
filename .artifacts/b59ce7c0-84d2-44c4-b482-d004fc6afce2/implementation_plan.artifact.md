# Phase 16: Interactive Home Screen Routine Checklist Plan

This phase implements the "Interactive Checklist" on the Home Screen as described in the project vision. Users will be able to check off individual products in their morning and evening routines directly from the dashboard.

## Proposed Changes

### 1. Backend: Granular Completion Tracking

#### [NEW] [RoutineItemCompletion.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Entities/RoutineItemCompletion.cs)
- Entity to track daily completion of specific products within a routine.
- Fields: `Id`, `RoutineItemId`, `UserId`, `CompletedAt`.

#### [MODIFY] [AppDbContext.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Data/AppDbContext.cs)
- Add `DbSet<RoutineItemCompletion>`.
- Configure relationship (User -> ItemCompletions).

#### [MODIFY] [RoutineDtos.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/DTOs/RoutineDto.cs)
- Update `RoutineItemDto` to include `bool IsCompletedToday`.
- Update `RoutineDto` to include `int CompletedItemCount`.

#### [MODIFY] [RoutineController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/RoutineController.cs)
- Add `POST /api/routine/items/{itemId}/toggle`: Toggles completion status for an item today.
- Update `GetRoutines` and `GetRoutine` to calculate and return item-level completion status.

### 2. Android: Data Layer

#### [MODIFY] [Routine models & DTOs]
- Add `isCompletedToday` to `RoutineItem`.
- Add `completedItemCount` to `Routine`.

#### [MODIFY] [RoutineApiService.kt]
- Add `toggleItemCompletion(itemId)`.

### 3. Android: Home Screen UI

#### [MODIFY] [HomeViewModel.kt]
- Fetch full routine details (including items) instead of just summaries.
- Add `toggleItem(itemId)` function to handle checklist clicks.

#### [MODIFY] [HomeScreen.kt]
- Replace the summary cards with an **Expanded Checklist View**.
- **Checklist Items**: Show product name with a clickable checkbox/circle.
- **Progress Bar**: Visual indicator of completion (e.g., "2 / 5 tamamlandı").

## Verification Plan

### Automated Tests
- `dotnet build` & `gradlew assembleDebug`.

### Manual Verification
1. **Check Item**: Tap "Temizleyici" on Home Screen. Verify the checkbox fills and the progress updates to "1 / 5".
2. **Persistence**: Restart the app. Verify the item remains checked.
3. **Synchronization**: Check an item on Home Screen, navigate to Routine Detail, and verify it's checked there too.
