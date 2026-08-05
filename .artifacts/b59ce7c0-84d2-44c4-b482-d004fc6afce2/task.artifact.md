# Phase 16: Interactive Home Screen Checklist - Task List

- [x] Backend: Granular Tracking
    - [x] Create `RoutineItemCompletion.cs` entity
    - [x] Update `AppDbContext.cs`
    - [x] Update `RoutineDtos.cs` (Item completion status)
    - [x] Implement `ToggleItemCompletion` in `RoutineController.cs`
- [x] Android: Data Layer
    - [x] Update `Routine` domain models
    - [x] Update `RoutineDtos` & `RoutineMapper`
    - [x] Update `RoutineApiService` & `Repository`
- [x] Android: UI Implementation
    - [x] Update `HomeViewModel.kt` to fetch routine items
    - [x] Implement `toggleItem` logic in ViewModel
    - [x] Redesign `HomeScreen.kt` with interactive checklist cards
- [x] Build and Verify
    - [x] Successful `dotnet build`
    - [x] Successful `gradlew assembleDebug`
