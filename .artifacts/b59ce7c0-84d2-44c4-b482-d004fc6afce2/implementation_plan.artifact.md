# Phase 10: OCR Ingredient Scanner Implementation Plan

This phase implements an OCR-based ingredient scanner, allowing users to scan product ingredient lists using the camera, review the recognized text, and parse it into individual ingredients.

## User Review Required

> [!NOTE]
> **Bulk Save**: I will add a bulk ingredient save endpoint to the backend to ensure the entire ingredient list is saved atomically.
>
> **Parser Logic**: The ingredient parser will split text based on commas, semicolons, and newlines. The user will have a chance to review and edit the parsed list before saving.

## Proposed Changes

### 1. Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/kotlinproje/DermaRoutine/gradle/libs.versions.toml)
- Add version for `mlkitTextRecognition = "16.0.0"`.
- Add library `mlkit-text-recognition = { group = "com.google.mlkit", name = "text-recognition", version.ref = "mlkitTextRecognition" }`.

#### [MODIFY] [build.gradle.kts (app)](file:///C:/kotlinproje/DermaRoutine/app/build.gradle.kts)
- Add `implementation(libs.mlkit.text.recognition)`.

### 2. Backend (DermaRoutine.Api)

#### [MODIFY] [ProductDto.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/DTOs/ProductDto.cs)
- Add `BulkAddIngredientsRequest` DTO.

#### [MODIFY] [ProductController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/ProductController.cs)
- Implement `POST /api/products/{id}/ingredients/bulk`:
    - Clears existing ingredients for the product.
    - Adds the new list in a single transaction.

### 3. OCR Analysis (Domain & Data)

#### [NEW] [TextRecognizer.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/domain/scanner/TextRecognizer.kt) (Interface)
- Defines the behavior for a text recognition analyzer.

#### [NEW] [MlKitTextRecognizer.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/scanner/MlKitTextRecognizer.kt)
- Implements ML Kit `TextRecognition` logic.

#### [NEW] [IngredientTextParser.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/domain/scanner/IngredientTextParser.kt)
- Pure Kotlin logic to split raw OCR text into a list of strings (Aqua, Glycerin, etc.).

### 4. Presentation Layer (OCR)

#### [NEW] [OcrScannerScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/scanner/OcrScannerScreen.kt)
- CameraX preview with a frame for ingredients.
- "Scan" button to trigger capture and processing.

#### [NEW] [OcrResultScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/scanner/OcrResultScreen.kt)
- Editable multiline text field showing the raw recognized text.

#### [NEW] [IngredientPreviewScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/scanner/IngredientPreviewScreen.kt)
- List of individual parsed ingredients.
- Ability to remove or edit items before the final save.

#### [NEW] [OcrViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/scanner/OcrViewModel.kt)
- Manages the multi-step OCR flow state.

### 5. Navigation

#### [MODIFY] [Screen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/Screen.kt)
- Add `OcrScanner`, `OcrResult`, `IngredientPreview` destinations.

#### [MODIFY] [NavGraph.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/NavGraph.kt)
- Integrate the OCR flow routes.

## Verification Plan

### Automated Tests
- Unit tests for `IngredientTextParser` with various separators and messy text.
- `dotnet build` & `gradlew assembleDebug`.

### Manual Verification
1. **Camera Flow**: Open a product, tap "Scan Ingredients", grant permission, and see the camera preview.
2. **Detection**: Point at an ingredient list, tap Scan, and verify text appears on the review screen.
3. **Editing**: Correct an OCR typo in the review screen.
4. **Parsing**: Continue to the preview screen and verify the text is split into a list.
5. **Saving**: Confirm and save, then verify the ingredients appear in the Product Detail screen.
