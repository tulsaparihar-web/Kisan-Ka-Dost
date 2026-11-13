# Kisan Ka Dost – Milestone Audit and Delivery Tracker

**Product brand:** किसान का दोस्त (Kisan Ka Dost)  
**Tagline:** खेती • बागवानी • पशुपालन  
**Parent brand:** Parihar Digital Labs  
**Repository:** `tulsaparihar-web/Kisan-Ka-Dost` (kept unchanged)  
**Package ID:** `com.parihardigitallabs.kisankadost` (kept unchanged)  
**Implementation standard:** Kotlin, Jetpack Compose, Material 3, offline-first, Hindi + English, Rajasthan-first.

## Permanent product promise: free for everyone

Kisan Ka Dost will remain free to every user. There will be **no subscriptions, premium tiers, paid feature locks, or payment requirements** for essential or advanced app functionality. All three pillars—agriculture, horticulture/gardening and animal husbandry—remain accessible without payment.

Permitted sustainability sources:
- Responsible, limited, clearly identified advertising that does not interrupt essential tasks or disguise sponsored content as independent advice.
- Voluntary donations, with no pressure and no change in access for people who do not contribute.
- Institutional, CSR, university, NGO, cooperative and other mission-aligned support that does not restrict farmer access or compromise editorial independence.

Trust safeguards:
- Never sell or rent farmers' personal information, farm records, phone numbers, photos or precise location data to advertisers.
- Collect only data needed for a feature; keep personal/farm records on-device by default and make any sync/sharing explicit.
- No subscription prompts, paywalls, premium-only content, paid diagnosis, or paid access to core tools.
- Advertising must be clearly separated from agricultural/veterinary guidance; reject misleading, unsafe or predatory products and claims.
- Keep a low-cost/offline-first architecture; donations and advertising are uncertain and must not be assumed to cover operating costs.
- Any future revenue experiment must preserve this promise and be documented here before implementation.

This tracker is the delivery source of truth. A milestone is not complete merely because a screen exists: its agreed scope must be implemented, cloud-built, checked for regressions, packaged, and the resulting APK must be available. Device-only functions must additionally be labelled as awaiting real-device verification until tested on a phone.

## 1. Audit of completed and active milestones

