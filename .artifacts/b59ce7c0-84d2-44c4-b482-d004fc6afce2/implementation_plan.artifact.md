# Phase 17: Progress Statistics & Analytics Plan

This phase implements the analytical part of the application. Users will be able to visualize their routine consistency and see a breakdown of their skin "mood" over the last 30 days, as described in the project vision.

## Proposed Changes

### 1. Backend: Analytics Engine

#### [NEW] [StatisticsController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/StatisticsController.cs)
- `GET /api/statistics/summary`:
    - Calculates the percentage of morning and evening routines completed in the last 7 days.
    - Aggregates mood data from `JournalEntries` for the last 30 days.

#### [NEW] [StatisticsDto.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/DTOs/StatisticsDto.cs)
- `ProgressSummaryDto`:
    - `MorningCompletionRate`: Double (0-100).
    - `EveningCompletionRate`: Double (0-100).
    - `CurrentStreak`: Int.
    - `MoodCounts`: List of `MoodStatDto` (Emoji, Count).

### 2. Android: Data Layer

#### [NEW] [Statistics Models & Repository]
- `ProgressStats` domain model.
- `StatisticsRepository` and `StatisticsApiService`.
- `GetProgressStatsUseCase`.

### 3. Android: Progress UI Enhancement

#### [MODIFY] [ProgressScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/progress/ProgressScreen.kt)
- Add a **Statistics Section** at the top of the timeline.
- **Routine Analytics**: Circular or linear progress indicators for Morning/Evening consistency.
- **Mood Analytics**: A grid or list showing mood emoji distribution (e.g., 😊 12 gün, 😐 10 gün).

#### [MODIFY] [ProgressViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/progress/ProgressViewModel.kt)
- Integrate `GetProgressStatsUseCase` to fetch and expose analytical data to the UI.

## Verification Plan

### Automated Tests
- `dotnet build` & `gradlew assembleDebug`.

### Manual Verification
1. **Consistency Stats**: Complete several routines and verify the percentage increases correctly on the Progress screen.
2. **Mood Breakdown**: Add multiple journal entries with different moods and verify the counts are accurate.
3. **Time Window**: Ensure the statistics only reflect the relevant periods (Last 7 days for routines, Last 30 for mood).
