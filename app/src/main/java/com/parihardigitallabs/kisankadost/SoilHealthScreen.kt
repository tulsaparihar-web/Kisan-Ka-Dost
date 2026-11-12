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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
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

private data class SoilTopic(
    val category: String,
    val titleHi: String,
    val titleEn: String,
    val summaryHi: String,
    val summaryEn: String,
    val detailHi: List<String>,
    val detailEn: List<String>
)

private val soilTopics = listOf(
    SoilTopic(
        "test", "मिट्टी की जाँच क्यों करें?", "Why test your soil?",
        "मिट्टी की जाँच से पोषक तत्वों और मिट्टी की स्थिति का पता चलता है। इससे अनावश्यक खाद डालने और खर्च से बचने में मदद मिलती है।",
        "A soil test helps understand nutrient status and soil condition, supporting better decisions and avoiding unnecessary fertilizer expense.",
        listOf("खेत के अलग-अलग समान हिस्सों से कई छोटे नमूने लें और उन्हें मिलाकर प्रतिनिधि नमूना बनाएँ।", "मेड़, खाद के ढेर, पेड़ के नीचे, पानी जमा जगह और हाल में खाद डाले स्थान से नमूना न लें।", "साफ औजार और साफ बाल्टी उपयोग करें; प्रयोगशाला द्वारा बताई गहराई और मात्रा का पालन करें।", "नमूने के साथ खेत/फसल की जानकारी दें और रिपोर्ट सुरक्षित रखें।"),
        listOf("Collect several small samples from representative spots in a uniform field area and mix them into one composite sample.", "Avoid field edges, manure heaps, areas under trees, waterlogged patches and recently fertilized spots.", "Use clean tools and a clean bucket; follow the laboratory's specified sampling depth and quantity.", "Share field and crop details with the lab and keep the report for future comparison.")
    ),
    SoilTopic(
        "test", "मिट्टी की रिपोर्ट समझें", "Understand a soil report",
        "रिपोर्ट में pH, विद्युत चालकता (EC), जैविक कार्बन और उपलब्ध पोषक तत्व जैसे संकेतक हो सकते हैं।",
        "A report may include pH, electrical conductivity (EC), organic carbon and available plant nutrients.",
        listOf("pH मिट्टी की अम्लीयता या क्षारीयता बताता है; इसका अर्थ अकेले देखकर सुधारक पदार्थ न डालें।", "EC लवणता का संकेत देता है; अधिक मान होने पर पानी की गुणवत्ता और निकास भी जाँचें।", "जैविक कार्बन मिट्टी के जैविक पदार्थ का उपयोगी संकेतक है।", "N, P, K तथा सूक्ष्म पोषक तत्वों की श्रेणी और प्रयोगशाला की सिफारिश को साथ पढ़ें।"),
        listOf("pH indicates acidity or alkalinity; do not apply amendments based on pH alone.", "EC indicates salinity; if elevated, also assess irrigation-water quality and drainage.", "Organic carbon is a useful indicator of soil organic matter status.", "Read N, P, K and micronutrient ratings together with the laboratory's recommendations.")
    ),
    SoilTopic(
        "nutrients", "पौधों के मुख्य पोषक तत्व", "Essential plant nutrients",
        "पौधों को कई पोषक तत्व चाहिए। संतुलन जरूरी है—किसी एक तत्व की अधिकता दूसरे तत्वों के उपयोग को प्रभावित कर सकती है।",
        "Plants need several nutrients. Balance matters: excess of one nutrient can interfere with the availability or uptake of another.",
        listOf("नाइट्रोजन (N): पत्तियों और vegetative growth में भूमिका; अधिकता से अनावश्यक हरी बढ़वार हो सकती है।", "फॉस्फोरस (P): जड़ विकास और ऊर्जा संबंधी प्रक्रियाओं में भूमिका।", "पोटाश (K): जल संतुलन, मजबूती और तनाव सहनशीलता में भूमिका।", "सल्फर, जिंक, आयरन, बोरॉन आदि भी फसल और मिट्टी के अनुसार आवश्यक हो सकते हैं।"),
        listOf("Nitrogen (N): supports leafy and vegetative growth; excess may cause overly lush growth.", "Phosphorus (P): supports root development and energy-related plant processes.", "Potassium (K): contributes to water regulation, plant strength and stress tolerance.", "Sulphur, zinc, iron, boron and other nutrients may also be needed depending on crop and soil.")
    ),
    SoilTopic(
        "organic", "जैविक पदार्थ और मिट्टी की सेहत", "Organic matter and soil health",
        "जैविक पदार्थ मिट्टी की संरचना, पानी रोकने की क्षमता और जीवों की गतिविधि को बेहतर बनाने में मदद करता है।",
        "Organic matter can improve soil structure, water-holding capacity and biological activity.",
        listOf("अच्छी तरह सड़ी गोबर खाद या कम्पोस्ट का उपयोग गुणवत्ता और स्थानीय सलाह के अनुसार करें।", "फसल अवशेषों को जलाने के बजाय सुरक्षित तरीके से मिट्टी में मिलाने या मल्च के रूप में उपयोग पर विचार करें।", "दलहनी फसलें और उपयुक्त हरी खाद फसल चक्र का हिस्सा बन सकती हैं।", "कच्ची/अधपकी खाद को सीधे संवेदनशील फसल या बीज के संपर्क में न रखें।"),
        listOf("Use well-decomposed farmyard manure or compost according to quality and local recommendations.", "Where practical, consider safe residue incorporation or mulching instead of burning residues.", "Legumes and suitable green-manure crops can be part of a rotation.", "Avoid placing raw or incompletely decomposed manure directly against seeds or sensitive plants.")
    ),
    SoilTopic(
        "salinity", "लवणता और क्षारीयता", "Salinity and alkalinity",
        "शुष्क और अर्ध-शुष्क क्षेत्रों में लवणता, खराब निकास और सिंचाई जल की गुणवत्ता मिट्टी और फसल को प्रभावित कर सकती है।",
        "In arid and semi-arid areas, salinity, poor drainage and irrigation-water quality can affect soil and crops.",
        listOf("सफेद परत, कमजोर अंकुरण या असमान बढ़वार दिखे तो पहले मिट्टी और पानी की जाँच कराएँ।", "सिर्फ लक्षण देखकर जिप्सम, चूना या अन्य सुधारक पदार्थ न डालें।", "निकास, सिंचाई जल की गुणवत्ता और खेत की समतलता का मूल्यांकन जरूरी हो सकता है।", "फसल/किस्म का चयन स्थानीय कृषि विशेषज्ञ की सलाह से करें।"),
        listOf("If you see a white crust, poor emergence or patchy growth, test both soil and irrigation water first.", "Do not apply gypsum, lime or other amendments based on symptoms alone.", "Drainage, irrigation-water quality and field levelling may need assessment.", "Choose crops and varieties with advice from a local agricultural specialist.")
    ),
    SoilTopic(
        "practice", "खाद का सुरक्षित और संतुलित उपयोग", "Safe and balanced fertilizer use",
        "खाद की मात्रा फसल, मिट्टी की रिपोर्ट, उपज लक्ष्य, सिंचाई और पहले से डाले गए पोषक तत्वों पर निर्भर करती है।",
        "Fertilizer decisions depend on crop, soil report, yield target, irrigation and nutrients already applied.",
        listOf("मिट्टी जाँच रिपोर्ट और कृषि विभाग/KVK की फसल-विशिष्ट सिफारिश को प्राथमिकता दें।", "एक ही मात्रा सभी फसलों, जिलों या खेतों पर लागू नहीं होती।", "उर्वरक का लेबल पढ़ें, दस्ताने उपयोग करें और बच्चों/पशुओं से दूर सूखी जगह रखें।", "उर्वरक को नालियों, कुओं और जलस्रोतों में जाने से रोकें।"),
        listOf("Prioritize soil-test results and crop-specific recommendations from the Agriculture Department or KVK.", "One rate does not fit every crop, district or field.", "Read the label, use appropriate protection and store products dry and away from children and animals.", "Prevent fertilizer from entering drains, wells and water bodies.")
    )
)

