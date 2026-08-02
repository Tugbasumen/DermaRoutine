# App Launcher Icon Integration Plan

This plan aims to replace the default Android launcher icon with the new DermaRoutine professional logo, so it appears correctly on the phone's home screen and app drawer.

## Proposed Changes

### 1. Android Manifest Update

#### [MODIFY] [AndroidManifest.xml](file:///C:/kotlinproje/DermaRoutine/app/src/main/AndroidManifest.xml)
- Update the `android:icon` and `android:roundIcon` attributes in the `<application>` tag to point to `@drawable/app_logo` instead of `@mipmap/ic_launcher`.

### 2. Cleanup (Optional but Recommended)
- I will keep the existing mipmap folders for now to avoid build errors, but the manifest will prioritize the new logo.

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to ensure the manifest correctly links to the new drawable resource.

### Manual Verification
1. **Home Screen**: Install the app on a device/emulator and verify that the DermaRoutine logo appears on the home screen.
2. **Settings/App Info**: Check the app's entry in Android Settings to ensure the logo is consistent.
