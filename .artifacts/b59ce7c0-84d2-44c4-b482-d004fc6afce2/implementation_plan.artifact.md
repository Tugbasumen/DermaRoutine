# Phase 15: Skin Journal Implementation - Part 1: Backend

This part focuses on creating the backend infrastructure for the Skin Journal, allowing users to save and retrieve their daily mood and notes.

## Proposed Changes

### 1. Backend: DTOs

#### [NEW] [JournalDtos.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/DTOs/JournalDtos.cs)
- `CreateJournalEntryRequest`: Content, Mood (Emoji/String).
- `JournalEntryResponse`: Id, Content, Mood, CreatedAt.

### 2. Backend: Controller

#### [NEW] [JournalController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/JournalController.cs)
- `POST /api/journal`: Create a new entry for the authenticated user.
- `GET /api/journal`: Get all entries for the user (ordered by date).
- `DELETE /api/journal/{id}`: Delete a specific entry.

## Verification Plan

### Automated Tests
- Run `dotnet build` to ensure no syntax errors.

### Manual Verification
- Use Swagger/Postman to verify:
    1. Authenticated user can create an entry.
    2. User can list only their own entries.
    3. User can delete an entry.
