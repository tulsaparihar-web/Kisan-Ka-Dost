# Kisan Ka Dost – Product Scope and Feature Blueprint

**Brand:** Parihar Digital Labs  
**Primary audience:** Farmers and rural households in Rajasthan, with an architecture that can expand across India  
**Languages:** Hindi and English  
**Platform:** Android (Kotlin, Jetpack Compose, Material 3)  
**Operating principle:** Free for everyone, offline-first; useful without an account or continuous internet

## Permanent promise: free for everyone

Kisan Ka Dost will remain free to every user. There will be no subscriptions, premium tiers, paywalls or paid feature restrictions. All three pillars—agriculture, horticulture/gardening and animal husbandry—will remain accessible without payment.

The project may sustain itself through responsible, limited and clearly identified advertising; voluntary donations; and mission-aligned institutional, CSR, university, NGO or cooperative support. Donations never affect access. Advertising must not interrupt essential tasks, imitate independent advice, or promote misleading/unsafe products. Farmer personal information, farm records, photos, phone numbers and precise location data must never be sold or rented to advertisers. Personal/farm records remain on-device by default; any sharing or sync must be explicit. Keep infrastructure lean and do not assume advertising or donations guarantee operating-cost coverage.


## Product vision

Kisan Ka Dost is an all-round farm and rural livelihood companion. It must cover the complete practical cycle of **कृषि (खेती), बागवानी and पशुपालन**, not only plant identification. The app should help a farmer plan, learn, record, monitor and make better-informed decisions across crops, horticulture, animals and farm economics.

The three pillars have equal product importance:

1. **कृषि / खेती – Field agriculture**
2. **बागवानी – Horticulture and plant care**
3. **पशुपालन – Livestock and allied animal care**

Supporting services (farm diary, soil, water, weather, markets, schemes, machinery and farm economics) connect these pillars.

---

## 1. कृषि / खेती – Field agriculture

### 1.1 Crop knowledge library
- Rajasthan-relevant crops: bajra, wheat, mustard, gram, barley, guar, moong, moth bean, groundnut, cotton, cumin, coriander, fenugreek, isabgol and other locally important crops.
- Crop profile: suitable agro-climatic zone, soil, season, varieties, seed rate, sowing window, spacing, crop duration and expected harvest period.
- Crop-stage guidance from land preparation and sowing through vegetative growth, flowering, grain/fruit formation and harvest.
- Intercropping, crop rotation, mixed cropping and green-manure guidance.
- Rainfed, irrigated, dryland and protected cultivation practices.

### 1.2 Crop calendar and planning
- Kharif, Rabi, Zaid and perennial crop calendars.
- District- and agro-climatic-zone-aware sowing and harvest windows.
- Crop reminders: land preparation, sowing, irrigation, weeding, nutrient application and harvest.
- Seasonal planning across available land and irrigation resources.
- Guidance must distinguish a general calendar from a locally validated recommendation.

### 1.3 Soil, nutrients and water
- Soil type, soil testing and interpretation of soil-health reports.
- pH, salinity/alkalinity, organic carbon and major nutrient concepts.
- Balanced nutrient management and organic matter improvement.
- Compost, farmyard manure, vermicompost, green manure and biofertilizer education.
- Irrigation methods, critical crop stages, water-use efficiency, drip and sprinkler systems.
- Rainwater harvesting, farm ponds, mulching and moisture conservation.
- Rajasthan-specific dryland farming, drought preparedness and saline-water considerations.

### 1.4 Crop protection and plant health
- Pest, disease, nutrient-deficiency and abiotic-stress learning library.
- Symptom descriptions, affected crop/stage, prevention, monitoring and integrated pest management (IPM).
- Safe handling, protective equipment, label reading, pre-harvest interval and responsible pesticide use.
- Photo-based screening through Plant Doctor where a supported model exists.
- AI results must show confidence and supported crop scope; never present a prediction as a confirmed diagnosis.
- No automatic chemical prescription from a photo. Treatment choices must be checked against current local agricultural recommendations and product labels.

