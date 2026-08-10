# Phase 19: AI Skin Assistant Chat - Task List

## Step 1: Backend Infrastructure
- [x] Update `AssistantDtos.cs` (Add GeneralChat request/response)
- [x] Implement `chat` endpoint in `AssistantController.cs`
- [x] Verify with `dotnet build`

## Step 2: Android Data Layer
- [x] Create `ChatMessage` domain model
- [x] Update `AssistantApiService.kt` and DTOs
- [x] Update `AssistantRepositoryImpl.kt`
- [x] Create `GetChatResponseUseCase.kt`

## Step 3: Android UI (Presentation)
- [x] Create `AssistantViewModel.kt` (State & Message list)
- [x] Redesign `AssistantScreen.kt` (Chat bubbles & animations)
- [x] Implement Quick Suggestion chips
- [x] Connect with Navigation (Added `AIAssistant` route)

## Step 4: Final Integration & Polish
- [x] Add "Yazıyor..." indicator
- [x] Personalized greeting in Chat
- [x] Final visual alignment with reference image
