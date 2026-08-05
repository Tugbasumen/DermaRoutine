# Phase 15: Skin Journal Implementation - Part 3: Android UI (Presentation)

This final part brings the Skin Journal to life with a modern, user-friendly interface. Users will be able to select their daily mood, write notes, and view their skincare history.

## Proposed Changes

### 1. ViewModel

#### [NEW] [JournalViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/journal/JournalViewModel.kt)
- Manages `JournalUiState` (Loading, Success, Error).
- Functions to fetch all entries and save a new entry.
- State for the current input (selected mood and note text).

### 2. UI Components

#### [NEW] MoodSelector
- A row of interactive emoji buttons: 😊 (İyi), 😐 (Normal), 😕 (Hassas), 😣 (Kötü).

#### [NEW] JournalEntryCard
- A clean card for the history list showing the mood, date, and truncated content.

### 3. Screen Update

#### [MODIFY] [JournalScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/journal/JournalScreen.kt)
- **Header**: "Günlük Notlarım" title.
- **Input Section**:
    - "Bugün cildin nasıl hissediyor?" label.
    - The `MoodSelector`.
    - `AppTextField` for notes.
    - "Kaydet" button.
- **History Section**:
    - "Geçmiş Kayıtlar" subtitle.
    - A `LazyColumn` showing all previous entries using `JournalEntryCard`.

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to ensure no UI or build errors.

### Manual Verification
1. **Save Entry**: Select a mood, write a note, and tap "Kaydet". Verify it appears in the list immediately.
2. **List History**: Scroll through past entries and verify dates are formatted correctly.
3. **Empty State**: Verify a friendly message is shown when no entries exist.
4. **Validation**: Ensure "Kaydet" is disabled if the note content is empty.
