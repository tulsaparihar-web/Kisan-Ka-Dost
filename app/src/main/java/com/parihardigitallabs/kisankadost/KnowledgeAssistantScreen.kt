package com.parihardigitallabs.kisankadost

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal data class KnowledgeEntry(
    val id: String,
    val categoryHi: String,
    val categoryEn: String,
    val questionHi: String,
    val questionEn: String,
    val answerHi: String,
    val answerEn: String,
    val keywords: String
)

internal object OfflineKnowledge {
    val entries = listOf(
        KnowledgeEntry("soil-test","मिट्टी","Soil","मिट्टी की जांच कब और क्यों कराएं?","When and why should I test my soil?","बुवाई से पहले मिट्टी की जांच कराएं। इससे pH और पोषक तत्वों की स्थिति समझने तथा जरूरत के अनुसार खाद की योजना बनाने में मदद मिलती है। नमूना लेने की सही विधि के लिए स्थानीय कृषि विभाग या KVK से पूछें।","Test soil before sowing. It helps understand pH and nutrient status so fertilizer can be planned according to need. Ask your local agriculture department or KVK for the correct sampling method.","soil test मिट्टी जांच pH खाद fertilizer nutrients"),
        KnowledgeEntry("compost","मिट्टी","Soil","कम्पोस्ट या सड़ी गोबर खाद का क्या लाभ है?","What are the benefits of compost or well-decomposed farmyard manure?","अच्छी तरह सड़ी जैविक खाद मिट्टी में जैविक पदार्थ बढ़ाने, संरचना सुधारने और जल धारण में मदद कर सकती है। कच्चा या अधसड़ा गोबर सीधे जड़ों के पास न डालें।","Well-decomposed organic manure can add organic matter, improve soil structure and support water retention. Avoid placing fresh or partly decomposed manure against plant roots.","compost manure farmyard गोबर खाद जैविक organic"),
        KnowledgeEntry("yellow-leaves","फसल","Crops","फसल की पत्तियां पीली क्यों हो रही हैं?","Why are my crop leaves turning yellow?","पीली पत्तियां पोषक तत्वों की कमी, अधिक या कम पानी, जड़ों की समस्या, कीट या रोग के कारण हो सकती हैं। पहले खेत में पानी निकास, जड़ों और पत्तियों के नीचे कीट देखें। फसल, उम्र और लक्षण की फोटो के साथ KVK से पुष्टि करें; बिना पहचान दवा न डालें।","Yellowing can result from nutrient deficiency, over- or under-watering, root stress, pests or disease. Check drainage, roots and leaf undersides first. Confirm with a local KVK using the crop, growth stage and clear photos; do not spray before identifying the cause.","yellow leaves पीली पत्ती रोग deficiency nutrient pest पानी"),
        KnowledgeEntry("seed-treatment","फसल","Crops","बुवाई से पहले बीज के बारे में क्या सावधानी रखें?","What should I check before sowing seed?","विश्वसनीय स्रोत से प्रमाणित और क्षेत्रानुकूल बीज लें। पैकेट पर फसल, किस्म, अंकुरण, वैधता और बीज उपचार की जानकारी पढ़ें। उपचारित बीज को भोजन या पशु चारे में उपयोग न करें और लेबल के निर्देश मानें।","Use quality seed from a reliable source suited to your area. Check crop, variety, germination, validity and treatment information on the packet. Never use treated seed as food or animal feed; follow the label.","seed बीज sowing germination certified"),
        KnowledgeEntry("irrigation","पानी","Water","सिंचाई कब करनी चाहिए?","When should I irrigate my crop?","सिंचाई का समय फसल, मिट्टी, मौसम और वृद्धि अवस्था पर निर्भर करता है। खेत की नमी जांचें और अनावश्यक पानी देने तथा जलभराव से बचें। स्थानीय फसल सलाह और उपलब्ध पानी के अनुसार कार्यक्रम बनाएं।","Irrigation timing depends on crop, soil, weather and growth stage. Check soil moisture, avoid unnecessary watering and prevent waterlogging. Follow local crop guidance and available water conditions.","irrigation सिंचाई पानी water moisture"),
        KnowledgeEntry("drip","पानी","Water","ड्रिप सिंचाई का उपयोग कब लाभदायक है?","When can drip irrigation be useful?","ड्रिप से पानी पौधों के जड़ क्षेत्र में दिया जा सकता है और पानी की बचत में मदद मिल सकती है। फसल, मिट्टी, पानी की गुणवत्ता, फिल्टर और रखरखाव देखकर प्रणाली चुनें। अनुदान की जानकारी आधिकारिक राज्य पोर्टल से जांचें।","Drip delivers water near the root zone and can help save water. Select a system based on crop, soil, water quality, filtration and maintenance. Check any subsidy only through the official state portal.","drip टपक सिंचाई micro irrigation"),
        KnowledgeEntry("mulching","बागवानी","Horticulture","मल्चिंग से क्या फायदा होता है?","How can mulching help?","मल्च मिट्टी की नमी बचाने, खरपतवार घटाने और तापमान में उतार-चढ़ाव कम करने में मदद कर सकता है। तने से थोड़ा खाली स्थान रखें और रोगग्रस्त पौध अवशेषों को मल्च न बनाएं।","Mulch can conserve soil moisture, suppress weeds and moderate soil temperature. Keep it slightly away from stems and do not use diseased plant residues as mulch.","mulch मल्च खरपतवार moisture weeds"),
        KnowledgeEntry("pruning","बागवानी","Horticulture","फलदार पौधों की छंटाई कैसे तय करें?","How should I plan pruning of fruit plants?","छंटाई का समय और तरीका फल प्रजाति, उम्र, मौसम और उद्देश्य पर निर्भर करता है। सूखी, टूटी या रोगग्रस्त शाखाएं साफ औजार से हटाएं। बड़े कट या व्यावसायिक बाग की छंटाई से पहले स्थानीय उद्यानिकी सलाह लें।","Pruning time and method depend on fruit species, age, season and purpose. Remove dead, broken or diseased branches with clean tools. Seek local horticulture advice before major cuts or commercial orchard pruning.","pruning छंटाई fruit tree branch"),
        KnowledgeEntry("nursery","बागवानी","Horticulture","पौध खरीदते समय क्या देखें?","What should I check when buying seedlings?","स्वस्थ, रोगमुक्त और सही किस्म के पौधे विश्वसनीय नर्सरी से लें। जड़ों, तने और पत्तियों पर सड़न या कीट के लक्षण देखें। बिल और किस्म का विवरण रखें तथा अपने क्षेत्र के लिए उपयुक्तता पूछें।","Buy healthy, disease-free and correctly identified plants from a reliable nursery. Check roots, stems and leaves for rot or pests. Keep the bill and variety details, and confirm local suitability.","nursery पौध नर्सरी seedling plant"),
        KnowledgeEntry("aphids","पौधा सुरक्षा","Plant protection","पत्तियों पर छोटे कीट या चिपचिपापन दिखे तो क्या करें?","What should I do if I see small insects or sticky leaves?","यह माहू जैसे रस चूसने वाले कीट हो सकते हैं, पर पहले पहचान जरूरी है। पत्तियों के नीचे देखें, प्रभावित हिस्से की फोटो लें और प्राकृतिक शत्रुओं को नुकसान न पहुंचाएं। कीटनाशक का चयन केवल सही पहचान और लेबल/विशेषज्ञ सलाह के बाद करें।","It may be a sap-sucking pest such as aphids, but identify it first. Inspect leaf undersides, photograph affected parts and protect beneficial insects. Use a pesticide only after correct identification and label or expert guidance.","aphid माहू insects sticky pest कीट"),
        KnowledgeEntry("disease","पौधा सुरक्षा","Plant protection","पौधे में रोग की पहचान कैसे करें?","How can I identify a plant disease?","पत्तियों के दोनों तरफ, तने, जड़ और फल पर लक्षण देखें। प्रभावित पौधे, आसपास के स्वस्थ पौधे, खेत में फैलाव और हाल की बारिश/सिंचाई की जानकारी नोट करें। फोटो और नमूने के साथ KVK या कृषि विशेषज्ञ से पुष्टि करें।","Inspect both leaf surfaces, stems, roots and fruits. Note affected and healthy plants, field pattern and recent rain or irrigation. Confirm with a KVK or agriculture expert using clear photos and a sample.","disease रोग fungus virus spots disease identification"),
        KnowledgeEntry("animal-water","पशु","Livestock","पशुओं को साफ पानी क्यों जरूरी है?","Why is clean water important for livestock?","पशुओं को हर समय साफ और ताजा पानी उपलब्ध रखें। पानी के बर्तन नियमित साफ करें और गर्मी में पानी की उपलब्धता पर विशेष ध्यान दें। पानी पीना अचानक कम हो या बीमारी के लक्षण हों तो पशु चिकित्सक से संपर्क करें।","Provide clean, fresh water at all times. Clean troughs regularly and pay extra attention during hot weather. Contact a veterinarian if water intake suddenly drops or illness signs appear.","animal पानी water livestock पशु साफ"),
        KnowledgeEntry("animal-feed","पशु","Livestock","पशुओं के आहार में क्या ध्यान रखें?","What should I consider in livestock feeding?","आहार पशु की प्रजाति, उम्र, वजन, दूध उत्पादन और अवस्था के अनुसार संतुलित होना चाहिए। साफ चारा और पानी दें; फफूंद लगा या सड़ा चारा न खिलाएं। राशन में बड़ा बदलाव धीरे-धीरे और पशु विशेषज्ञ की सलाह से करें।","Balance feed for species, age, body condition, production and stage. Provide clean fodder and water; never feed mouldy or spoiled fodder. Make major ration changes gradually with advice from an animal nutrition expert.","feed चारा पशु आहार ration fodder"),
        KnowledgeEntry("animal-vet","पशु","Livestock","पशु बीमार लगे तो सबसे पहले क्या करें?","What should I do first if an animal looks ill?","बीमार पशु को संभव हो तो अलग रखें, साफ पानी दें और लक्षण शुरू होने का समय नोट करें। सांस में तकलीफ, खड़ा न हो पाना, गंभीर चोट या प्रसव में कठिनाई हो तो तुरंत पशु चिकित्सक को बुलाएं। खुद से दवा या इंजेक्शन न दें।","Separate the sick animal if possible, provide clean water and note when symptoms began. Seek urgent veterinary help for breathing difficulty, inability to stand, serious injury or difficult delivery. Do not give medicines or injections on your own.","vet doctor पशु चिकित्सक sick बीमारी"),
        KnowledgeEntry("heat-stress","पशु","Livestock","गर्मी में पशुओं की देखभाल कैसे करें?","How should I care for livestock in hot weather?","छायादार और हवादार स्थान, साफ पानी और पर्याप्त आराम दें। दिन के सबसे गर्म समय में अनावश्यक ढुलाई या काम से बचाएं। तेज हांफना, कमजोरी या लड़खड़ाहट दिखे तो तुरंत पशु चिकित्सक से मदद लें।","Provide shade, ventilation, clean water and rest. Avoid unnecessary transport or work during the hottest hours. Get veterinary help promptly for heavy panting, weakness or staggering.","heat गर्मी पशु stress shade"),
        KnowledgeEntry("storage","भंडारण","Storage","अनाज भंडारण से पहले क्या करें?","What should I do before storing grain?","अनाज को सुरक्षित नमी स्तर तक अच्छी तरह सुखाएं, टूटे दाने और कचरा अलग करें तथा साफ, सूखे और कीटरोधी भंडार में रखें। भंडार में रिसाव और नमी न आने दें और नियमित निरीक्षण करें।","Dry grain to a safe moisture level, remove broken grains and debris, and store in a clean, dry, pest-resistant place. Prevent leaks and moisture entry and inspect regularly.","grain storage अनाज भंडारण moisture सुखाना"),
        KnowledgeEntry("crop-rotation","फसल","Crops","फसल चक्र अपनाने का क्या लाभ है?","Why use crop rotation?","फसल चक्र से मिट्टी के पोषक तत्वों पर दबाव कम करने, कुछ कीट-रोग चक्र तोड़ने और खेत की उत्पादकता बनाए रखने में मदद मिल सकती है। फसल चयन स्थानीय जलवायु, पानी और बाजार के अनुसार करें।","Rotation can help balance nutrient demand, interrupt some pest and disease cycles, and maintain field productivity. Choose crops according to local climate, water and market conditions.","crop rotation फसल चक्र legumes दलहन"),
        KnowledgeEntry("drainage","पानी","Water","खेत में जलभराव हो तो क्या करें?","What should I do about waterlogging?","जहां सुरक्षित हो वहां खेत से अतिरिक्त पानी निकालने के लिए निकास मार्ग खुला रखें। जड़ों के आसपास लगातार पानी रहने से नुकसान हो सकता है। अगली बुवाई से पहले खेत की ढाल और जल निकास की स्थानीय विशेषज्ञ से समीक्षा कराएं।","Where safe, keep drainage outlets clear to remove excess water. Prolonged water around roots can cause damage. Review field slope and drainage with a local expert before the next sowing.","waterlogging जलभराव drainage निकास"),
        KnowledgeEntry("pesticide-safety","सुरक्षा","Safety","कीटनाशक का उपयोग करते समय क्या सावधानी रखें?","What precautions should I take when using pesticides?","उत्पाद का लेबल पढ़ें और केवल अनुशंसित फसल व कीट के लिए उपयोग करें। दस्ताने, मास्क और अन्य बताए गए सुरक्षा साधन पहनें। हवा की दिशा, प्रतीक्षा अवधि, भंडारण और खाली पैक के निपटान के निर्देश मानें। बच्चों और पशुओं से दूर रखें।","Read the label and use only for the crop and pest listed. Wear the specified protective equipment. Follow directions for wind, re-entry or pre-harvest interval, storage and container disposal. Keep away from children and animals.","pesticide कीटनाशक safety सुरक्षा spray दवा"),
        KnowledgeEntry("kvk","सहायता","Support","स्थानीय कृषि सलाह कहां से लें?","Where can I get local agricultural advice?","अपने जिले के कृषि विभाग, कृषि विज्ञान केंद्र (KVK), पशुपालन विभाग या उद्यानिकी विभाग से संपर्क करें। सलाह लेते समय फसल/पशु का नाम, जिला, अवस्था, समस्या की शुरुआत और साफ फोटो/रिकॉर्ड साथ रखें।","Contact your district agriculture department, Krishi Vigyan Kendra (KVK), animal husbandry department or horticulture office. Share the crop or animal, district, growth stage, when the issue began and clear photos or records.","KVK कृषि विज्ञान केंद्र help advice support")
    )
}

