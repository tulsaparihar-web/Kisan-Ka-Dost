package com.parihardigitallabs.kisankadost

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class AnimalGuide(
    val key: String,
    val titleHi: String,
    val titleEn: String,
    val breedsHi: String,
    val breedsEn: String,
    val feedHi: String,
    val feedEn: String,
    val housingHi: String,
    val housingEn: String,
    val careHi: String,
    val careEn: String
)

private val animalGuides = listOf(
    AnimalGuide("Cattle", "गाय / बैल", "Cattle",
        "राठी, थारपारकर, कांकरेज, नागौरी, मेवाती, नारी, सांचोरी", "Rathi, Tharparkar, Kankrej, Nagori, Mewati, Nari, Sanchori",
        "हरा चारा, सूखा चारा और संतुलित पशु आहार का संयोजन रखें। स्वच्छ पानी हर समय उपलब्ध हो। आहार में बदलाव धीरे-धीरे करें और फफूंद लगा चारा न दें।", "Combine green fodder, dry roughage and a balanced ration. Keep clean water available at all times. Change feed gradually and never offer mouldy fodder.",
        "गर्मी में छाया और हवा का अच्छा प्रवाह रखें। फर्श सूखा, साफ और फिसलन-रहित हो। बछड़ों के लिए अलग सुरक्षित जगह रखें।", "Provide shade and good airflow in hot weather. Keep floors dry, clean and non-slip. Provide a separate safe area for calves.",
        "दूध, प्रजनन, टीकाकरण और स्वास्थ्य का रिकॉर्ड रखें। टीकाकरण/कृमिनाशन का समय स्थानीय पशु चिकित्सक से तय करें।", "Keep milk, breeding, vaccination and health records. Set vaccination and deworming timing with the local veterinarian."
    ),
    AnimalGuide("Buffalo", "भैंस", "Buffalo",
        "मुर्रा (हरियाणा), भदावरी (उ.प्र./म.प्र.), मेहसाणा, जाफराबादी, बन्नी (गुजरात)", "Murrah (Haryana), Bhadawari (Uttar Pradesh/Madhya Pradesh), Mehsana, Jaffarabadi and Banni (Gujarat)",
        "गुणवत्तापूर्ण हरा-सूखा चारा और उत्पादन अवस्था के अनुसार संतुलित राशन दें। स्वच्छ और ठंडा पीने का पानी उपलब्ध रखें।", "Offer good-quality green and dry fodder with a balanced ration suited to production stage. Provide clean, cool drinking water.",
        "भैंसों को गर्मी का तनाव अधिक हो सकता है। छायादार, हवादार स्थान और पानी/नहाने की सुरक्षित सुविधा रखें; कीचड़ या गंदे पानी में मजबूर न करें।", "Buffaloes can be sensitive to heat stress. Provide shade, ventilation and safe access to water for cooling; do not force animals into dirty stagnant water.",
        "दूध दुहने से पहले और बाद में स्वच्छता रखें। थन में सूजन, दर्द या दूध में बदलाव हो तो पशु चिकित्सक से जाँच कराएँ।", "Maintain hygiene before and after milking. Seek veterinary examination for udder swelling, pain or changes in milk."
    ),
    AnimalGuide("Goat", "बकरी", "Goat",
        "सिरोही, जखराना, मारवाड़ी, सोजत, करौली, गुजरी, टोटापुरी, बत्तीसी", "Sirohi, Jakhrana, Marwari, Sojat, Karauli, Gujari, Totapuri, Battisi",
        "पत्तेदार चारा/ब्राउज़िंग, सूखा चारा और जरूरत अनुसार संतुलित पूरक आहार दें। साफ पानी और उपयुक्त खनिज स्रोत उपलब्ध रखें।", "Provide browse/leafy fodder, dry roughage and suitable balanced supplements when needed. Keep clean water and an appropriate mineral source available.",
        "सूखा, हवादार और सुरक्षित शेड रखें। फर्श फिसलन-रहित हो और बच्चों के लिए अलग साफ कोना हो।", "Use a dry, ventilated and secure shelter. Keep floors non-slip and provide a clean separate area for kids.",
        "नए पशु को झुंड में मिलाने से पहले अलग रखें और निरीक्षण करें। भूख बंद होना, दस्त, खाँसी या अचानक कमजोरी पर पशु चिकित्सक से संपर्क करें।", "Isolate and observe new animals before mixing with the flock. Contact a veterinarian for loss of appetite, diarrhoea, cough or sudden weakness."
    ),
    AnimalGuide("Sheep", "भेड़", "Sheep",
        "चोकला, मगरा, मालपुरा, मारवाड़ी, नाली, जैसलमेरी, पूगल, सोनाड़ी, खेरी; अविशान (सिंथेटिक)", "Chokla, Magra, Malpura, Marwari, Nali, Jaisalmeri, Pugal, Sonadi, Kheri; Avishaan (synthetic)",
        "चराई के साथ उपलब्ध हरा/सूखा चारा दें। गर्भित और दूध पिलाने वाली भेड़ों की पोषण जरूरत अलग हो सकती है; स्थानीय विशेषज्ञ से राशन तय करें।", "Use grazing with available green/dry fodder. Pregnant and lactating ewes may have different needs; plan rations with a local expert.",
        "रात में सुरक्षित, सूखा और हवादार बाड़ा रखें। भीड़, गीली बिछावन और तेज हवा/बारिश से बचाएँ।", "Provide a secure, dry and ventilated night shelter. Avoid overcrowding, wet bedding and exposure to harsh wind or rain.",
        "झुंड की नियमित जाँच करें। खुर, त्वचा, ऊन और शरीर की स्थिति देखें; टीकाकरण व कृमिनाशन पशु चिकित्सक की सलाह से करें।", "Inspect the flock regularly. Check hooves, skin, fleece and body condition; follow veterinary advice for vaccination and parasite control."
    ),
    AnimalGuide("Camel", "ऊँट", "Camel",
        "बीकानेरी, जैसलमेरी, मारवाड़ी, जालोरी, मेवाड़ी, मेवाती", "Bikaneri, Jaisalmeri, Marwari, Jalori, Mewari, Mewati",
        "स्थानीय चराई/ब्राउज़िंग और उपलब्ध चारे के अनुसार आहार दें। काम, गर्भावस्था और मौसम के अनुसार पानी व पोषण की जरूरत बदलती है।", "Base feeding on local browsing and available fodder. Water and nutrition needs vary with work, pregnancy and season.",
        "खुला लेकिन सुरक्षित बाड़ा, छाया, सूखी विश्राम जगह और साफ पानी की व्यवस्था रखें। रस्सी/नकेल से चोट न लगे।", "Provide a secure open enclosure, shade, a dry resting area and clean water. Prevent injuries from ropes or nose pegs.",
        "पैरों, त्वचा, मुँह और शरीर की स्थिति नियमित देखें। काम के बाद आराम दें और लंगड़ापन/घाव पर विशेषज्ञ से सलाह लें।", "Check feet, skin, mouth and body condition regularly. Allow rest after work and seek expert advice for lameness or wounds."
    ),
    AnimalGuide("Poultry", "मुर्गीपालन", "Poultry",
        "स्थानीय देसी पक्षी, मेवाड़ी और क्षेत्रानुकूल backyard poultry lines", "Local indigenous birds, Mewari and locally suitable backyard poultry lines",
        "उम्र और उत्पादन के अनुसार साफ, संतुलित poultry feed दें। पानी के बर्तन रोज साफ करें और फफूंद/नमी वाला दाना न दें।", "Provide clean balanced poultry feed suited to age and production. Clean drinkers daily and avoid mouldy or damp feed.",
        "हवादार, सूखा, शिकारी-रोधी शेड रखें। भीड़ कम रखें, सूखी बिछावन और पर्याप्त रोशनी/छाया दें।", "Use a ventilated, dry, predator-proof shelter. Avoid overcrowding and maintain dry litter with suitable light and shade.",
        "बीमार पक्षियों को अलग रखें। अचानक मृत्यु, साँस की तकलीफ या तेजी से फैलती बीमारी पर तुरंत पशु चिकित्सक/पशुपालन विभाग को सूचित करें।", "Separate sick birds. Report sudden deaths, breathing distress or rapidly spreading illness promptly to a veterinarian/Animal Husbandry Department."
    )
)

