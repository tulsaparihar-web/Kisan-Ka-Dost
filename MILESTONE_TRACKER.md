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
| **M4 – Animal husbandry** | **In progress; first expanded M4 release build-verified** | Bilingual animal-care starter library for cattle, buffalo, goats, sheep, camels and poultry; Rajasthan-relevant breed names with clarified buffalo home-tract context; qualitative age-/production-stage feeding and biosecurity education; urgent veterinary warning signs; private on-device animal registry with add/list/edit/delete; offline per-animal health-event history with event date, notes and optional veterinarian-confirmed next date; non-destructive Room migrations v1→v2→v3 preserving existing farmer-profile data. Main-branch APK build passed in [run 43381264104](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43381264104); artifact downloaded and inspected. | Expert-reviewed breed traits/production-system comparisons; validated species/life-stage fodder and ration depth; Android notification scheduling for veterinarian-confirmed dates; physical-device upgrade/migration and bilingual regression tests. Health dates are stored/displayed only; no phone notification is scheduled yet. No diagnosis, drug dosage or treatment prescription. |
| **M5 – Farm, garden and livestock diaries** | **Not started** | Requirements defined in PRODUCT_SCOPE.md. | Field/crop activity diary; orchard/garden logs; animal health/production records; expense/labour/input records; reminders; offline persistence and export/backup plan. |
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

### M4 – Initial animal husbandry delivery
- [x] Bilingual searchable starter library for cattle, buffalo, goats, sheep, camels and poultry.
- [x] Rajasthan-relevant recognized breed names included with clear expert-verification caveat.
- [x] General feed/water, housing/comfort and preventive-care education included.
- [x] Urgent warning signs direct owners to a qualified veterinarian; no diagnosis or drug dosage.
- [x] M4 source register created from ICAR-NBAGR, Government of Rajasthan, DAHD, NDDB and ICAR references.
- [x] First animal husbandry screen cloud-build verified in [run 43211738560](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43211738560); APK archive downloaded and inspected.
- [ ] Detailed breed profiles and production-system comparisons reviewed by animal husbandry experts.\n- [x] Expanded qualitative age-/production-stage feeding and biosecurity starter guidance; no unverified quantities or treatment instructions are provided.\n- [x] Breed list wording distinguishes nationally registered buffalo examples from Rajasthan home-tract breeds and displays buffalo home-tract context.
- [ ] Species- and life-stage-specific fodder/ration guides validated by veterinary nutrition sources.
- [x] Offline health-event log per animal, with event date, optional veterinarian-confirmed next date, notes and delete; invalid dates are rejected and next dates cannot precede the event. This stores/displays dates but does not yet schedule Android notifications.\n- [ ] Android local notification scheduling for user-entered, veterinarian-confirmed dates.
- [x] Offline animal registry (add/list/edit/delete), health-event records and non-destructive Room 1→2→3 migrations implemented; main build passed in [run 43381264104](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43381264104); physical-device migration verification remains pending.\n- [ ] Edit animal records; health/production history and veterinarian-confirmed reminders.
- [ ] Physical-device and bilingual regression testing.

### Plant Doctor and quality
- [x] Camera/gallery image selection flow and offline model integration.
- [x] Supported-class/confidence-oriented result presentation and cautious guidance.
- [ ] Verify image permissions, camera capture, gallery selection, image orientation/size and inference performance on physical Android devices.
- [ ] Test supported examples, unsupported plants, low-confidence images and unclear photos.
- [x] UI presents the result as an AI-assisted preliminary indication, flags low confidence/background results and limits supported-class claims.
- [x] Safe first-step guidance directs users to local expert confirmation; no automatic pesticide prescription.

## 3. Forward delivery sequence

1. **M3 implementation delivered**: bilingual guides, source register and APK are cloud-build verified. Physical-device camera/gallery and regression checks remain a user acceptance gate before calling the M3 release fully signed off. Detailed district-specific crop packages are a documented future content expansion; no unverified prescriptions are shipped.
2. **M4 – Animal husbandry (next active milestone)**: deliver species/breed knowledge first, then feeding/housing/preventive-care education and offline animal profiles. Add database migrations without deleting existing farmer data.
3. **M5 – Diaries**: implement reusable offline records for fields, gardens, animals, expenses and production, plus practical reminders. Reuse M2 farmer/farm context.
4. **M6 – Rajasthan advisory services (deferred)**: only proceed when deliberately reactivated; integrate trusted data sources and show timestamps/freshness.
5. **M7 – Markets, schemes and services**: verified sources, dated information and official links.
6. **M8 – Farm economics and smart assistance**: calculations first, then carefully bounded retrieval/AI features grounded in the app's verified content.

## 4. Engineering and release gates for every milestone

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
