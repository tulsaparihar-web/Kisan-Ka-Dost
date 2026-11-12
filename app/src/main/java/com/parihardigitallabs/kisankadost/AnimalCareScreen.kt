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
        "राठी, थारपारकर, कांकरेज, नागौरी, मेवाती", "Rathi, Tharparkar, Kankrej, Nagori, Mewati",
        "हरा चारा, सूखा चारा और संतुलित पशु आहार का संयोजन रखें। स्वच्छ पानी हर समय उपलब्ध हो। आहार में बदलाव धीरे-धीरे करें और फफूंद लगा चारा न दें।", "Combine green fodder, dry roughage and a balanced ration. Keep clean water available at all times. Change feed gradually and never offer mouldy fodder.",
        "गर्मी में छाया और हवा का अच्छा प्रवाह रखें। फर्श सूखा, साफ और फिसलन-रहित हो। बछड़ों के लिए अलग सुरक्षित जगह रखें।", "Provide shade and good airflow in hot weather. Keep floors dry, clean and non-slip. Provide a separate safe area for calves.",
        "दूध, प्रजनन, टीकाकरण और स्वास्थ्य का रिकॉर्ड रखें। टीकाकरण/कृमिनाशन का समय स्थानीय पशु चिकित्सक से तय करें।", "Keep milk, breeding, vaccination and health records. Set vaccination and deworming timing with the local veterinarian."
    ),
    AnimalGuide("Buffalo", "भैंस", "Buffalo",
        "मुर्रा, भदावरी, मेहसाणा, जाफराबादी, बन्नी", "Murrah, Bhadawari, Mehsana, Jaffarabadi, Banni",
        "गुणवत्तापूर्ण हरा-सूखा चारा और उत्पादन अवस्था के अनुसार संतुलित राशन दें। स्वच्छ और ठंडा पीने का पानी उपलब्ध रखें।", "Offer good-quality green and dry fodder with a balanced ration suited to production stage. Provide clean, cool drinking water.",
        "भैंसों को गर्मी का तनाव अधिक हो सकता है। छायादार, हवादार स्थान और पानी/नहाने की सुरक्षित सुविधा रखें; कीचड़ या गंदे पानी में मजबूर न करें।", "Buffaloes can be sensitive to heat stress. Provide shade, ventilation and safe access to water for cooling; do not force animals into dirty stagnant water.",
        "दूध दुहने से पहले और बाद में स्वच्छता रखें। थन में सूजन, दर्द या दूध में बदलाव हो तो पशु चिकित्सक से जाँच कराएँ।", "Maintain hygiene before and after milking. Seek veterinary examination for udder swelling, pain or changes in milk."
    ),
    AnimalGuide("Goat", "बकरी", "Goat",
        "सिरोही, जखराना, मारवाड़ी, सोजत, करौली", "Sirohi, Jakhrana, Marwari, Sojat, Karauli",
        "पत्तेदार चारा/ब्राउज़िंग, सूखा चारा और जरूरत अनुसार संतुलित पूरक आहार दें। साफ पानी और उपयुक्त खनिज स्रोत उपलब्ध रखें।", "Provide browse/leafy fodder, dry roughage and suitable balanced supplements when needed. Keep clean water and an appropriate mineral source available.",
        "सूखा, हवादार और सुरक्षित शेड रखें। फर्श फिसलन-रहित हो और बच्चों के लिए अलग साफ कोना हो।", "Use a dry, ventilated and secure shelter. Keep floors non-slip and provide a clean separate area for kids.",
        "नए पशु को झुंड में मिलाने से पहले अलग रखें और निरीक्षण करें। भूख बंद होना, दस्त, खाँसी या अचानक कमजोरी पर पशु चिकित्सक से संपर्क करें।", "Isolate and observe new animals before mixing with the flock. Contact a veterinarian for loss of appetite, diarrhoea, cough or sudden weakness."
    ),
    AnimalGuide("Sheep", "भेड़", "Sheep",
        "चोकला, मगरा, मालपुरा, मारवाड़ी, नाली, जैसलमेरी", "Chokla, Magra, Malpura, Marwari, Nali, Jaisalmeri",
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
fun AnimalCareScreen(isHindi: Boolean) {
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
                        Text((if (isHindi) "प्रमुख नस्लें: " else "Recognized breeds: ") +
                            if (isHindi) guide.breedsHi else guide.breedsEn, fontSize = 12.sp, color = Color(0xFF247A45), lineHeight = 17.sp)
                        if (open) {
                            AnimalDetail(if (isHindi) "चारा और पानी" else "Feed & water", if (isHindi) guide.feedHi else guide.feedEn)
                            AnimalDetail(if (isHindi) "आवास और आराम" else "Housing & comfort", if (isHindi) guide.housingHi else guide.housingEn)
                            AnimalDetail(if (isHindi) "स्वास्थ्य और देखभाल" else "Health & care", if (isHindi) guide.careHi else guide.careEn)
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

@Composable
private fun AnimalDetail(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFF247A45))
        Text(value, fontSize = 13.sp, lineHeight = 18.sp, color = Color(0xFF394A40))
    }
}
