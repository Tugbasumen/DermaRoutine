# Premium Home Screen Redesign - Task List

## Step 1: Backend Analytics
- [x] Update `StatisticsDto.cs` (Add WeeklyActivity & SkinScore)
- [x] Update `StatisticsController.cs` (Calculate 7-day trend & score)
- [x] Verify with `dotnet build`

## Step 2: Android Data Layer
- [x] Update `StatisticsDtos.kt` and `ProgressStats.kt`
- [x] Update `StatisticsRepositoryImpl.kt` mapping
- [/] Update `HomeUiState.kt` and `HomeViewModel.kt` (DYNAMIZING)

## Step 3: Premium UI Components
- [x] Implement `CircularProgressIndicator` (Custom Drawing)
- [x] Implement `WeeklyBarChart` (Canvas-based)
- [x] Implement `StatCard` (Streak & Score)

## Step 4: HomeScreen Redesign
- [x] Implement Gradient Header & Greeting
- [x] Implement Featured Routine Checklist Card
- [/] Final visual polish & DYNAMIZING data (Next Reminder, Smart Routine selection)
- [ ] Final verification