### 1.5 Harvest and post-harvest
- Harvest maturity indicators and timing.
- Drying, grading, cleaning, storage, moisture and pest prevention.
- Post-harvest handling, value addition and primary processing.
- Crop loss prevention and quality basics.

---

## 2. बागवानी – Horticulture

### 2.1 Horticulture knowledge library
- **Fruits:** locally relevant options such as ber, pomegranate, date palm, kinnow/citrus, guava, aonla, mango, papaya and other suitable fruit crops.
- **Vegetables:** tomato, chilli, brinjal, okra, cucurbits, onion, garlic, leafy vegetables and seasonal vegetables.
- **Flowers and ornamentals:** rose, marigold, jasmine, chrysanthemum, bougainvillea and common home-garden plants.
- **Spices and medicinal/aromatic plants:** cumin, coriander, fenugreek, ajwain, isabgol, tulsi, aloe vera and lemongrass.
- Kitchen gardens, nutrition gardens, nursery plants and landscaping basics.

### 2.2 Orchard and garden management
- Site selection, planting season, pit preparation, spacing and planting material.
- Nursery management, seed propagation, grafting, budding and vegetative propagation.
- Training, pruning, canopy management, flowering and fruit-set care.
- Irrigation, fertigation, mulching, weed management and orchard floor management.
- Fruit thinning, harvesting, grading, storage and handling.
- Protected cultivation: polyhouse, shade-net, low tunnels and nursery structures where suitable.
- Container gardening, terrace gardening and home-plant care.

### 2.3 Horticulture Plant Doctor
- Camera and gallery-based plant/leaf photo capture.
- Identify supported crop/plant classes and show model confidence.
- Distinguish supported diagnosis classes from unsupported plants or unclear photos.
- Show likely symptoms, preventive practices and safe next steps.
- Maintain a searchable plant-care guide even when the AI model cannot identify a plant.
- Add broader species recognition only after suitable model quality, licensing and offline-device performance are verified.

---

## 3. पशुपालन – Livestock and animal husbandry

### 3.1 Species and breed library
- Cattle and indigenous breeds relevant to Rajasthan, including Rathi, Tharparkar, Kankrej, Nagori, Mewati, Nari and Sanchori.
- Buffalo management and nationally registered breeds with home-tract context; do not imply that breeds from other states are Rajasthan-native.
- Goat breeds and systems, including Sirohi, Jakhrana, Marwari, Sojat, Karauli, Gujari, Totapuri and Battisi.
- Sheep breeds and wool/meat management, including Chokla, Nali, Marwari, Magra, Malpura, Jaisalmeri, Pugal, Sonadi and Kheri; distinguish Avishaan as a synthetic breed.
- Camel husbandry and Rajasthan-associated Bikaneri, Jaisalmeri, Jalori, Marwari, Mewari and Mewati breeds.
- Poultry: backyard poultry, layers and broilers; identify registered local breeds such as Mewari where relevant.
- Allied modules can later include beekeeping, fisheries, piggery and rabbit rearing where locally relevant.

### 3.2 Feeding and nutrition
- Green fodder, dry fodder, crop residues, silage and fodder conservation.
- Balanced ration concepts by species, age and production stage.
- Clean drinking water, mineral mixture and salt education.
- Calf, lamb, kid and chick feeding basics.
- Fodder crop planning and seasonal fodder availability.
- Any ration calculator must disclose assumptions and encourage veterinary/nutritionist validation for special cases.