@Composable
fun AnimalCareScreen(isHindi: Boolean, onManageRecords: () -> Unit) {
    var search by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf<String?>(null) }
    val categories = listOf("All", "Cattle", "Buffalo", "Goat", "Sheep", "Camel", "Poultry")
    val filtered = animalGuides.filter { guide ->
        (selected == "All" || guide.key == selected) &&
            (search.isBlank() || guide.titleEn.contains(search, true) ||
                guide.titleHi.contains(search) || guide.breedsEn.contains(search, true) || guide.breedsHi.contains(search))
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(if (isHindi) "पशुपालन मार्गदर्शिका" else "Animal Husbandry Guide",
            fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128))
        Text(if (isHindi) "राजस्थान के पशुपालकों के लिए नस्ल, चारा, आवास और स्वास्थ्य की शुरुआती जानकारी।"
            else "Starter guidance on breeds, feeding, housing and health for livestock keepers in Rajasthan.",
            fontSize = 13.sp, color = Color(0xFF66766B))
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF3E9))) {
            Text(if (isHindi) "यह सामान्य शैक्षिक जानकारी है। पशु की उम्र, वजन, नस्ल, गर्भावस्था, दूध/काम और स्थानीय चारे के अनुसार राशन व स्वास्थ्य योजना पशु चिकित्सक/पशु पोषण विशेषज्ञ से तय करें।" else
                "This is general educational information. Set rations and health plans with a veterinarian/animal nutritionist based on age, weight, breed, pregnancy, milk/work and locally available feed.",
                Modifier.padding(13.dp), fontSize = 12.sp, color = Color(0xFF203128), lineHeight = 17.sp)
        }
        Button(onClick = onManageRecords, modifier = Modifier.fillMaxWidth()) {
            Text(if (isHindi) "मेरे पशुओं के रिकॉर्ड" else "My Animal Records")
        }
        OutlinedTextField(value = search, onValueChange = { search = it }, modifier = Modifier.fillMaxWidth(),
            singleLine = true, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            placeholder = { Text(if (isHindi) "पशु या नस्ल खोजें" else "Search animal or breed") })
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            categories.forEach { category ->
                FilterChip(selected = selected == category, onClick = { selected = category },
                    label = { Text(if (isHindi) when(category) {
                        "All" -> "सभी"; "Cattle" -> "गाय"; "Buffalo" -> "भैंस"; "Goat" -> "बकरी"; "Sheep" -> "भेड़"; "Camel" -> "ऊँट"; else -> "मुर्गी"
                    } else category) })
            }
        }
        Text(if (isHindi) "${filtered.size} पशु वर्ग" else "${filtered.size} animal groups",
            style = MaterialTheme.typography.labelMedium, color = Color(0xFF66766B))
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            items(filtered, key = { it.key }) { guide ->
                val open = expanded == guide.key
                Card(shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
                    onClick = { expanded = if (open) null else guide.key }) {
                    Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(if (isHindi) guide.titleHi else guide.titleEn, modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF203128))
                            Text(if (open) "−" else "+", fontSize = 21.sp, color = Color(0xFF247A45))
                        }
                        Text((if (isHindi) if (guide.key == "Buffalo") "भारत की मान्यता प्राप्त नस्लें: " else "प्रमुख नस्लें: " else if (guide.key == "Buffalo") "Recognized Indian breeds: " else "Key breeds: ") +
                            if (isHindi) guide.breedsHi else guide.breedsEn, fontSize = 12.sp, color = Color(0xFF247A45), lineHeight = 17.sp)
                        if (guide.key == "Buffalo") {
                            Text(if (isHindi) "इन नस्लों के गृह क्षेत्र अलग-अलग राज्यों में हैं; राजस्थान में चयन से पहले स्थानीय पशु विशेषज्ञ से उपयुक्तता जाँचें।" else
                                "These breeds have home tracts in different states; check local suitability with an animal husbandry expert before selecting one in Rajasthan.",
                                fontSize = 11.sp, color = Color(0xFF66766B), lineHeight = 15.sp)
                        }
                        if (open) {
                            AnimalDetail(if (isHindi) "चारा और पानी" else "Feed & water", if (isHindi) guide.feedHi else guide.feedEn)
                            AnimalDetail(if (isHindi) "आवास और आराम" else "Housing & comfort", if (isHindi) guide.housingHi else guide.housingEn)
                            AnimalDetail(if (isHindi) "स्वास्थ्य और देखभाल" else "Health & care", if (isHindi) guide.careHi else guide.careEn)
                            AnimalDetail(if (isHindi) "नस्ल संदर्भ" else "Breed context", animalBreedNotes(guide.key, isHindi))
                            AnimalDetail(if (isHindi) "आयु और उत्पादन अवस्था के अनुसार" else "By age and production stage", animalStageGuidance(guide.key, isHindi))
                            AnimalDetail(if (isHindi) "जैव-सुरक्षा" else "Biosecurity", animalBiosecurityGuidance(guide.key, isHindi))
                        } else {
                            Text(if (isHindi) "विस्तार देखने के लिए टैप करें" else "Tap for care details",
                                fontSize = 12.sp, color = Color(0xFF66766B))
                        }
                    }
                }
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0E8))) {
                    Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(if (isHindi) "तुरंत पशु चिकित्सक से संपर्क करें" else "Contact a veterinarian urgently",
                            fontWeight = FontWeight.Bold, color = Color(0xFF8A3D20), fontSize = 14.sp)
                        Text(if (isHindi) "साँस लेने में कठिनाई, पेट का अचानक बहुत फूलना, खड़ा न हो पाना, तेज रक्तस्राव, कठिन प्रसव, विषाक्त पदार्थ खाने या अचानक कई पशुओं के बीमार होने पर देरी न करें।" else
                            "Do not delay for breathing difficulty, sudden severe abdominal swelling, inability to stand, heavy bleeding, difficult birth, suspected poisoning or multiple animals becoming ill suddenly.",
                            fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF6E3B2A))
                    }
                }
                Spacer(Modifier.height(5.dp))
                Text(if (isHindi) "नस्लों के नाम ICAR-NBAGR और राजस्थान सरकार के प्रकाशित संदर्भों पर आधारित हैं। नस्ल की पहचान खरीद/प्रजनन निर्णय से पहले विशेषज्ञ से सत्यापित करें।" else
                    "Breed names are based on ICAR-NBAGR and published Rajasthan government references. Verify breed identity with an expert before purchase or breeding decisions.",
                    fontSize = 11.sp, lineHeight = 16.sp, color = Color(0xFF66766B), modifier = Modifier.padding(bottom = 12.dp))
            }
        }
    }
}


