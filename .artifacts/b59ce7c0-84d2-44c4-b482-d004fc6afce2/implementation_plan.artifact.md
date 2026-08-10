# Phase 21: Final UI Polish & Analytics Alignment Plan

This phase focuses on the "final 10%" of the UI/UX, ensuring every screen perfectly matches the project vision and the premium "Emma" design reference. We will enhance the analytics reporting and polish the visual transitions.

## Proposed Changes

### 1. Home Screen: Weekly Summary & Polish
- **Goal**: Show "Bu Hafta: 4 / 7 gün" and match the typography of the reference.
- **Changes**:
    - Update `WeeklyProgressSection` to include a summary text: "Rutin devamlılığın: {streak} günlük seri".
    - Add the completion ratio (e.g., "4 / 7 gün") next to the weekly chart title.
    - Fine-tune bar widths and spacing in `WeeklyBarChart` to match the reference.

### 2. Progress Screen: Enhanced Activity View
- **Goal**: Make the "Fotoğraf Günlüğü" and "Analizler" feel more integrated.
- **Changes**:
    - Add a "Tümü" action next to the "Fotoğraf Günlüğü" title.
    - Polish the `SkinPhotoCard` to have a slightly softer shadow and better date formatting.

### 3. Comparison Feature: Interaction Polish
- **Goal**: Improve the "Önce / Sonra" experience.
- **Changes**:
    - Update `ComparePhotosScreen.kt` to allow a "Full Screen" view of the selected pair.
    - Add a "Paylaş" (Share) option to the comparison result.

### 4. General Branding Consistency
- **Goal**: Ensure the "DermaRoutine Green" is used correctly everywhere.
- **Changes**:
    - Review all screens for any lingering default purple or grey tones.
    - Ensure all icons (Bell, Profile, Back) use the consistent brand primary color.

## Verification Plan

### Automated Tests
- `gradlew assembleDebug` to ensure no resource errors.

### Manual Verification
1. **Side-by-Side Comparison**: Open the app and the reference image. Verify the alignment, font weights, and colors match.
2. **Data Consistency**: Verify the "4 / 7 gün" summary matches the bars in the chart.
3. **Smooth Navigation**: Verify that moving between Home and Progress feels native and premium.
