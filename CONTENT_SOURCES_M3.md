# M3 Agriculture and Horticulture Content Source Register

This register records reference material used to shape general educational content. It is not a substitute for current district-level Packages of Practices or a live official advisory. Recommendations, varieties, dates, rates and crop-protection actions must be checked against the current local KVK, Rajasthan agricultural university or government department before being presented as locally prescriptive.

| Source | Relevant subject | Use and limitation |
|---|---|---|
| Government of Rajasthan, Economic Review 2023–24, Agriculture and Allied Sectors, Table 2.4 | Ten agro-climatic zones and broad Kharif/Rabi crop patterns | Used for a broad educational zone reference only. District/tehsil boundaries and local crop choices must be checked against current official records and local extension advice. https://jankalyanfile.rajasthan.gov.in/Content/UploadFolder/DepartmentMaster/166/2024/Jul/30409/1666423ffcc-ab62-47f1-b382-de1d8bcb42f8.pdf |\n| ICAR, B.Sc. Horticulture curriculum – Production Technology of Fruit Crops; Production Technology of Vegetables and Flowers | Orchard site selection and establishment, propagation, training/pruning, fruit and vegetable production, flower crops | Used as a topic framework for the horticulture learning library. A curriculum is not itself a crop-specific field recommendation. https://ebook.icar.org.in/index.php/bookprocess/ugbschorti_list |
| ICAR Horticultural Division | Orchard rejuvenation, micro-irrigation/fertigation, intercropping and protected cultivation | Used to frame orchard and protected-cultivation learning topics. https://icar.gov.in/en/horticultural-division |
| Rajasthan Agriculture Department – RajKisan fruit garden guidance | Rajasthan orchard planting material, drip-irrigation context and approved/reputable nursery sourcing | Government programme information can change by financial year; do not assume scheme eligibility or subsidy is current. Confirm on the official portal. https://rajkisan.rajasthan.gov.in/WebsiteNew/newfruitgarden.html |
| Swami Keshwanand Rajasthan Agricultural University, Bikaner – Directorate of Research | Region-specific research and recommendations for selected crops in Rajasthan agro-climatic zones | Examples demonstrate why recommendations must be zone-specific. Do not generalize a Bikaner/Zone 1 recommendation statewide. https://raubikaner.org/directorate-of-research/ |
| ICAR – Kharif Agro-Advisories for Farmers | Seasonal crop and horticulture advisories | Seasonal advisories are time- and region-sensitive. The app's current content uses only general safe-handling and crop-management concepts, not unverified pesticide rates from an advisory. https://icar.gov.in/sites/default/files/Circulars/ICAR-En-Kharif-Agro-Advisories-for-Farmers-2025.pdf |
| National Horticulture Board – Horticulture Experiential Learning | Nursery operations, propagation, protected cultivation and plant hardening | Used as a general nursery workflow reference, not as a crop-specific prescription. https://nhb.gov.in/guideline/Horticulture_Experiential_Learning.pdf |

## Plant Doctor model provenance and limits

- The bundled TensorFlow Lite model is fetched by the pinned Android build workflow from the AgroVision-AI repository at commit `150bf1c847cba7e70adc595d3685ed01e366434b`, file `FarmerFriendApp/src/main/assets/model.tflite`. The workflow verifies the downloaded Git blob hash before building.
- The bundled label file contains 39 output labels: 38 named crop/health/disease classes plus one `background` class. These outputs cover 14 crop types (apple, blueberry, cherry, maize, grape, orange, peach, bell pepper, potato, raspberry, soybean, squash, strawberry and tomato).
- The model is a bounded leaf-image classifier, not a general plant-identification system. It has not been established as a definitive diagnostic model for Rajasthan field conditions or local cultivars. A high model score is not a calibrated probability of disease.
- Results are screening hints only. The app must not prescribe a pesticide, dose or treatment product from the model output; confirm symptoms with a local KVK/agriculture/horticulture expert before taking crop-protection action.
- The included `PLANT_MODEL_LICENSE.txt` records the upstream MIT license notice. Keep this notice with redistributed builds.

## Plant Doctor history and backup behavior

- Diagnosis history is stored in the app-private Room database with compressed JPEG copies in the app-private `plant_diagnoses` directory. The app retains the latest 50 records and provides individual deletion.
- Full user-selected ZIP backup version 3 includes Plant Doctor history and its photos alongside farmer, animal, animal-health and diary data. Versions 1 and 2 remain supported; older backups contain no Plant Doctor history, so restoring them replaces the current local history with an empty history.
- Restore validates record IDs, model-score ranges, safe photo names and exact photo manifests; it stages image files before the database transaction and removes replaced private photos only after a successful commit.
- The ZIP is not password-protected. Users should save it only to a trusted private location. No automatic cloud upload or background sync occurs.

## Editorial rules applied

- Keep Hindi and English guidance equivalent in meaning.
- Distinguish general education from locally validated recommendations.
- Do not publish universal sowing dates, seed rates, fertilizer quantities, irrigation volumes or pesticide doses.
- For orchard establishment, variety/rootstock, spacing, pit preparation and irrigation design, require species- and agro-climatic-zone-specific validation.
- Use healthy, correctly identified planting material from reputable/recognized nurseries.
- Encourage soil and irrigation-water testing before commercial orchard or protected-cultivation investment.
- For plant-health concerns, encourage diagnosis by a qualified local agriculture/horticulture expert; do not prescribe pesticides from symptoms or image-model output.
- Recheck source pages and current official advisories before any future data refresh.