private fun animalBreedNotes(key: String, isHindi: Boolean): String = when (key) {
    "Cattle" -> if (isHindi)
        "ICAR-NBAGR गृह क्षेत्र: राठी और थारपारकर—राजस्थान; कांकरेज—राजस्थान/गुजरात; नागौरी और सांचोरी—राजस्थान; मेवाती—राजस्थान/हरियाणा/उत्तर प्रदेश; नारी—राजस्थान/गुजरात। ये पंजीकृत नस्ल-नाम हैं, व्यक्तिगत पशु की शुद्धता या उत्पादन की गारंटी नहीं।"
    else "ICAR-NBAGR home tracts: Rathi and Tharparkar—Rajasthan; Kankrej—Rajasthan/Gujarat; Nagori and Sanchori—Rajasthan; Mewati—Rajasthan/Haryana/Uttar Pradesh; Nari—Rajasthan/Gujarat. Registration does not prove an individual animal's purity or guarantee its performance."
    "Goat" -> if (isHindi)
        "सिरोही: सिरोही क्षेत्र से जुड़ी मध्यम आकार की, प्रायः भूरे रंग/धब्बों वाली बकरी। जखराना: अलवर के बहरोड़-जखराना क्षेत्र से; बड़ी कद-काठी और प्रायः काले रंग पर सफेद निशान। मारवाड़ी: मारवाड़ क्षेत्र की मध्यम आकार, प्रायः काले लंबे बालों वाली नस्ल। सोजत, करौली, गुजरी, टोटापुरी और बत्तीसी भी ICAR-NBAGR सूची में पंजीकृत हैं। स्थानीय प्रजनन रिकॉर्ड और विशेषज्ञ से पहचान जाँचें।"
    else "Sirohi: a medium-sized breed associated with Sirohi, commonly brown with patches. Jakhrana: from the Behror/Jakhrana area of Alwar, generally larger and often black with white markings. Marwari: a medium-sized Marwar breed, commonly with a long black coat. Sojat, Karauli, Gujari, Totapuri and Battisi are also registered in the ICAR-NBAGR list. Verify identity and breeding records locally."
    "Sheep" -> if (isHindi)
        "चोकला (शेखावाटी/छप्पर) को महीन carpet wool के लिए जाना जाता है; नाली भी राजस्थान की ऊन देने वाली नस्ल है। मारवाड़ी भेड़ मारवाड़ क्षेत्र से जुड़ी है और अच्छी fleece के लिए वर्णित है। पूगल और खेरी के गृह क्षेत्र भी राजस्थान हैं। अविशान राजस्थान की पंजीकृत सिंथेटिक नस्ल है—इसे देशी नस्लों से अलग समझें।"
    else "Chokla (Shekhawati/Chappar) is described for fine carpet wool; Nali is another Rajasthan wool breed. Marwari sheep is associated with the Marwar tract and is described for its fleece. Pugal and Kheri also have Rajasthan home tracts. Avishaan is a registered synthetic breed from Rajasthan and must be distinguished from indigenous breeds."
    "Camel" -> if (isHindi)
        "बीकानेरी ऊँट भारी शरीर और भारवाही क्षमता के लिए वर्णित है। जैसलमेरी ऊँट रेतीले शुष्क क्षेत्र से जुड़ा, लंबा और सक्रिय प्रकार है। जालोरी, मारवाड़ी, मेवाड़ी और मेवाती भी राजस्थान से जुड़े पंजीकृत नाम हैं। काम, मौसम और पशु की स्थिति के अनुसार देखभाल बदलती है।"
    else "Bikaneri camels are described as heavily built with draught potential. Jaisalmeri camels are associated with arid sandy tracts and described as tall and active. Jalori, Marwari, Mewari and Mewati are also registered Rajasthan-associated names. Care needs vary with workload, season and individual condition."
    "Buffalo" -> if (isHindi)
        "मुर्रा—हरियाणा; भदावरी—उत्तर प्रदेश/मध्य प्रदेश; मेहसाणा, जाफराबादी और बन्नी—गुजरात के गृह क्षेत्रों से जुड़ी नस्लें हैं। राजस्थान में खरीद/प्रजनन से पहले स्थानीय जलवायु, चारा, पानी और पशु चिकित्सक/पशुपालन विशेषज्ञ की सलाह देखें।"
    else "Murrah—Haryana; Bhadawari—Uttar Pradesh/Madhya Pradesh; Mehsana, Jaffarabadi and Banni—Gujarat home tracts. Before selecting one in Rajasthan, check local climate, fodder and water availability with an animal husbandry expert."
    else -> if (isHindi)
        "मेवाड़ी मुर्गी ICAR-NBAGR में पंजीकृत नाम है। स्थानीय backyard poultry के लिए चूजों का स्रोत, रोग-स्थिति और क्षेत्रानुकूलता पशुपालन विभाग/KVK से जाँचें।"
    else "Mewari chicken is a registered breed name in ICAR-NBAGR. For backyard poultry, verify chick source, health status and local suitability with the Animal Husbandry Department/KVK."
}

