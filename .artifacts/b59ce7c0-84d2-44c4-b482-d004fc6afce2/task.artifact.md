# Phase 15: Skin Journal Implementation - Task List

## Part 1: Backend Infrastructure
- [x] Create `JournalDtos.cs`
- [x] Implement `JournalController.cs` (CRUD)
- [x] Verify with `dotnet build`

## Part 2: Android Domain & Data
- [x] Create `JournalEntry` Domain Model
- [x] Create `JournalApiService.kt` and DTOs
- [x] Implement `JournalRepository`
- [x] Create UseCases (`GetJournalEntries`, `AddJournalEntry`)
- [x] Configure `JournalModule.kt` (DI)

## Part 3: Android UI (Presentation)
- [ ] Create `JournalViewModel.kt`
- [ ] Design `MoodSelector` component
- [ ] Implement `JournalScreen.kt` (Entry form + History list)
- [ ] Integrate with Navigation
