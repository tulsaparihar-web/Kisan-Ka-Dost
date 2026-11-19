# Kisan Ka Dost — Dataset Sources and Licensing Register (M6)

This register records candidate public datasets for offline educational/reference content. A source being publicly downloadable does not automatically grant permission to bundle all of its records or images. Check the exact licence and attribution requirements for every file before redistribution.

| Domain | Candidate source | Useful content | Reuse notes |
|---|---|---|---|
| Crop suitability | Kaggle Crop Recommendation Dataset | N, P, K, temperature, humidity, pH, rainfall and crop labels | Dataset page states Apache 2.0; retain attribution and licence with the imported file. It is a small ML dataset, not validated field advice. |
| India crop production | India District Crop Dataset (1997–2015) | 34,903 district-season records and 63 dominant crops | Repository states CC BY 4.0 for data and MIT for code; retain source citation and attribution. Check Rajasthan district coverage before using. |
| Plant taxonomy and names | Pl@ntNet observations via GBIF | Validated plant observations and taxonomy; broad species coverage | Portal states CC BY 4.0 for published data. Image rights are record-specific; do not bundle photos without checking each record licence. |
| Plant names and descriptions | World Flora Online | Accepted names, synonyms, classification and botanical descriptions | Site states CC BY 4.0 unless otherwise noted. Preserve citations and any record-level exceptions. |
| Rajasthan crop and livestock advisories | ICAR Rabi Agro-Advisory for Farmers 2021–22 | Rajasthan/Haryana/Delhi zone crop priorities, recommended varieties, sowing-window emphasis and livestock management context | Use as a dated advisory, not a timeless prescription; verify current variety and district recommendations. |
| Arid horticulture | ICAR-ATARI Jodhpur / ICAR articles on date palm, fig and arid fruit-based systems | Rajasthan-specific context for date palm, fig, aonla, ber, bael, khejri and mixed fruit systems | Use as regional reference; validate local cultivar, planting material, water and market suitability with KVK/horticulture experts. |
| Rajasthan crop profile | ICAR Rajasthan state profile | State crop priorities, rainfed context, pearl millet, maize, chickpea, mustard, clusterbean and spices | High-level state context only; do not treat as district-level sowing advice. |
| Livestock species and breed reference | FAO DAD-IS | Breed profiles, species-country listings and diversity information | Data may include third-party copyrighted/proprietary content; do not bulk-republish breed text or photos without checking each rights statement. |
| Indian livestock and poultry breeds | ICAR-NBAGR breed registration and species-wise breed lists | Registered breed names, home tracts and accession references for cattle, buffalo, goat, sheep, camel and poultry | Use official lists for names and home tracts; avoid copying full descriptors without attribution/permission. Recheck the register periodically because new breeds are added. |
| India livestock statistics | 20th Livestock Census, data.gov.in | State/district livestock and poultry counts | Released under India's NDSAP policy; check the dataset's specific terms and attribution before bundling. Statistics are not animal-care instructions. |

## Current in-app catalogue coverage (after the M7 content update)

- 20 crop guides (including maize, sesame, soybean, black gram and fennel additions).
- 50 horticulture / garden guides (including date palm, fig, Indian gooseberry, bael, khejri, lasora, ker, karonda, phalsa, cactus pear, kachri and mateera additions).
- 35 Plant Doctor guide entries.
- 6 livestock groups with breed, feeding, housing and care guidance (cattle, buffalo, goat, sheep, camel and poultry).

These are curated bilingual starter guides, not a comprehensive agronomic or veterinary database. Additional species and district-specific recommendations should be imported only with source, date, licence and expert validation.

## M7 content update — arid Rajasthan horticulture

The horticulture catalogue now includes starter reference entries for lasora (Cordia), ker (Capparis), karonda (Carissa), phalsa (Grewia), cactus pear (Opuntia), kachri and mateera, alongside the earlier ber, date palm, pomegranate, aonla, bael and khejri entries. The ICAR-CIAH pages above support crop relevance, germplasm and institute variety references. The new in-app entries are concise bilingual educational summaries; they are not a reproduction of ICAR bulletins and do not claim complete production packages. Verify planting material, cultivar, spacing, irrigation and commercial suitability with current KVK/SAU guidance.

## Implementation rules

1. Prefer official Indian sources for agronomy, animal health, breed characteristics and government schemes.
2. Store source URL, dataset version/date, licence, attribution and retrieval date alongside each imported dataset.
3. Separate factual reference data (species, synonyms, breed names, census counts) from expert-reviewed recommendations (treatment, dosage, disease management).
4. Do not infer pesticide, veterinary-drug or supplement dosages from generic datasets. Any treatment guidance must be locally validated and clearly attributed.
5. Keep large image datasets out of the APK. Use a small, licensed thumbnail set or optional downloads; optimise and cache images.
6. The existing Plant Doctor model is for its labelled leaf-disease classes only. General plant identification requires a separately evaluated model and dataset.

## Source links

- Kaggle Crop Recommendation Dataset: https://www.kaggle.com/datasets/atharvaingle/crop-recommendation-dataset
- India District Crop Dataset: https://github.com/priyanshupriyank04/india-district-crop-dataset
- Pl@ntNet / GBIF portal: https://gbif.plantnet.org/
- World Flora Online: https://worldfloraonline.org/
- FAO DAD-IS: https://www.fao.org/dad-is/
- India 20th Livestock Census: https://www.data.gov.in/catalog/20th-livestock-census
- ICAR Rabi Agro-Advisory 2021–22: https://www.icar.gov.in/sites/default/files/2022-09/Rabi-Agro-Advisory-2021-22_0.pdf
- ICAR Rajasthan state profile: https://www.icar.gov.in/en/node/17288
- ICAR Date Palm in Barmer: https://icar.gov.in/en/node/5899
- ICAR Fig cultivation in Thar region: https://icar.gov.in/en/fig-cultivation-pursuit-profit-thar-region-rajasthan
- ICAR fruit-based cropping systems: https://www.icar.gov.in/sites/default/files/2022-04/ICAR-News-April-June-2013%20Print.pdf
- ICAR-CIAH Bikaner research achievements and varieties: https://icar-ciah.org/index.php?do=achivmnts_highlits&mod=achivmnts
- ICAR-CIAH institute-identified varieties: https://www.icar-ciah.org/index.php?do=veriti_identifid&mod=veriti
- ICAR-CIAH germplasm repository: https://www.icar-ciah.org/index.php?do=resrch_GermDevelp&mod=resrch
- ICAR-CIAH technical bulletin on arid horticulture adoption: https://www.icar-ciah.org/include/pdf/technical_bulletin/Adotion_Arid_Hort_Tech.pdf
- ICAR-CIAH AICRP arid-zone fruit crop centres: https://www.icar-ciah.org/AICRP/index.php?do=AICRP_centers&mod=AICRP
- ICAR-NBAGR breed registration: https://nbagr.res.in/breed-registration
- ICAR-NBAGR cattle breeds: https://nbagr.res.in/cattle-breed
- ICAR-NBAGR goat breeds: https://nbagr.res.in/goat-breed
- ICAR-NBAGR breed registration achievements: https://nbagr.res.in/brc
- ICAR update on newly registered livestock breeds (2025): https://icar.gov.in/en/icar-nbagr-registers-16-new-livestock-and-poultry-breeds-strengthening-indias-animal-genetic