private fun animalStageGuidance(key: String, isHindi: Boolean): String = when (key) {
    "Cattle", "Buffalo" -> if (isHindi)
        "नवजात बछड़े को जन्म के तुरंत बाद खीस/पहला दूध पिलाने की व्यवस्था करें। बढ़ते बछड़े को उम्र के अनुसार स्वच्छ पानी, अच्छा रेशा और उपयुक्त calf starter धीरे-धीरे दें। गर्भित, दूध देने वाले और सूखे पशुओं का राशन अलग-अलग जरूरत के अनुसार पशु पोषण विशेषज्ञ से बनवाएँ।"
    else "Ensure the newborn calf receives colostrum promptly after birth. Introduce clean water, good-quality roughage and suitable calf starter gradually as it grows. Have separate rations formulated for pregnant, lactating and dry animals by an animal nutritionist."
    "Goat", "Sheep" -> if (isHindi)
        "नवजात मेमने/बच्चे को खीस अवश्य मिले और उसे सूखे, साफ स्थान पर रखें। बढ़ते बच्चों के लिए अच्छी गुणवत्ता का पत्तेदार चारा/ब्राउज़िंग और उम्र के अनुसार पूरक आहार दें। गर्भावस्था और दूध पिलाने के समय पोषण जरूरत बढ़ सकती है; स्थानीय विशेषज्ञ से राशन तय करें।"
    else "Ensure newborn lambs/kids receive colostrum and remain in a clean, dry area. Growing young stock needs good-quality browse/roughage and age-appropriate supplementary feed. Nutrient needs can rise during pregnancy and lactation; seek local ration advice."
    "Camel" -> if (isHindi)
        "बछड़े को माँ का दूध और स्वच्छ, सुरक्षित पालन स्थान दें। बढ़ते और काम करने वाले ऊँटों की जरूरतें अलग होती हैं; मौसम, काम, गर्भावस्था और उपलब्ध चारे के अनुसार पानी व आहार की योजना स्थानीय ऊँट/पशु विशेषज्ञ से बनवाएँ।"
    else "Provide nursing calves with access to the dam and a clean, safe rearing area. Growing and working camels have different needs; plan feed and water with a local camel/livestock specialist based on season, workload, pregnancy and available browse."
    else -> if (isHindi)
        "चूजों के लिए chick starter, बढ़ते पक्षियों के लिए grower और अंडा देने वाली मुर्गियों के लिए layer feed जैसे उम्र/उत्पादन-अनुकूल पूर्ण आहार का उपयोग करें। आहार बदलना धीरे-धीरे करें और स्वच्छ पानी लगातार दें।"
    else "Use complete age- and purpose-appropriate feed: chick starter for chicks, grower feed for growing birds and layer feed for laying hens. Change diets gradually and provide clean water continuously."
}

