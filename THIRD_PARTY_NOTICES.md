Third-party model notice

Kisan Ka Dost includes a TensorFlow Lite plant disease classification model
downloaded during the GitHub Actions build from:

https://github.com/YeshwanthRajSelvaraj/AgroVision-AI

Upstream revision: 150bf1c847cba7e70adc595d3685ed01e366434b
Model Git blob: 17967ab19d1d7bfedb36cfcdbd959ea518700793
Model labels: the corresponding labels.txt from the same upstream revision.

The upstream repository declares the MIT License. Its license text is included
in app/src/main/assets/PLANT_MODEL_LICENSE.txt.

Model scope and limitations:
- 39 output classes, including a background class.
- Covers selected PlantVillage-style crop leaf classes only.
- It is not a general plant species identifier.
- Performance on real field images may differ from controlled training images.
- Predictions are advisory screening only, not confirmed diagnoses.
- Verify symptoms locally and consult an agricultural expert before treatment.
