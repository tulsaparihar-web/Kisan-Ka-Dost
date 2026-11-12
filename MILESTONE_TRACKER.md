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
| **M3 – Agriculture, horticulture and gardening** | **In progress** | Rajasthan field-crop guide with 15 crops; Plant Doctor camera/gallery entry and offline TensorFlow Lite screening for selected crop-leaf classes; confidence/top-three results and cautious care guidance; bilingual searchable horticulture/gardening guide with 19 entries across fruit, vegetables, flowers, herbs and indoor/ornamental plants; general Kharif/Rabi/Zaid seasonal calendar with month-by-month activities. Crop calendar build succeeded in [run 43207410819](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43207410819); bilingual foundational soil-health/nutrient guide is now implemented and queued for build verification. | Crop profiles/stages, soil and water, nutrient/IPM depth, validated local calendars, complete fruit/vegetable/flower production guides, nursery, propagation, orchard, protected cultivation, terrace/kitchen garden; source review; Plant Doctor device checks remain open. |
| **M4 – Animal husbandry** | **Not started** | Product requirements defined in PRODUCT_SCOPE.md. | Species/breed library; feeding/fodder/water; housing/hygiene; preventive-care education; animal profiles and records. No diagnosis, drug dosage or treatment prescription. |
| **M5 – Farm, garden and livestock diaries** | **Not started** | Requirements defined in PRODUCT_SCOPE.md. | Field/crop activity diary; orchard/garden logs; animal health/production records; expense/labour/input records; reminders; offline persistence and export/backup plan. |
| **M6 – Rajasthan advisory services** | **Deferred** | Scope defined: verified local calendars, weather and alerts. | Do not start until core M3–M5 foundations are stable and this milestone is explicitly brought forward. Any forecast/advisory needs trusted source, location context, timestamp and offline/stale-data handling. |
| **M7 – Markets, schemes and services** | **Not started** | Scope defined in product blueprint. | Dated mandi rates with source/freshness; official scheme details and application links; verified KVK, veterinary, extension and service contacts. |
| **M8 – Farm economics and smart assistance** | **Not started** | Scope defined in product blueprint. | Cost of cultivation, enterprise expenses/revenue, simple profit/loss; bounded search/AI assistance grounded in verified content, with clear limitations. |

## 2. M3 completion checklist

M3 must not be closed until all applicable items below are delivered or explicitly recorded as a later approved enhancement.

### Field agriculture
- [x] Searchable bilingual starter crop guide (15 Rajasthan-relevant crops).
- [ ] Crop profiles expanded with locally validated varieties, seed rate, spacing, crop duration and district/season context.
- [ ] Crop-stage guidance from land preparation through harvest.
- [x] General bilingual Kharif, Rabi and Zaid seasonal crop calendar with month-by-month activities and clear local-validation caveat.
- [ ] District-/agro-climatic-zone-validated sowing and harvest windows, including perennial crop calendars.
- [x] Bilingual foundational soil-health guide: soil sampling/testing, pH/EC/salinity, organic carbon, major/micronutrients, organic matter and responsible fertilizer use. General education only; personalized rates and soil-report interpretation remain out of scope until reviewed locally.
- [ ] Irrigation, water conservation, drip/sprinkler and moisture-management basics.
- [ ] Pest/disease/deficiency library and IPM, safe handling and label-reading guidance.
- [ ] Harvest, drying, storage and post-harvest basics.

### Horticulture and gardening
- [x] Bilingual searchable guide with category filters and expandable growing/care information (19 entries).
- [ ] Expand fruit cultivation into complete orchard establishment and management guides.
- [ ] Expand seasonal vegetable production, spacing, nutrition, irrigation and harvest guidance.
- [ ] Expand flower and ornamental cultivation guides.
- [ ] Kitchen garden, terrace/container/grow-bag and household herb modules.
- [ ] Indoor/decorative plant care and troubleshooting.
- [ ] Nursery, seedling production, grafting/budding, propagation, pruning and canopy management.
- [ ] Protected cultivation basics where appropriate for Rajasthan.
- [ ] Content review against reliable agriculture/horticulture sources, with geography/season/source labels.

### Plant Doctor and quality
- [x] Camera/gallery image selection flow and offline model integration.
- [x] Supported-class/confidence-oriented result presentation and cautious guidance.
- [ ] Verify image permissions, camera capture, gallery selection, image orientation/size and inference performance on physical Android devices.
- [ ] Test supported examples, unsupported plants, low-confidence images and unclear photos.
- [ ] Ensure the UI never presents model output as a confirmed diagnosis or universal plant identification.
- [ ] Provide expert escalation and safe next steps; no automatic pesticide prescription.

## 3. Forward delivery sequence

1. **Finish M3**: close remaining field agriculture and horticulture/gardening requirements; test and publish a verified APK.
2. **M4 – Animal husbandry**: deliver species/breed knowledge first, then feeding/housing/preventive-care content and offline animal profiles. Add database migrations without deleting existing farmer data.
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
