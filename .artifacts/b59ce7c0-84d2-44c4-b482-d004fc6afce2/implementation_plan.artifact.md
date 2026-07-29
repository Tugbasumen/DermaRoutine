# Phase 9: Barcode Scanning Implementation Plan

This phase integrates a barcode scanner into the product management flow, allowing users to quickly add products by scanning their barcodes using CameraX and ML Kit.

## User Review Required

> [!IMPORTANT]
> **Form State Preservation**: To ensure that user-entered data (name, brand, etc.) isn't lost when navigating to the scanner, I will move the form state from the `AddEditProductScreen` Composable into the `ProductViewModel`.
>
> **Camera Permission**: The app will request camera access when the user taps the "Scan" button. I will handle all permission states (Granted, Denied, Permanently Denied).

## Proposed Changes

### 1. Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/kotlinproje/DermaRoutine/gradle/libs.versions.toml)
- Add versions and libraries for:
    - `androidx.camera:camera-camera2`
    - `androidx.camera:camera-lifecycle`
    - `androidx.camera:camera-view`
    - `com.google.mlkit:barcode-scanning`

#### [MODIFY] [build.gradle.kts (app)](file:///C:/kotlinproje/DermaRoutine/app/build.gradle.kts)
- Add the new dependencies.

### 2. Barcode Scanning (Domain & Data)

#### [NEW] [BarcodeScanner.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/domain/scanner/BarcodeScanner.kt) (Interface)
- Defines the behavior for a barcode analyzer.

#### [NEW] [MlKitBarcodeScanner.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/data/scanner/MlKitBarcodeScanner.kt)
- Implements ML Kit `BarcodeScanning` logic.
- Converts CameraX `ImageProxy` to ML Kit `InputImage`.

### 3. Presentation Layer (Scanner)

#### [NEW] [BarcodeScannerScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/scanner/BarcodeScannerScreen.kt)
- UI with `PreviewView` (CameraX).
- Semi-transparent overlay with a focused "scan area" box.
- Instruction text and a back button.
- Handling of camera lifecycle and permissions.

#### [NEW] [BarcodeScannerViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/scanner/BarcodeScannerViewModel.kt)
- Manages the scanning state (Initializing, Scanning, Detected, Error).
- Provides a way to stop scanning after a success.

### 4. Integration with Product Form

#### [MODIFY] [ProductViewModel.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/products/ProductViewModel.kt)
- Add properties to hold the temporary form state (name, brand, category, barcode, notes).
- Add methods to update these fields.
- Add `checkDuplicateBarcode(barcode: String)` to verify if the user already has this product.

#### [MODIFY] [AddEditProductScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/products/AddEditProductScreen.kt)
- Update to use the `ProductViewModel` for all form fields.
- Add a "Scan" icon/button next to the Barcode field that navigates to the scanner.
- Observe the scanning result from the navigation result (SavedStateHandle).

### 5. Navigation

#### [MODIFY] [Screen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/Screen.kt)
- Add `BarcodeScanner` destination.

#### [MODIFY] [NavGraph.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/navigation/NavGraph.kt)
- Integrate the scanner route.
- Handle passing the scanned barcode back to the product form.

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to verify dependency integration.
- Unit test for `ProductViewModel` duplicate check logic.

### Manual Verification
1. **Permission**: Launch the scanner, deny permission, verify the warning message. Grant permission and verify camera starts.
2. **Scanning**: Point the camera at a common barcode (EAN-13/UPC) and verify it is detected and the value is displayed.
3. **Return to Form**: Verify navigation returns to the Add Product screen with the barcode field filled and other fields preserved.
4. **Duplicate**: Scan a barcode already in the user's library and verify the "Already in library" warning.
