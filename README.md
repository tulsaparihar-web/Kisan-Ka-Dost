# किसान का दोस्त – Android App

**Parihar Digital Labs**  
**Tagline:** खेती • बागवानी • पशुपालन  
Hindi + English | Rajasthan-first | Offline-first | Free for everyone

**किसान का दोस्त (Kisan Ka Dost)** is an all-round digital companion for **कृषि (खेती), बागवानी and पशुपालन**. The product scope includes field crops, horticulture, livestock, farm records, soil and water, weather, markets, government schemes and farm economics. See [PRODUCT_SCOPE.md](PRODUCT_SCOPE.md) for the detailed feature blueprint and [MILESTONE_TRACKER.md](MILESTONE_TRACKER.md) for the implementation audit, acceptance checklist and delivery sequence.

## Our promise: free for everyone

Kisan Ka Dost will remain free for every user—no subscriptions, premium tiers, paywalls or paid feature restrictions. All three pillars (खेती, बागवानी and पशुपालन) stay accessible without payment. We aim to sustain the project through responsible, clearly identified and non-disruptive advertising, voluntary donations and mission-aligned institutional/CSR support. Donations never change access. We will not sell or rent farmers' personal information, farm records, photos, phone numbers or precise location data to advertisers. Personal/farm records remain on-device by default; any sync or sharing must be explicit. Advertising must be screened for misleading or unsafe claims and kept separate from independent agricultural guidance.

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

Every push to `main` or a feature branch triggers the APK workflow. It runs JVM unit tests, assembles the APK and uploads the artifact without waiting for an Android emulator. Emulator instrumentation tests are kept in the separate, manually triggered [Android Instrumentation Tests workflow](.github/workflows/android-instrumentation.yml), so emulator startup failures cannot block APK delivery. Both workflows avoid cancelling an active run.

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
- Bilingual soil-health learning guide covering soil sampling/testing, pH and salinity indicators, nutrients, organic matter and responsible fertilizer use (general education, not personalized fertilizer prescriptions)

## Current implementation status (feature branch)

The active feature branch contains the following build-integrated modules. A successful CI build confirms compilation, configured JVM unit tests and APK packaging; it does not replace physical-device acceptance.

- **App foundation:** Home dashboard, Compose navigation, Hindi/English preference and settings.
- **My Farm:** Offline Room-backed farmer/farm profile.
- **Crop Care and Crop Calendar:** Bilingual general crop guides and seasonal calendar.
- **Plant Doctor:** Offline TensorFlow Lite preliminary leaf screening for selected model classes; physical-device accuracy, camera/gallery and performance validation remain open.
- **Horticulture:** Searchable bilingual plant and garden guidance.
- **Soil Health, Irrigation, Crop Protection and Harvest:** General bilingual educational guides; local prescription-level agronomy is not claimed.
- **Animal Care and Animal Registry:** Bilingual care guidance, offline animal profiles, health-event history and veterinarian-confirmed reminders; device reminder/migration acceptance remains open.
- **Farm Diary:** Offline field/garden/animal/expense/labour/input/production/sales records, local photos, CSV export, reminders and user-triggered ZIP backup/restore; physical-device backup/restore acceptance remains open.
- **District Advisory:** District selection and stored/general advisory information; no live weather feed is claimed.
- **Government Schemes:** Offline scheme information with official external links; live eligibility/status is not claimed.
- **M8 Offline Knowledge Assistant:** Offline bilingual keyword-based knowledge search. This is general educational content, not AI diagnosis or individualized agronomy/veterinary advice.

**Not yet delivered as live services:** live weather/alerts, live mandi prices, cloud sync, multi-device conflict resolution, personalized crop prescriptions, farm economics/profit-loss analytics, and AI chat/LLM assistance. The offline Knowledge Assistant does not mean those separate capabilities are complete.

## Milestones and release status

- **M1–M5 foundations:** implemented and cloud-build verified on prior milestones; physical-device regression and migration/backup/reminder acceptance remain necessary.
- **M6 – Rajasthan advisory services:** live, source-attributed weather/alerts remain deferred.
- **M7 – Markets, schemes and services:** offline Government Schemes information is implemented; live mandi data and verified service directory remain open.
- **M8 – Offline Knowledge Assistant:** implemented on the active feature branch and build-verified. Farm economics calculations and any AI/LLM capability are separate future scope and are not included in this delivery.

See [QA_AUDIT.md](QA_AUDIT.md) for the static engineering audit, confirmed test coverage boundaries and prioritized release gaps. See [MILESTONE_TRACKER.md](MILESTONE_TRACKER.md) for the broader product scope and acceptance checklist.

## Plant Doctor model scope
The current model has 39 output classes, including a background class, and covers selected crop leaf classes. It is not a general plant species identifier. Indoor and ornamental plants currently have searchable care guides; broader photo-based identification is a later enhancement.

The model is downloaded from a pinned upstream revision during GitHub Actions and its Git blob hash is verified before the APK build. Attribution and the upstream MIT license are included in THIRD_PARTY_NOTICES.md and the app assets.

## Content and safety
- Agricultural, horticultural and veterinary content will be based on official and research-backed sources, localized by district/region, season and crop/species.
- AI output is advisory screening, not a confirmed diagnosis.
- Do not treat an AI result as a pesticide or veterinary prescription. Seek qualified local advice before treatment.
- Weather, live mandi prices and scheme updates must show their source and last-updated time; no live data is claimed until connected.
- Farmer records work offline by default. Optional sharing/sync must be explicit.
