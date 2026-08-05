# Final Visual & Language Polish Plan

This plan addresses the "lila" (lavender) color mismatch by enforcing a consistent green-based palette and cleans up the remaining hardcoded English strings in the Onboarding and Journal sections.

## Proposed Changes

### 1. Design System: Green Theme Enforcement

#### [MODIFY] [Color.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/designsystem/Color.kt)
- Define a professional green palette:
    - `Primary`: #4E8A5E (Main Brand Green)
    - `PrimaryContainer`: #DCEFE3 (Light background for cards)
    - `SecondaryContainer`: #EAF5EE (Used for selected menu items to replace lavender)

#### [MODIFY] [Theme.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/designsystem/Theme.kt)
- Explicitly map all Material 3 color slots (especially `secondaryContainer`) to the green palette to override default lavender/purple behavior.

### 2. Localization: 100% Turkish Coverage

#### [MODIFY] [OnboardingScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/onboarding/OnboardingScreen.kt)
- Replace hardcoded "Get Started" and "Continue" with `R.string.onboarding_get_started` and `R.string.onboarding_continue`.

#### [MODIFY] [JournalScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/journal/JournalScreen.kt)
- Ensure the localized version is used and no "Coming Soon" placeholder remains.

#### [MODIFY] [strings.xml](file:///C:/kotlinproje/DermaRoutine/app/src/main/res/values/strings.xml)
- Verify and add any missing translations for buttons and status indicators.

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to ensure all resource IDs are correct.

### Manual Verification
1. **Visual Sweep**: Check the Bottom Navigation. The selected item indicator should now be a soft green/mint color instead of purple.
2. **Onboarding Sweep**: Verify all 3 pages and the action buttons are in Turkish.
3. **Journal Sweep**: Ensure the daily entry form and history list are fully localized.
