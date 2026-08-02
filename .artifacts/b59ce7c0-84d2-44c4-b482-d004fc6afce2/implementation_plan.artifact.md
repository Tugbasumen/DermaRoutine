# Phase 11: AI Ingredient Assistant Implementation Plan

This phase implements an AI-powered skincare ingredient assistant using the Gemini API. The backend will handle the communication with Gemini to ensure security and controlled prompt design, while the Android app will provide a user-friendly interface for educational explanations.

## User Review Required

> [!IMPORTANT]
> **Gemini API Key**: You must provide a valid Gemini API key. I will show you how to add it to your backend's **User Secrets** so it is never committed to Git.
>
> **Educational Disclaimer**: All AI responses will strictly include a disclaimer stating that the information is for educational purposes only and not medical advice.

## Proposed Changes

### 1. Backend (DermaRoutine.Api)

#### [MODIFY] [appsettings.json](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/appsettings.json)
- Add placeholder configuration for Gemini (Model, BaseUrl).

#### [NEW] [IGeminiService.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Services/IGeminiService.cs) & [GeminiService.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Services/GeminiService.cs)
- Handles raw HTTP communication with the Gemini API.
- Implements secure API key usage from configuration.

#### [NEW] [AssistantController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/AssistantController.cs)
- `POST /api/assistant/ingredient`: Receives an ingredient name and returns a structured educational explanation.
- **System Prompt**: Enforced at the controller/service level to ensure no medical advice is given.

#### [NEW] [AssistantDtos.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/DTOs/AssistantDtos.cs)
- `IngredientExplanationRequest` and `IngredientExplanationResponse`.

### 2. Android (app)

#### [NEW] [AssistantApiService.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/remote/AssistantApiService.kt)
- Retrofit interface for the assistant endpoint.

#### [NEW] Domain Layer
- `IngredientExplanation` (Domain Model).
- `AssistantRepository` (Interface).
- `GetIngredientExplanationUseCase`.

#### [NEW] [IngredientAssistantScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/assistant/IngredientAssistantScreen.kt)
- Displays the AI-generated summary, common uses, and considerations.
- Prominent "Not medical advice" disclaimer.
- Loading and error states.

#### [MODIFY] [ProductDetailScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/products/ProductDetailScreen.kt)
- Make individual ingredients clickable to launch the `IngredientAssistantScreen`.

### 3. Navigation

#### [MODIFY] [Screen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/Screen.kt)
- Update `Assistant` destination to accept an `ingredientName` parameter.

#### [MODIFY] [NavGraph.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/NavGraph.kt)
- Integrate the parameterized assistant route.

## Verification Plan

### Automated Tests
- `dotnet build` & `gradlew assembleDebug`.
- Backend unit tests for prompt structure and safety rules.

### Manual Verification
1. Open a product (e.g., Hydrating Serum).
2. Tap on "Niacinamide" in the ingredient list.
3. Verify that the `IngredientAssistantScreen` opens and shows a loading state.
4. Confirm that a structured explanation appears with the educational disclaimer.
5. Verify that the API key is not visible anywhere in the Android code or Git history.
