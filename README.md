# Kisan Ka Dost – Android App

**Parihar Digital Labs**  
Hindi + English | Rajasthan-first | Offline-first

## Project details
- Package: `com.parihardigitallabs.kisankadost`
- Kotlin + Jetpack Compose + Material 3
- MVVM + Repository pattern
- Room database for on-device farmer profile storage
- TensorFlow Lite for on-device plant leaf screening
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
- Plant Doctor: camera photo and gallery image input
- Offline TensorFlow Lite leaf screening with top-three predictions and confidence display
- Bilingual first-step care guidance and low-confidence warnings
- Searchable bilingual care guide for field crops, vegetables, fruit trees, indoor plants, decorative flowers, herbs and medicinal plants
- Other future module entry points remain clearly marked as planned

## Development milestones
- **M1 – App foundation:** branding, dashboard, navigation, bilingual UI and cloud APK build.
- **M2 – Farmer and farm setup:** offline farmer profile and basic farm details. Source and cloud APK build verified.
- **M3 – Plant Doctor and plant care:** in progress; photo-based crop leaf screening and searchable horticulture / home gardening guide.
- **M4 – Animal husbandry:** planned.
- **M5 – Farm diary and garden diary:** planned.
- **Later / deferred:** Rajasthan government schemes, weather, alerts and smart assistance.

## Plant Doctor model scope
The current bundled model has 39 output classes, including a background class, and covers selected crop leaf classes. It is not a general plant species identifier. Indoor and ornamental plants currently have searchable care guides; broad photo-based species identification is a later M3 enhancement.

The model is downloaded from a pinned upstream revision during GitHub Actions and its Git blob hash is verified before the APK build. Attribution and the upstream MIT license are included in `THIRD_PARTY_NOTICES.md` and the app assets.

## Safety and limitations
- AI output is an advisory screening result, not a confirmed diagnosis.
- The model was trained for a limited set of crop/disease classes; results may be less reliable on real field photos, unfamiliar varieties, mixed backgrounds, or unsupported plants.
- Low-confidence and unsupported-image results are explicitly flagged.
- Confirm symptoms and local conditions with a qualified agricultural or horticultural expert before treatment. Follow approved product labels and local recommendations.
- Weather and other live services are not connected.
