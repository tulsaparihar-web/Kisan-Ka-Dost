# Kisan Ka Dost — Dataset Sources and Licensing Register (M6)

This register records candidate public datasets for offline educational/reference content. A source being publicly downloadable does not automatically grant permission to bundle all of its records or images. Check the exact licence and attribution requirements for every file before redistribution.

| Domain | Candidate source | Useful content | Reuse notes |
|---|---|---|---|
| Crop suitability | Kaggle Crop Recommendation Dataset | N, P, K, temperature, humidity, pH, rainfall and crop labels | Dataset page states Apache 2.0; retain attribution and licence with the imported file. It is a small ML dataset, not validated field advice. |
| India crop production | India District Crop Dataset (1997–2015) | 34,903 district-season records and 63 dominant crops | Repository states CC BY 4.0 for data and MIT for code; retain source citation and attribution. Check Rajasthan district coverage before using. |
| Plant taxonomy and names | Pl@ntNet observations via GBIF | Validated plant observations and taxonomy; broad species coverage | Portal states CC BY 4.0 for published data. Image rights are record-specific; do not bundle photos without checking each record licence. |
| Plant names and descriptions | World Flora Online | Accepted names, synonyms, classification and botanical descriptions | Site states CC BY 4.0 unless otherwise noted. Preserve citations and any record-level exceptions. |
| Livestock species and breed reference | FAO DAD-IS | Breed profiles, species-country listings and diversity information | Data may include third-party copyrighted/proprietary content; do not bulk-republish breed text or photos without checking each rights statement. |
| India livestock statistics | 20th Livestock Census, data.gov.in | State/district livestock and poultry counts | Released under India's NDSAP policy; check the dataset's specific terms and attribution before bundling. Statistics are not animal-care instructions. |

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