### 3.3 Health, prevention and veterinary support
- Species-wise preventive-care education and user-entered health-event history.
- User-entered next-date reminders only when confirmed by a veterinarian; any published vaccination/deworming schedule must be sourced and reviewed by veterinary authorities.
- Common signs of illness, heat stress, dehydration, parasites and injury.
- Clean housing, ventilation, hygiene, biosecurity and isolation of sick animals.
- First-response guidance and nearby veterinary-contact information when available.
- Emergency warning signs and clear instruction to contact a qualified veterinarian promptly.
- The app must not claim to diagnose disease, prescribe medicines, set drug dosages or replace a veterinarian.

### 3.4 Breeding, dairy and production
- Heat detection and breeding record.
- Pregnancy, calving, lambing and kidding records.
- Newborn care and colostrum education.
- Lactation, milking hygiene, milk yield and quality records.
- Dry-period and reproductive-cycle tracking.
- Offline individual animal profile: name, species, identification tag, sex, age/date of birth, purpose and notes; breed, purchase details and photographs are planned extensions.
- Health-event history with event date, notes and optional veterinarian-confirmed next date; local Android notification attempt around 9 AM, subject to permission and battery-management delays.
- Production and expense records for milk, eggs, wool and other outputs remain in the later diary milestone.

### 3.5 Animal and herd diary
- Individual animal profile and herd register.
- Vaccination, deworming, breeding, treatment and veterinary-visit history.
- Milk/egg/wool production log.
- Feed purchase and consumption record.
- Reminders for due care and follow-up.
- Offline storage with export/backup options planned.

---

## 4. Shared farm management features

### 4.1 My Farm profile
- Farmer and farm location: state, district, tehsil, village.
- Land parcels, area and unit, ownership/lease status if the farmer chooses to record it.
- Soil type, water source, irrigation method and farm infrastructure.
- Crop and horticulture inventory.
- Livestock and allied-enterprise inventory.
- Farm preferences and local language.

### 4.2 Farm diary and records
- Field-wise crop diary and activity log.
- Planting, irrigation, fertilizer, pest observation, spray and harvest records.
- Orchard, kitchen-garden and nursery diary.
- Animal and herd diary.
- Expense, labour, input and sales records.
- Attach photos and notes; work offline.
- Optional export to PDF/CSV and user-controlled backup in a later milestone.

### 4.3 Weather and alerts
- Location-aware current weather and forecast when a reliable service is connected.
- Rain, heatwave, cold wave, frost, wind and severe-weather alerts.
- Crop-stage-aware advisory only when location and crop context are sufficient.
- Clear timestamp and source; cached last update must be labelled as stale/offline.
- Never fabricate a forecast when offline.

### 4.4 Mandi and market information
- Market/mandi price lookup by commodity and market.
- Price date, unit, source and freshness clearly visible.
- Nearby mandi discovery when location permission is granted.
- Historical trends and sale/expense records as later enhancements.
- Do not present old cached prices as current prices.

### 4.5 Government schemes and services
- Central and Rajasthan government agriculture, horticulture, irrigation, livestock and farmer-support schemes.
- Eligibility, benefit, required documents, application process, deadline and official source.
- Clearly identify scheme information update date.
- Link to official application portals; do not collect unnecessary sensitive information.
- Include KVK, agriculture/horticulture extension, veterinary hospital and helpline directory where verified.

### 4.6 Equipment, inputs and farm enterprise
- Farm machinery and custom-hiring information.
- Seed, fertilizer and input-use education.
- FPO/cooperative and extension-service information.
- Farm budgeting, cost of cultivation, income and simple profit/loss.
- Value addition, storage and small farm enterprise ideas.

---

## 5. Rajasthan-first localization

- District and agro-climatic-zone selection, with the option to expand to other states.
- Local crop calendars and crop/variety guidance reviewed against Rajasthan agricultural university, ICAR, KVK and state department publications.
- Arid and semi-arid farming, drought and heat management.
- Water conservation, drip/sprinkler irrigation and rainwater harvesting.
- Salinity/alkalinity and water-quality education.
- Horticulture suited to local conditions, including ber, date palm, pomegranate, kinnow/citrus and other district-appropriate crops.
- Regional livestock, fodder, camel and small-ruminant management.
- Hindi-first terminology, with simple explanations and English toggle.

