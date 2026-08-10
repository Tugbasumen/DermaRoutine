# Phase 19: AI Skin Assistant Chat Implementation Plan

This phase implements a real-time AI messaging interface where users can ask general skincare questions and receive educational guidance from Gemini, matching the provided design.

## Proposed Changes

### 1. Backend: General Chat Engine

#### [NEW] [AssistantDtos.cs Update](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/DTOs/AssistantDtos.cs)
- `GeneralChatRequest`: User's message.
- `GeneralChatResponse`: AI's text response.

#### [MODIFY] [AssistantController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/AssistantController.cs)
- Implement `POST /api/assistant/chat`:
    - Handles general skincare inquiries.
    - Uses a system prompt focused on education, safety, and neutral guidance.
    - Personalizes the greeting if the user's name is available in the token/profile.

### 2. Android: Data Layer

#### [NEW] [ChatMessage.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/domain/model/ChatMessage.kt)
- Model representing a message in the chat UI: `text`, `isFromUser`, `timestamp`.

#### [MODIFY] [AssistantApiService.kt] & [AssistantRepository.kt]
- Add methods to send a message and receive a response.

### 3. Android: UI Implementation (Matching Reference)

#### [NEW] [AssistantViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/assistant/AssistantViewModel.kt)
- Manages a list of `ChatMessage`.
- Handles the "typing..." state indicator.
- Provides "Quick Suggestion" prompts (e.g., "Yağlı cilt için rutin nedir?").

#### [MODIFY] [AssistantScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/assistant/AssistantScreen.kt)
- **Message List**: A `LazyColumn` with bubble-style messages.
    - User messages: Brand green background, right-aligned.
    - AI messages: Soft mint background, left-aligned.
- **Typing Indicator**: Animated "..." when AI is processing.
- **Input Field**: Bottom sticky bar with a text field and a circular send button.
- **Quick Actions**: Scrollable chips above the input field for common questions.

## Verification Plan

### Automated Tests
- `dotnet build` & `gradlew assembleDebug`.

### Manual Verification
1. **Chat Flow**: Send "Retinol ile C vitamini kullanılır mı?". Verify the user bubble appears instantly and the AI response follows.
2. **Branding**: Verify bubbles use the established green palette.
3. **Quick Prompts**: Tap "Niacinamide'i açıkla". Verify the message is sent automatically.
4. **Safety**: Verify the AI includes the educational disclaimer in its response.