internal fun normalizeKnowledgeText(value: String): String =
    value.lowercase().replace(Regex("[^\\p{L}\\p{N}\\s]"), " ").trim().replace(Regex("\\s+"), " ")

internal fun searchOfflineKnowledge(query: String, entries: List<KnowledgeEntry> = OfflineKnowledge.entries): List<KnowledgeEntry> {
    val normalized = normalizeKnowledgeText(query)
    if (normalized.isBlank()) return emptyList()
    val terms = normalized.split(" ").filter { it.length > 1 }.toSet()
    if (terms.isEmpty()) return emptyList()
    return entries.mapNotNull { entry ->
        val searchable = normalizeKnowledgeText(listOf(entry.questionHi, entry.questionEn, entry.answerHi, entry.answerEn, entry.keywords, entry.categoryHi, entry.categoryEn).joinToString(" "))
        val matched = terms.count { term -> searchable.contains(term) }
        val phraseBonus = if (searchable.contains(normalized)) 3 else 0
        val score = matched * 2 + phraseBonus
        if (score > 0) entry to score else null
    }.sortedWith(compareByDescending<Pair<KnowledgeEntry, Int>> { it.second }.thenBy { it.first.questionEn })
        .map { it.first }
}

@Composable
fun KnowledgeAssistantScreen(isHindi: Boolean) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") }
    var expandedId by remember { mutableStateOf("") }
    val categories = listOf("all", "Crops", "Soil", "Water", "Horticulture", "Plant protection", "Livestock", "Storage", "Safety", "Support")
    val hindiCategories = listOf("सभी", "फसल", "मिट्टी", "पानी", "बागवानी", "पौधा सुरक्षा", "पशु", "भंडारण", "सुरक्षा", "सहायता")
    val results = remember(query, selectedCategory) {
        val found = if (query.isBlank()) OfflineKnowledge.entries else searchOfflineKnowledge(query)
        if (selectedCategory == "all") found else found.filter {
            val category = it.categoryEn.lowercase()
            when (selectedCategory) {
                "Crops" -> category == "crops"
                "Soil" -> category == "soil"
                "Water" -> category == "water"
                "Horticulture" -> category == "horticulture"
                "Plant protection" -> category == "plant protection"
                "Livestock" -> category == "livestock"
                "Storage" -> category == "storage"
                "Safety" -> category == "safety"
                else -> category == "support"
            }
        }
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(if (isHindi) "किसान ज्ञान सहायक" else "Farmer Knowledge Assistant", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128))
        Text(if (isHindi) "खेती, मिट्टी, पानी, बागवानी और पशुपालन के सामान्य सवालों के जवाब" else "Quick answers for common questions on crops, soil, water, horticulture and livestock", fontSize = 13.sp, color = Color(0xFF66766B))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it; expandedId = "" },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(if (isHindi) "अपना सवाल या शब्द लिखें..." else "Type a question or keyword...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { })
        )
        Row(Modifier.fillMaxWidth().horizontalScrollForKnowledge(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            categories.forEachIndexed { index, category ->
                FilterChip(selected = selectedCategory == category, onClick = { selectedCategory = category }, label = { Text(if (isHindi) hindiCategories[index] else if (category == "all") "All" else category) })
            }
        }
        Text(if (isHindi) "${results.size} उपयोगी विषय" else "${results.size} helpful topics", fontSize = 12.sp, color = Color(0xFF66766B))
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(9.dp), contentPadding = PaddingValues(bottom = 10.dp)) {
            if (results.isEmpty()) item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(if (isHindi) "इस सवाल का सटीक मेल नहीं मिला" else "No close match found", fontWeight = FontWeight.Bold, color = Color(0xFF203128))
                        Text(if (isHindi) "फसल, कीट, लक्षण या समस्या का छोटा शब्द लिखकर देखें। अधिक सटीक सलाह के लिए स्थानीय KVK से संपर्क करें।" else "Try a crop name, pest, symptom or a shorter keyword. Contact your local KVK for case-specific advice.", fontSize = 13.sp, color = Color(0xFF66766B))
                    }
                }
            }
            items(results, key = { it.id }) { entry ->
                val expanded = expandedId == entry.id
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().clickable { expandedId = if (expanded) "" else entry.id }.padding(15.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text(if (isHindi) entry.categoryHi else entry.categoryEn, color = Color(0xFF247A45), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text(if (isHindi) entry.questionHi else entry.questionEn, color = Color(0xFF203128), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        if (expanded) {
                            HorizontalDivider(color = Color(0xFFE8EDE8))
                            Text(if (isHindi) entry.answerHi else entry.answerEn, color = Color(0xFF394A40), fontSize = 13.sp, lineHeight = 19.sp)
                        } else {
                            Text(if (isHindi) "जवाब पढ़ने के लिए टैप करें" else "Tap to read the answer", color = Color(0xFF66766B), fontSize = 12.sp)
                        }
                    }
                }
            }
            item {
                Text(
                    if (isHindi) "महत्वपूर्ण: यह ऑफलाइन, कीवर्ड-आधारित सामान्य जानकारी है; यह AI निदान या व्यक्तिगत कृषि/पशु चिकित्सा सलाह नहीं है। कीटनाशक, दवा, खुराक या गंभीर बीमारी के निर्णय से पहले उत्पाद लेबल और स्थानीय विशेषज्ञ की सलाह लें।"
                    else "Important: This is offline, keyword-based general information—not AI diagnosis or personalised agronomy/veterinary advice. Check product labels and consult a local expert before pesticide, medicine, dosage or serious disease decisions.",
                    color = Color(0xFF66766B), fontSize = 11.sp, lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun Modifier.horizontalScroll(rememberScrollState()): Modifier =
    this.then(Modifier)
