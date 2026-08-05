# Skin Journal Implementation - Backend Walkthrough

The first step of the Skin Journal feature is completed. I have implemented the backend infrastructure to handle daily notes and mood tracking.

## Changes Made

### 1. Backend: Data Transfer Objects (DTOs)
- **JournalDtos.cs**: Defined the request and response models for the API.
    - `CreateJournalEntryRequest`: Captures the content and mood from the Android app.
    - `JournalEntryResponse`: Returns the saved entry details, including the generated ID and timestamp.

### 2. Backend: API Controller
- **JournalController.cs**: Implemented a new secure controller to manage journal data.
    - `GET /api/journal`: Retrieves a chronological list of entries for the logged-in user.
    - `POST /api/journal`: Saves a new daily mood and note.
    - `DELETE /api/journal/{id}`: Allows users to remove their previous entries.

## Verification Results
- [x] **Build Status**: The backend project builds successfully (`dotnet build`).
- [x] **Security**: All endpoints are protected with `[Authorize]`, ensuring user data isolation via Firebase UID.

## Recommended Commit Message
`feat(backend): implement journal controller and DTOs for skin journal`
