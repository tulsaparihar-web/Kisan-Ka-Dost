# Kisan Ka Dost – Android App

**Parihar Digital Labs**  
Hindi + English | Rajasthan-first | Offline-first

## Project details
- Package: `com.parihardigitallabs.kisankadost`
- Kotlin + Jetpack Compose + Material 3
- MVVM + Repository pattern
- Room database for on-device farm profile storage
- Minimum Android: API 26 (Android 8.0)
- Compile/Target SDK: 35
- Java/JVM target: 17

## Build APK using GitHub Actions
No Android Studio or local Android SDK is required.

1. Open the [Actions page](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions).
2. Open the latest successful **Android APK Build** run.
3. Download the `Kisan-Ka-Dost-debug-apk` artifact.
4. Extract the ZIP and install `app-debug.apk` on an Android phone.

Every push to `main` triggers a cloud build. Older in-progress builds are cancelled when a newer commit arrives.

## Current features
- Home dashboard and feature navigation
- Hindi / English language switch, saved locally
- My Farm profile: farmer name, mobile number, village, tehsil, district
- Farm details: land area/unit, irrigation source, main crops and livestock
- Farmer profile stored locally using Room; no account or internet connection required
- Farm Diary, Learn, Crop Care, Plant, Animal Care, Trees, Soil and Government Schemes module entry points

## Development milestones
- **M1 – App foundation:** branding, dashboard, navigation, bilingual UI and cloud APK build.
- **M2 – Farmer and farm setup:** offline farmer profile and basic farm details.
- **M3 – Crop and plant care:** crop guides, seasonal information and plant problem support.
- **M4 – Animal husbandry:** livestock profiles and practical care information.
- **M5+ –** farm diary, Rajasthan schemes, weather, alerts and carefully scoped AI assistance.

## Scope and safety
Weather and other live services are not yet connected. Screens for future modules are clearly placeholders until their content and workflows are implemented and reviewed. The app does not yet provide crop-disease diagnoses or veterinary treatment advice.