Do not assume that a single recommendation applies to every district. Recommendations must be tagged by region, season, crop/species and source.

---

## 6. Offline-first product requirements

- Farmer profile, crop/plant guides, livestock guides, diaries and reminders must remain usable offline.
- Store structured reference content locally (Room/database assets); cache only content with clear update dates.
- Network-dependent features (weather, live market rates, scheme updates, maps) must show connectivity and last-updated status.
- Sync should be optional, safe and resilient to interrupted connectivity.
- User data remains on-device by default; Android automatic app-data backup is disabled. Any export, cloud sync or sharing must be an explicit, user-controlled action.
- Keep the APK size and memory use suitable for mainstream Android phones.

## 7. Content quality, trust and safety

- Prefer official and research-backed sources: ICAR institutes, Rajasthan agricultural universities, Krishi Vigyan Kendras, state Agriculture/Horticulture/Animal Husbandry departments and other relevant government authorities.
- Store source, publication/update date, geography, season, crop/species and review status with advisory content.
- Separate educational information, AI screening and expert/official recommendations.
- No invented live prices, forecasts, scheme deadlines, vaccination schedules, pesticide dosages or veterinary prescriptions.
- Show a clear escalation to an agricultural officer, KVK, horticulture specialist or veterinarian where the situation needs professional assessment.
- Respect privacy: collect only what a feature needs; location, camera and photo access must be permission-based.

## 8. Delivery roadmap

| Milestone | Scope | Completion condition |
|---|---|---|
| M1 | App foundation | Bilingual app, navigation, cloud build and installable APK |
| M2 | Farmer and farm profile | Offline profile and farm details saved and restored |
| M3 | Agriculture, horticulture and gardening foundation | Field crop guides, Plant Doctor, fruit/vegetable/flower/home-garden knowledge, soil/water basics and seasonal care content; verified APK and device checks for camera/gallery |
| M4 | Animal husbandry foundation | Bilingual species/breed starter library, feeding and housing education, offline animal profiles and health-event history, veterinarian-confirmed local reminders, non-destructive Room migrations, verified APK and device acceptance |
| M5 | Farm, garden and livestock diaries | Activity logs, expenses, production records and reminders; verified APK |
| M6 | Rajasthan advisory services (deferred) | Start only after M3–M5 core foundations are stable and the milestone is explicitly reactivated; verified local calendars, soil testing, weather and alerts where reliable sources/APIs exist |
| M7 | Markets, schemes and services | Dated mandi prices, official schemes and verified service directory |
| M8 | Farm economics and smart assistance | Cost/profit tools and carefully bounded AI/search assistance |

Milestones can overlap only when a shared foundation is ready and the user-approved sequence is preserved. M6 is currently deferred. Each milestone must be implemented, cloud-compiled, checked, packaged and device-tested where applicable before being called complete. See [MILESTONE_TRACKER.md](MILESTONE_TRACKER.md) for the live audit and acceptance checklist.

## 9. M3/M4 immediate implementation priorities

1. Replace generic placeholders with three clear pillars: **खेती**, **बागवानी**, **पशुपालन**.
2. Expand My Farm to capture multiple fields/crops, horticulture plants/orchards and individual or herd-level livestock basics.
3. Build a structured crop library with season, soil, sowing, irrigation, nutrition, pests and harvest sections.
4. Expand the Plant Doctor guide while keeping AI model scope and confidence transparent.
5. Build and validate the structured animal/breed starter library, offline animal profiles, veterinarian-confirmed health-event records and local reminders; expand advanced breed and ration content only after expert review.
6. Add farm, garden and animal diaries as shared reusable record components.
7. Verify every source, translation, unit, reminder and recommendation before publishing it in the app.