private fun animalBiosecurityGuidance(key: String, isHindi: Boolean): String = when (key) {
    "Poultry" -> if (isHindi)
        "आने-जाने वालों और जंगली पक्षियों का संपर्क सीमित रखें। नए पक्षियों को अलग रखकर निरीक्षण करें; बीमार या मृत पक्षियों को सुरक्षित तरीके से संभालें और स्थानीय पशुपालन विभाग को असामान्य मृत्यु की सूचना दें।"
    else "Limit visitor and wild-bird contact. Keep new birds separate for observation; handle sick or dead birds safely and report unusual mortality to the local Animal Husbandry Department."
    else -> if (isHindi)
        "नए पशुओं को झुंड में मिलाने से पहले अलग रखकर निरीक्षण करें। पानी/चारे के बर्तन और शेड साफ रखें; बीमार पशु को अलग रखें और उपकरण साझा करने पर सफाई करें। बीमारी फैलने की आशंका पर पशु चिकित्सक से संपर्क करें।"
    else "Keep newly acquired animals separate for observation before mixing with the herd. Clean feed/water containers and housing; separate sick animals and clean shared equipment. Contact a veterinarian if disease spread is suspected."
}

@Composable
private fun AnimalDetail(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFF247A45))
        Text(value, fontSize = 13.sp, lineHeight = 18.sp, color = Color(0xFF394A40))
    }
}
