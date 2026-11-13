# किसान का दोस्त – Kisan Ka Dost

**Parihar Digital Labs**  
**Tagline:** खेती • बागवानी • पशुपालन  
Hindi + English | Rajasthan-first | Offline-first | Free for everyone

Kisan Ka Dost is a practical digital companion for farmers and rural households. It brings together **field agriculture, horticulture and animal husbandry**, with offline farm records, learning guides, plant screening, animal care and a farm diary.

## Our promise

- Free for everyone: no subscriptions, premium tiers, paywalls or paid feature locks.
- Farmer and farm records stay on the device by default. Export and restore are user-controlled.
- No sale or rental of personal information, farm records, photos, phone numbers or precise location data.
- Agricultural and veterinary guidance must be grounded in reliable sources and must not replace local experts.
- Live weather, mandi rates, scheme status and advisories must show their source and last-updated time. Never display invented or stale information as current.

## Project details

- App display name: **किसान का दोस्त**
- English name: **Kisan Ka Dost**
- Parent brand: **Parihar Digital Labs**
- Package: `com.parihardigitallabs.kisankadost`
- Kotlin + Jetpack Compose + Material 3
- MVVM + Repository pattern
- Room database for local records
- TensorFlow Lite for bounded offline leaf-image screening
- Minimum Android: API 26 (Android 8.0)
- Compile/Target SDK: 35
- Java/JVM target: 17

## Implemented foundations

- Bilingual Hindi/English interface with saved language preference.
- Farmer profile and basic farm details stored locally.
- Rajasthan-focused bilingual crop, horticulture, soil, irrigation, crop-stage, IPM and harvest learning content.
- Offline Plant Doctor leaf screening for supported crop classes, with top-three results, cautious guidance and local diagnosis history.
- Animal-care library, animal profiles and veterinarian-confirmed health-event reminders.
- Offline farm/garden/livestock diary with add/edit/delete, search, categories, private photo attachments, reminders and CSV export.
- User-selected ZIP backup and restore for farmer, animal, animal-health, diary and Plant Doctor records/photos, with legacy backup compatibility.
- Professional dashboard and Learning Hub work is in progress in [draft PR #7](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/pull/7).

## Delivery status

| Area | Status |
|---|---|
| M1 – App foundation | Implemented; cloud builds available |
| M2 – Farmer profile | Implemented; single basic farm profile |
| M3 – Agriculture and horticulture foundation | Implemented; physical-device acceptance pending |
| M4 – Animal husbandry foundation | Implemented; physical-device acceptance pending |
| M5 – Farm and livestock diary | Implemented; physical-device acceptance pending |
| Professional UI refresh | In progress – draft PR #7 |
| M6 – Rajasthan advisory services | Reactivated; not yet implemented |
| M7 – Markets, schemes and local services | Reactivated; not yet implemented |
| M8 – Farm economics and smart assistance | Reactivated; not yet implemented |

The complete audit, known limitations and device acceptance checklist are in [MILESTONE_TRACKER.md](MILESTONE_TRACKER.md). The product blueprint is in [PRODUCT_SCOPE.md](PRODUCT_SCOPE.md).

## Build APK using GitHub Actions

1. Open the [Actions page](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions).
2. Open the latest successful **Android APK Build** run for the intended commit.
3. Download the `Kisan-Ka-Dost-debug-apk` artifact.
4. Extract the ZIP and install `app-debug.apk` on an Android phone.

Always back up existing app data before installing a build that may have a different debug signing key. Do not uninstall an existing installation until the backup is confirmed.

## Plant Doctor model scope

The current model has 39 output classes including a background class, covering selected leaf classes across 14 crop types. It is not a general plant identifier and has not been established as a definitive diagnostic model for Rajasthan field conditions. Model scores are not calibrated disease probabilities. Results are screening hints only; the app must not prescribe pesticides or dosages.

## Content and safety

- Prefer ICAR, Rajasthan agricultural universities, KVKs and official state/central government sources.
- Distinguish general education from locally validated recommendations.
- Do not publish unsupported universal sowing dates, seed rates, fertilizer quantities, pesticide doses, veterinary medicines or vaccination schedules.
- For serious crop or animal health issues, direct farmers to a qualified local expert.
- Keep network-fed information timestamped and clearly label cached data when offline.
