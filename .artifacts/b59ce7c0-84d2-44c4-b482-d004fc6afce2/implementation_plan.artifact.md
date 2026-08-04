# Final Localization & Network Security Fix Plan

This plan addresses the remaining hardcoded English strings identified in the UI and resolves the "CLEARTEXT communication" network security error preventing connections to the local backend.

## User Review Required

> [!IMPORTANT]
> **Network Security**: To allow the app to communicate with your local backend (10.0.2.2) via HTTP, I will add a network security configuration. This is necessary for development but will be restricted to the local IP for security.

## Proposed Changes

### 1. Localization Cleanup

#### [MODIFY] [strings.xml](file:///C:/kotlinproje/DermaRoutine/app/src/main/res/values/strings.xml)
- Add missing strings for the Journal placeholder.
- Ensure onboarding button labels are consistent.

#### [MODIFY] [OnboardingScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/onboarding/OnboardingScreen.kt)
- Fix "Get Started" and "Continue" hardcoded buttons.

#### [MODIFY] [JournalScreen.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/presentation/journal/JournalScreen.kt)
- Fix "Skin Journal Coming Soon 🌿" hardcoded text.

### 2. Network Security Fix

#### [NEW] [network_security_config.xml](file:///C:/kotlinproje/DermaRoutine/app/src/main/res/xml/network_security_config.xml)
- Define a policy to allow cleartext (HTTP) traffic specifically for `10.0.2.2` (the Android emulator loopback).

#### [MODIFY] [AndroidManifest.xml](file:///C:/kotlinproje/DermaRoutine/app/src/main/AndroidManifest.xml)
- Link the new network security configuration in the `<application>` tag.

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to verify no resource or manifest errors.

### Manual Verification
1. **Onboarding**: Swipe through the pages and verify buttons are in Turkish ("Başla" / "Devam Et").
2. **Journal**: Open the Journal tab and verify the message is in Turkish.
3. **Routine/API**: Navigate to the Routine screen and verify that the red "CLEARTEXT" error is gone and routines are loaded (or shows "Henüz rutin yok").
