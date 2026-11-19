# M6 – Rajasthan Advisory Services: Architecture and Source Review

**Status:** Architecture baseline; implementation not yet started  
**Branch:** `feature/m6-ux-plant-search-tools`  
**Product constraints:** Hindi + English, Rajasthan-first, offline-first, free for everyone, no sale/rental of farmer data.

## 1. Scope and delivery order

1. District-aware advisory home using the farmer's saved district (with manual district selection when profile location is blank).
2. Official weather observations, forecast and district warnings.
3. Agrometeorological crop advisories with issue date, validity and source.
4. Seasonal crop calendar enhancements only where current district/zone recommendations are supported by authoritative sources.
5. Offline cache with explicit freshness labels; no stale data presented as current.

## 2. Source review

### Primary weather source: India Meteorological Department (IMD)

Official API reference: https://api.imd.gov.in/public/api_reference.html

The reference documents:
- City forecast up to 7 days, including forecast max/min temperatures and daily forecast text.
- Current weather observations, including observation date/time, temperature, humidity, wind and rainfall.
- District-wise nowcast, including consolidated message, issue time and validity.
- District-wise warnings for up to five days, including heavy rain, thunderstorm/lightning, hail, dust storm, strong winds, heat wave, hot day, cold wave, frost and fog.
- District rainfall and agrometeorological advisory endpoints.

The API portal is https://api.imd.gov.in/public/. Before shipping a direct integration, verify registration, access terms, authentication/token requirements, rate limits and production use conditions with IMD. Do not embed a private API credential in the Android APK. If credentials are required, use a small server-side proxy with secrets held server-side, or obtain an approved public access method.

### Agricultural advisory sources

- IMD Agromet Services / District Bulletin: https://imdagrimet.gov.in/Services/DistrictBulletin.php
- Rajasthan agricultural university and KVK publications; select recommendations by district/agro-climatic zone and publication date.
- Government of Rajasthan Agriculture Department publications and contingency crop plans.
- Rajasthan agro-climatic-zone references provide broad regional crop patterns, not exact current district sowing dates.

A live district bulletin is preferable to manually converting generic seasonal text into an advisory. Every advisory displayed must retain its issuing institution, district, issue date and validity/update time. If the source does not supply a field, the app must not invent it.

### Source limitations identified

- IMD's API reference documents endpoints and field meanings, but API access conditions must be confirmed before integration.
- A general crop calendar cannot be represented as a district-specific prescription.
- Third-party aggregators are not authoritative sources for farmer-facing recommendations.
- Weather forecasts and agromet advisories are time-sensitive; cache expiry and stale-data labels are mandatory.

## 3. Proposed Android architecture

Keep the existing Kotlin + Jetpack Compose + Material 3 architecture.

- `advisory/data`: DTOs, source adapters, district mapping and repository.
- `advisory/domain`: normalized weather, warning and crop-advisory models; freshness rules.
- `advisory/ui`: bilingual advisory dashboard and detail screens.
- Existing Room database: add cache entities only after source payload and retention requirements are confirmed; use explicit non-destructive migrations.
- Existing WorkManager dependency: optional periodic refresh, subject to network availability and Android scheduling limits.

The UI must distinguish:
- **Live/current:** fetched successfully and within source validity.
- **Cached:** previously fetched; show exact last-updated time.
- **Expired/offline:** do not present as current; show a clear unavailable/stale message.
- **No official advisory:** say no advisory was available from the selected source; do not generate one.

## 4. Privacy and location

- Use the farmer's already saved district by default.
- Allow manual district selection without requesting GPS.
- Do not request precise location for a district-level service.
- If GPS is ever introduced for a genuinely location-specific service, make it optional, explain why, and avoid storing or sharing coordinates unnecessarily.

## 5. Acceptance gates

- Hindi and English labels, warnings, loading, empty, error and stale states.
- Correct district mapping and handling of renamed/new districts.
- Source name, issue/observation time, validity and last-updated time visible.
- No forecast/advisory fabricated on network failure or missing data.
- Unit tests for parsing, date/time handling, warning severity mapping, freshness and district matching.
- Existing Room data preserved by explicit migrations.
- GitHub Actions unit tests + debug APK build; APK artifact downloaded and inspected.
- Physical-device checks for online/offline transitions, refresh, language switching and screen layout.

## 6. Immediate implementation gate

Before connecting IMD live endpoints, confirm the approved access/authentication route and terms. Keep credentials out of source control and the APK. Until then, implement only source-independent models, parsing tests and UI states; do not ship mocked or hard-coded weather as live data.