@Composable
fun SoilHealthScreen(isHindi: Boolean) {
    var selectedCategory by remember { mutableStateOf("all") }
    val categories = listOf("all", "test", "nutrients", "organic", "salinity", "practice")
    val visibleTopics = if (selectedCategory == "all") soilTopics else soilTopics.filter { it.category == selectedCategory }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            if (isHindi) "मिट्टी की सेहत और पोषण" else "Soil Health & Nutrient Care",
            fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128)
        )
        Text(
            if (isHindi) "मिट्टी जाँच, पोषक तत्व और सुरक्षित खाद प्रबंधन की शुरुआती जानकारी" else "A practical introduction to soil testing, nutrients and responsible fertilizer management",
            fontSize = 13.sp, color = Color(0xFF66766B)
        )
        androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            items(categories) { category ->
                val label = when (category) {
                    "all" -> if (isHindi) "सभी" else "All"
                    "test" -> if (isHindi) "मिट्टी जाँच" else "Testing"
                    "nutrients" -> if (isHindi) "पोषक तत्व" else "Nutrients"
                    "organic" -> if (isHindi) "जैविक पदार्थ" else "Organic matter"
                    "salinity" -> if (isHindi) "लवणता" else "Salinity"
                    else -> if (isHindi) "सुरक्षित उपयोग" else "Safe use"
                }
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(label) }
                )
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(visibleTopics) { topic ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (isHindi) topic.titleHi else topic.titleEn, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF247A45))
                        Text(if (isHindi) topic.summaryHi else topic.summaryEn, fontSize = 13.sp, lineHeight = 18.sp, color = Color(0xFF394A40))
                        (if (isHindi) topic.detailHi else topic.detailEn).forEachIndexed { index, detail ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("•", color = Color(0xFF247A45), fontWeight = FontWeight.Bold)
                                Text(detail, modifier = Modifier.weight(1f), fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF526157))
                            }
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(2.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3D9)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(if (isHindi) "किसान के लिए जरूरी सूचना" else "Important farmer note", fontWeight = FontWeight.Bold, color = Color(0xFF72521A))
                        Text(
                            if (isHindi) "यह सामान्य शैक्षिक जानकारी है, मिट्टी रिपोर्ट या खाद की मात्रा की व्यक्तिगत सिफारिश नहीं। मिट्टी और सिंचाई जल की जाँच मान्यता प्राप्त प्रयोगशाला से कराएँ। खाद/सुधारक की मात्रा अपनी फसल, रिपोर्ट और स्थानीय KVK/कृषि विभाग की सलाह के अनुसार तय करें।" else "This is general educational information, not an interpretation of your soil report or a personalized fertilizer rate. Test soil and irrigation water through a recognized laboratory. Decide fertilizer or amendment rates using your crop, report and local KVK/Agriculture Department recommendations.",
                            fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF62543A)
                        )
                    }
                }
            }
        }
    }
}
