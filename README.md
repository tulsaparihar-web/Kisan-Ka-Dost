# किसान का दोस्त – Android App

**Parihar Digital Labs**  
**Tagline:** खेती • बागवानी • पशुपालन  
Hindi + English | Rajasthan-first | Offline-first

**किसान का दोस्त (Kisan Ka Dost)** is an all-round digital companion for **कृषि (खेती), बागवानी and पशुपालन**. The product scope includes field crops, horticulture, livestock, farm records, soil and water, weather, markets, government schemes and farm economics. See [PRODUCT_SCOPE.md](PRODUCT_SCOPE.md) for the detailed feature blueprint and delivery roadmap.

## Project details
- App display name: **किसान का दोस्त**
- English brand name: **Kisan Ka Dost**
- Tagline: **खेती • बागवानी • पशुपालन**
- Parent brand: **Parihar Digital Labs**
- Repository: Kisan-Ka-Dost (unchanged)
- Package: com.parihardigitallabs.kisankadost
- Kotlin + Jetpack Compose + Material 3
- MVVM + Repository pattern
- Room database for on-device farmer profile and future farm records
- TensorFlow Lite for on-device plant leaf screening
- Minimum Android: API 26 (Android 8.0)
- Compile/Target SDK: 35
- Java/JVM target: 17

## Build APK using GitHub Actions
No Android Studio or local Android SDK is required.

1. Open the [Actions page](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions).
2. Open the latest successful **Android APK Build** run.
3. Download the Kisan-Ka-Dost-debug-apk artifact.
4. Extract the ZIP and install app-debug.apk on an Android phone.

Every push to main triggers a cloud build. Older in-progress builds are cancelled when a newer commit arrives.

## Implemented and build-verified foundations
- Home dashboard and feature navigation
- Hindi / English language switch, saved locally
- My Farm profile: farmer name, mobile number, village, tehsil, district
- Farm details: land area/unit, irrigation source, main crops and livestock
- Farmer profile stored locally using Room; no account or internet connection required
- Plant Doctor: camera photo and gallery image input
- Offline TensorFlow Lite leaf screening with top-three predictions and confidence display
- Bilingual first-step care guidance and low-confidence warnings
- Searchable bilingual care guide for field crops, vegetables, fruit trees, indoor plants, decorative flowers, herbs and medicinal plants

## Development milestones
- **M1 – App foundation:** complete; cloud APK build verified.
- **M2 – Farmer and farm setup:** complete; offline farmer profile and basic farm details, cloud APK build verified.
- **M3 – Agriculture and horticulture foundation:** in progress; Plant Doctor and plant-care guide are the first delivered pieces. Expand to crop library, crop calendar, soil/water and broader horticulture.
- **M4 – Animal husbandry:** planned; species/breed library, feeding, housing, preventive-care records and animal profiles.
- **M5 – Farm, garden and livestock diaries:** planned.
- **M6 – Rajasthan advisory services:** planned; verified local calendars, weather and alerts.
- **M7 – Markets, schemes and services:** planned.
- **M8 – Farm economics and smart assistance:** planned.

## Plant Doctor model scope
The current model has 39 output classes, including a background class, and covers selected crop leaf classes. It is not a general plant species identifier. Indoor and ornamental plants currently have searchable care guides; broader photo-based identification is a later enhancement.

The model is downloaded from a pinned upstream revision during GitHub Actions and its Git blob hash is verified before the APK build. Attribution and the upstream MIT license are included in THIRD_PARTY_NOTICES.md and the app assets.

## Content and safety
- Agricultural, horticultural and veterinary content will be based on official and research-backed sources, localized by district/region, season and crop/species.
- AI output is advisory screening, not a confirmed diagnosis.
- Do not treat an AI result as a pesticide or veterinary prescription. Seek qualified local advice before treatment.
- Weather, live mandi prices and scheme updates must show their source and last-updated time; no live data is claimed until connected.
- Farmer records work offline by default. Optional sharing/sync must be explicit.