| Milestone | Audit result | Implemented and verified | Remaining / boundary |
|---|---|---|---|
| **M1 – App foundation** | **Baseline complete** | Android app foundation; Compose/Material 3; home dashboard; navigation; Hindi/English switch persisted locally; basic settings; launcher branding; successful GitHub Actions APK builds. | Weather is a clearly identified placeholder, not live weather. Diary and Learn destinations remain placeholders. Perform device UX checks as part of regression testing. |
| **M2 – Farmer and farm setup** | **Baseline complete** | Offline Room-backed farmer profile; farmer name, mobile, village, tehsil, district; land area/unit; irrigation source; main crops and livestock; profile loads and saves locally. | Current form is a single basic farm profile. Multiple land parcels/fields, ownership/lease, soil test details, orchard/garden inventory and individual livestock records are not yet implemented. Add those in the milestone where they are operationally needed; do not imply they already exist. |
| **M3 – Agriculture, horticulture and gardening** | **Implementation complete; device acceptance pending** | Rajasthan bilingual field-crop guide (15 crops); offline Plant Doctor screening for selected leaf classes with confidence/top-three results and cautious care guidance; searchable horticulture/gardening library expanded to 37 bilingual entries; general Kharif/Rabi/Zaid calendar; soil-health, irrigation/water conservation, crop stages, introductory IPM and harvest/post-harvest guides; ten-zone broad crop-pattern reference. Combined app build and APK archive verified in [run 43210956086](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43210956086). Content references are recorded in [CONTENT_SOURCES_M3.md](CONTENT_SOURCES_M3.md). | Physical-device checks for Plant Doctor camera/gallery, image orientation/size, inference performance and install/language/layout regression remain pending. District-specific variety, seed-rate, spacing and exact sowing-window packages are deferred until current district/agro-zone-specific official Packages of Practices can be validated; no unsupported prescriptions are shipped. |
| **M4 – Animal husbandry** | **Closed for implementation and cloud verification; physical-device acceptance pending** | Bilingual animal-care library for cattle, buffalo, goats, sheep, camels and poultry; ICAR-NBAGR-aligned Rajasthan breed/home-tract references and qualitative breed notes; life-stage feeding, fodder conservation/storage and biosecurity education; offline animal profiles with species, breed, tag, sex, age/date of birth, purpose and notes; per-animal health-event history and veterinarian-confirmed next-date reminders via WorkManager; Android notification permission flow; automatic app-data backup disabled; non-destructive Room v1→v2→v3→v4 migrations preserving farmer and existing animal records. Unit tests and main build/packaging passed in [run 43383362324](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43383362324); APK artifact downloaded and inspected. | Physical-device v1→v4 migration, reminder/permission/reboot/Doze behavior and Hindi/English regression tests. Quantitative breed performance comparisons and individualized ration tables remain deferred until formal veterinary/animal-science review. No diagnosis, drug dosage or universal vaccination/deworming schedule. |
| **M5 – Farm, garden and livestock diaries** | **Implementation complete; final cloud unit tests and APK build passed; physical-device acceptance pending** | Bilingual searchable offline diary for field, garden, animal, expense, labour, input, production and sales/income; add/edit/delete/filter; date, related item, quantity/unit, amount, notes; optional local one-time reminders; up to five compressed private photos per entry; CSV export with spreadsheet-formula protection; full user-triggered ZIP v2 backup/restore for farmer profile, animal profiles, animal-health history, diary entries and photos (v1 import remains supported); non-destructive Room v4→v5→v6→v7 migrations; validated backup manifests/record relationships/photo dimensions; background photo import and thumbnail decoding; orphan-photo cleanup; reminder, photo, CSV and backup-validation unit tests. Core diary feature merged in [PR #3](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/pull/3); backup/photo/reminder hardening merged in [PR #4](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/pull/4). Latest hardening unit tests, APK build and artifact upload passed in [run 43411964102](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43411964102); Sales/photo/backup validation polish was merged in [PR #5](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/pull/5) as `bed5224474a76847809fdb0e9cb2cdcadc185e8d`. Final main-branch unit tests, APK build and artifact upload passed in [run 43415921222](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43415921222). | Physical-device v4→v5→v6→v7 migration with populated M4 data; photo selection/orientation/removal/delete/restart; full backup→restore including photos and M4 animal health; invalid archive rejection; notifications/permission/Doze; CSV; offline and Hindi/English layout regression. |
| **M6 – Rajasthan advisory services** | **Deferred** | Scope defined: verified local calendars, weather and alerts. | Do not start until core M3–M5 foundations are stable and this milestone is explicitly brought forward. Any forecast/advisory needs trusted source, location context, timestamp and offline/stale-data handling. |
| **M7 – Markets, schemes and services** | **Not started** | Scope defined in product blueprint. | Dated mandi rates with source/freshness; official scheme details and application links; verified KVK, veterinary, extension and service contacts. |
| **M8 – Farm economics and smart assistance** | **Not started** | Scope defined in product blueprint. | Cost of cultivation, enterprise expenses/revenue, simple profit/loss; bounded search/AI assistance grounded in verified content, with clear limitations. |

## 2. M3 completion checklist

M3 must not be closed until all applicable items below are delivered or explicitly recorded as a later approved enhancement.

### Field agriculture
- [x] Searchable bilingual starter crop guide (15 Rajasthan-relevant crops).
- [x] Starter profiles for 15 Rajasthan-relevant field crops with season, soil, sowing context, water, care/protection and harvest information.
- [ ] **Deferred content expansion:** locally validated variety, seed-rate, spacing and crop-duration tables by current district/agro-climatic zone; requires current official Packages of Practices and KVK review.
- [x] Bilingual general crop-stage guide implemented from field preparation/sowing through harvest; cloud-build verified in [run 43209315460](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43209315460); agronomic review and crop-specific depth remain open.
- [x] General bilingual Kharif, Rabi and Zaid seasonal crop calendar with month-by-month activities and clear local-validation caveat.
- [x] Broad reference to Rajasthan's ten agro-climatic zones and major Kharif/Rabi crop patterns added from the Government of Rajasthan Economic Review 2023–24.
- [ ] **Deferred content expansion:** precise district-/zone-validated sowing and harvest windows, including perennial calendars; must be checked against current district boundaries, weather and local university/KVK advisories.
- [x] Bilingual foundational soil-health guide: soil sampling/testing, pH/EC/salinity, organic carbon, major/micronutrients, organic matter and responsible fertilizer use. General education only; personalized rates and soil-report interpretation remain out of scope until reviewed locally.
- [x] Irrigation and water-conservation bilingual guide implemented (planning, irrigation methods, drip/sprinkler, mulching, rainwater capture, soil moisture and water quality); cloud-build verified in [run 43209315460](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43209315460); content review and crop-specific recommendations remain open.
- [x] Bilingual introductory crop protection/IPM guide implemented (scouting, prevention, beneficial insects, symptom assessment and pesticide-label safety); cloud-build verified in [run 43209315460](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43209315460); detailed crop/pest-specific reviewed library remains open.
- [x] Bilingual harvest, safe threshing, drying/moisture, storage hygiene and horticultural post-harvest basics.

### Horticulture and gardening
- [x] Bilingual searchable guide with category filters and expandable growing/care information (37 entries).
- [x] General fruit crop guides expanded; orchard site assessment, planting-material selection, orchard layout, drainage and establishment starter guide added. Detailed species-specific spacing/pit/nutrition schedules remain deferred pending local validation.
- [x] Seasonal vegetable starter guides expanded for tomato, chilli, okra, brinjal, leafy vegetables, cucumber, gourds, onion/garlic, root vegetables and cole crops. Crop-specific spacing and nutrient schedules remain subject to local validation.
- [x] Flower starter guides expanded to include marigold, rose, jasmine, hibiscus, chrysanthemum and tuberose; detailed commercial packages remain subject to local validation.
- [x] Kitchen garden and terrace/grow-bag starter guides implemented; expanded crop-specific plans and household herb detail remain open.
- [x] Indoor/decorative plant care and basic troubleshooting guide included.
- [x] Starter guides for nursery/seedlings, propagation/cuttings, fruit-tree pruning and protected cultivation implemented; locally reviewed crop-specific procedures remain open.
- [x] Protected cultivation starter guide included with site, structure, ventilation, water and investment cautions.
- [x] M3 content source register added with ICAR, Rajasthan Agriculture Department, Rajasthan Agricultural University, National Horticulture Board and Government of Rajasthan references; content is labelled as general education where local prescriptions are not verified.

### M4 – Animal husbandry foundation
- [x] Bilingual searchable starter library for cattle, buffalo, goats, sheep, camels and poultry.
- [x] Rajasthan-relevant breed names and concise home-tract/profile notes based on ICAR-NBAGR, KVK and Government of Rajasthan references.
- [x] Qualitative feeding and water education, including newborn colostrum, life-stage considerations, safe fodder conservation/storage and biosecurity.
- [x] Urgent warning signs direct owners to a qualified veterinarian; no diagnosis, medicine or drug dosage.
- [x] Offline animal profiles support add/list/edit/delete, including species, breed, tag, sex, age/date of birth, purpose and notes.
- [x] Per-animal offline health-event history with event date, notes and optional veterinarian-confirmed next date.
- [x] Veterinarian-confirmed next-date reminders scheduled through WorkManager around 9:00 AM local time; notification permission is requested only when a reminder is saved, and reminders can be re-enqueued after permission is later granted. Android battery management may delay delivery.
- [x] Non-destructive Room migrations v1→v2→v3→v4 preserve existing farmer profiles and animal records.
- [x] Android automatic app-data backup disabled so farm and animal records remain local by default; explicit export/backup is a separate future user-controlled feature.
- [x] Unit tests for reminder time calculation and cloud build/APK packaging passed in [run 43383362324](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43383362324); APK artifact downloaded and inspected.
- [ ] Physical-device upgrade test from a populated M3 database through Room v1→v4.
- [ ] Physical-device reminder permission, reboot/Doze, delivery and Hindi/English regression tests.
- [ ] Quantitative breed performance comparisons and individualized ration tables remain deferred until formal animal-science/veterinary review.

### Plant Doctor and quality
- [x] Camera/gallery image selection flow and offline model integration.
- [x] Supported-class/confidence-oriented result presentation and cautious guidance.
- [ ] Verify image permissions, camera capture, gallery selection, image orientation/size and inference performance on physical Android devices.
- [ ] Test supported examples, unsupported plants, low-confidence images and unclear photos.
- [x] UI presents the result as an AI-assisted preliminary indication, flags low confidence/background results and limits supported-class claims.
- [x] Safe first-step guidance directs users to local expert confirmation; no automatic pesticide prescription.

## 3. M5 – Farm, garden and livestock diaries

### Delivered implementation
- [x] Separate Room diary table and non-destructive migrations v4→v5 (diary), v5→v6 (photo paths) and v6→v7 (one-time reminder delivery state).
- [x] Bilingual offline diary screen connected to the existing navigation.
- [x] Field, garden/orchard, animal, expense, labour, input, production and sales/income categories.
- [x] Add, edit, delete, category filter and local text search.
- [x] Date, related crop/plant/animal, quantity, unit, amount, notes and optional reminder date.
- [x] Local one-time WorkManager reminders around 9 AM; successful delivery state is persisted to prevent repeats on screen reopen. If notifications are disabled, the reminder remains pending for rescheduling after permission is enabled. Android may delay delivery.
- [x] Up to five private photo attachments per diary entry; images are downsampled, EXIF orientation corrected and recompressed to remove original metadata, including embedded GPS.
- [x] Photo files stored in the app's private files directory; automatic Android app-data backup remains disabled.
- [x] User-triggered CSV export, including photo counts and protection against spreadsheet formula injection.
- [x] User-triggered ZIP backup v2 and restore covering farmer profile, animals, animal health history, diary entries and attached photos; v1 backups remain importable. Restore bounds archive/photo sizes, validates entries, record IDs, dates, numeric values, photo manifests and animal-health relationships, stages data before changing the database, and applies replacement in a Room transaction.
- [x] Backup/restore remains local and user-selected; no account, sync service or background upload.
- [x] Unit tests for reminder scheduling, image downsampling/path validation, CSV escaping, backup-version compatibility, record relationships, duplicate IDs, and exact photo-manifest validation.
- [x] Latest hardening unit tests, APK build and artifact upload passed in [run 43411964102](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43411964102); final Sales/photo/backup validation polish merged in [PR #5](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/pull/5). Main-branch unit tests, APK build and artifact upload passed in [run 43415921222](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43415921222); APK downloaded and inspected.
- [ ] Physical-device v4→v5→v6→v7 migration test with populated M4 data.
- [ ] Physical-device photo selection, EXIF orientation, photo removal and deletion tests.
- [ ] Physical-device full backup→restore test, verifying farmer profile, animals, health history, diary entries and photos; test invalid/corrupt backup rejection.
- [ ] Physical-device notification permission, reboot/Doze, offline, CSV and Hindi/English regression tests.

## 4. Forward delivery sequence

1. **M3 implementation delivered**: bilingual guides, source register and APK are cloud-build verified. Physical-device Plant Doctor camera/gallery and regression checks remain an acceptance gate; detailed district-specific crop packages remain a future validated content expansion.
2. **M4 implementation and cloud verification complete**: animal profiles, health history, breed/care library and local reminders are delivered. Physical-device migration and reminder acceptance remains open.
3. **M5 implementation and cloud verification complete**: offline diaries, sales/income, photos, CSV, reminders and full user-controlled ZIP backup/restore are delivered. Physical-device migration, backup/restore and UX acceptance remains open.
4. **M6 – Rajasthan advisory services (deferred)**: do not start until explicitly reactivated; integrate trusted data sources and show timestamps/freshness.
5. **M7 – Markets, schemes and services (not started)**: verified sources, dated information and official links.
6. **M8 – Farm economics and smart assistance (not started)**: calculations first, then carefully bounded retrieval/AI features grounded in the app's verified content.

## 5. Engineering and release gates for every milestone

- Preserve Kotlin + Jetpack Compose + Material 3 and the existing application package ID.
- Keep repository name `Kisan-Ka-Dost`; public app branding is **किसान का दोस्त** with tagline **खेती • बागवानी • पशुपालन**.
- Maintain Hindi and English for all user-facing flows, including validation, empty, error and permission states.
- Offline-first: local features must remain usable without connectivity. Never invent live weather, market prices, scheme status or advisories.
- For Room changes, add explicit non-destructive migrations; never use destructive migration as a shortcut.
- **Free-access gate:** reject subscriptions, premium tiers, paywalls and paid feature restrictions; no sale/rental of personal or farm data; ads must be limited, transparent, non-disruptive and screened for safety; donations remain voluntary.
- Prefer small, testable commits. Review the diff and check route wiring, state handling, accessibility, screen overflow and error/empty states.
- Build through GitHub Actions (JDK 17 / configured Android SDK). Inspect failed logs and fix before proceeding.
- Confirm the workflow run corresponds to the latest intended source commit; ensure artifact exists, download and inspect the APK archive.
- Provide the installable APK and build link after success. Do not mark a milestone complete on source commit or a green build alone if device-specific behaviour remains untested.
- Record what is build-verified versus what still needs physical-device testing.
- Keep user data, privacy and permission use minimal and transparent.

## 5. Scope control

The three equal product pillars are **खेती**, **बागवानी**, and **पशुपालन**. Shared farm records, advisory services, markets, schemes and economics support those pillars. Do not skip a pillar or move to the next milestone simply because a basic screen has been added. When scope must be deferred, document it here and preserve it in the forward plan.
