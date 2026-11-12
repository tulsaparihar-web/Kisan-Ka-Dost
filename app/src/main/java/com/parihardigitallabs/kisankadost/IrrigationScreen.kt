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

private data class WaterTopic(
    val category: String,
    val hiTitle: String,
    val enTitle: String,
    val hiSummary: String,
    val enSummary: String,
    val hiPoints: List<String>,
    val enPoints: List<String>
)

private val waterTopics = listOf(
    WaterTopic(
        "planning", "सिंचाई की योजना", "Plan irrigation wisely",
        "सिंचाई का समय और मात्रा फसल, मिट्टी, मौसम, जड़ क्षेत्र और उपलब्ध पानी पर निर्भर करती है।",
        "Irrigation timing and amount depend on crop, soil, weather, root zone and available water.",
        listOf("खेत की मिट्टी और पौधों की स्थिति नियमित देखें; केवल तय तारीख के आधार पर पानी न दें।", "हल्की रेतीली मिट्टी में पानी जल्दी नीचे जा सकता है; भारी मिट्टी में जलभराव का जोखिम देखें।", "बारिश का पानी खेत में उपयोगी रहे, इसके लिए खेत की ढाल और निकास का ध्यान रखें।", "फसल की अवस्था और स्थानीय कृषि सलाह के अनुसार सिंचाई का निर्णय लें।"),
        listOf("Check soil and plant condition regularly; do not irrigate only by a fixed calendar date.", "Water may drain quickly in light sandy soils; watch for waterlogging in heavier soils.", "Manage field slope and drainage so rainfall can be used effectively without prolonged waterlogging.", "Decide irrigation using crop stage and local agricultural recommendations.")
    ),
    WaterTopic(
        "methods", "सिंचाई के तरीके", "Irrigation methods",
        "सही विधि पानी की बचत, समान वितरण और श्रम प्रबंधन में मदद कर सकती है।",
        "A suitable method can improve water efficiency, distribution and labour management.",
        listOf("ड्रिप सिंचाई पानी सीधे जड़ क्षेत्र के पास देती है; फिल्टर और पाइप की नियमित सफाई जरूरी है।", "स्प्रिंकलर असमतल या हल्की मिट्टी वाले खेतों में उपयोगी हो सकता है; तेज हवा में वितरण प्रभावित हो सकता है।", "फ्लड/क्यारी सिंचाई में समतलीकरण और नियंत्रित प्रवाह से पानी का नुकसान घट सकता है।", "विधि चुनने से पहले लागत, पानी की गुणवत्ता, फसल, बिजली और रखरखाव देखें।"),
        listOf("Drip irrigation delivers water near the root zone; filters and lines need regular cleaning.", "Sprinklers can suit some uneven or light-soil fields; strong wind can affect uniformity.", "For basin or border irrigation, field levelling and controlled flow can reduce losses.", "Before choosing a system, consider cost, water quality, crop, power and maintenance.")
    ),
    WaterTopic(
        "conserve", "पानी बचाने के उपाय", "Water conservation practices",
        "खेत में नमी बनाए रखना और बारिश के पानी को रोकना शुष्क क्षेत्रों में विशेष रूप से उपयोगी है।",
        "Maintaining soil moisture and capturing rainfall can be especially useful in dry regions.",
        listOf("फसल अवशेष या उपयुक्त जैविक मल्च से मिट्टी की सतह ढकने पर वाष्पीकरण कम हो सकता है।", "कंटूर बंड, मेड़बंदी और खेत तालाब जैसी संरचनाएँ स्थानीय ढाल, मिट्टी और अनुमति के अनुसार अपनाएँ।", "खरपतवार नियंत्रण और समय पर निराई से फसल के साथ पानी की प्रतिस्पर्धा घट सकती है।", "पाइपलाइन, वाल्व और टपकने वाले जोड़ों की मरम्मत करें।"),
        listOf("Covering soil with suitable residue or organic mulch can reduce surface evaporation.", "Contour bunds, field bunding and farm ponds should suit local slope, soil and permissions.", "Timely weed management can reduce competition for available water.", "Repair leaking pipes, valves and connections.")
    ),
    WaterTopic(
        "soil", "मिट्टी और पानी का संबंध", "Soil and water",
        "मिट्टी की बनावट, जैविक पदार्थ और सतह की स्थिति यह तय करने में मदद करती है कि पानी कितनी देर उपलब्ध रहेगा।",
        "Soil texture, organic matter and surface condition influence how long water remains available.",
        listOf("रेतीली मिट्टी में कम मात्रा और जरूरत के अनुसार अपेक्षाकृत बार-बार सिंचाई की जरूरत हो सकती है।", "चिकनी मिट्टी में पानी धीरे प्रवेश कर सकता है; अधिक सिंचाई से जलभराव हो सकता है।", "जैविक पदार्थ और मल्च मिट्टी की संरचना तथा नमी संरक्षण में मदद कर सकते हैं।", "खेत में अलग-अलग हिस्सों की मिट्टी अलग हो तो सिंचाई प्रबंधन भी अलग रखें।"),
        listOf("Sandy soils may need smaller, more frequent irrigations depending on crop and conditions.", "Water may infiltrate slowly in clayey soils; excess irrigation can cause waterlogging.", "Organic matter and mulch can support soil structure and moisture conservation.", "If soil varies across a field, irrigation management may need to vary too.")
    ),
    WaterTopic(
        "quality", "सिंचाई जल की गुणवत्ता", "Irrigation-water quality",
        "खारे या खराब गुणवत्ता वाले पानी का लगातार उपयोग मिट्टी और फसल पर असर डाल सकता है।",
        "Repeated use of saline or poor-quality water can affect soil and crops.",
        listOf("संदेह होने पर सिंचाई जल की जाँच मान्यता प्राप्त प्रयोगशाला से कराएँ।", "पानी की EC और सोडियम संबंधी संकेतकों की व्याख्या विशेषज्ञ से कराएँ।", "जल निकास और मिट्टी की लवणता की जाँच के बिना सुधारक पदार्थ न डालें।", "कुएँ/ट्यूबवेल के पानी की गुणवत्ता समय के साथ बदल सकती है।"),
        listOf("If concerned, test irrigation water through a recognized laboratory.", "Ask a qualified specialist to interpret EC and sodium-related indicators.", "Do not apply amendments without assessing drainage and soil salinity.", "Well and borewell water quality can change over time.")
    ),
    WaterTopic(
        "safety", "सावधानी और स्थानीय सलाह", "Safety and local advice",
        "जल प्रबंधन में गलत अनुमान से फसल, मिट्टी और संसाधनों को नुकसान हो सकता है।",
        "Incorrect assumptions about water management can harm crops, soil and resources.",
        listOf("यह सामान्य शैक्षिक जानकारी है; इसमें फसल-विशिष्ट सिंचाई मात्रा या समय-सारणी नहीं दी गई है।", "बोरवेल, पंप, बिजली और तालाब के आसपास सुरक्षा नियमों का पालन करें।", "जलभराव, अचानक मुरझाने या लवणता के लक्षण हों तो खेत और पानी की जाँच कराएँ।", "स्थानीय KVK, कृषि विश्वविद्यालय या कृषि विभाग की फसल-विशिष्ट सलाह लें।"),
        listOf("This is general education; it does not prescribe crop-specific irrigation volumes or schedules.", "Follow safety practices around borewells, pumps, electricity and farm ponds.", "If waterlogging, sudden wilting or salinity symptoms appear, assess the field and water quality.", "Consult your local KVK, agricultural university or Agriculture Department for crop-specific advice.")
    )
)

@Composable
fun IrrigationScreen(isHindi: Boolean) {
    var selected by remember { mutableStateOf("all") }
    val filters = listOf("all", "planning", "methods", "conserve", "soil", "quality", "safety")
    val topics = if (selected == "all") waterTopics else waterTopics.filter { it.category == selected }
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(if (isHindi) "सिंचाई और जल संरक्षण" else "Irrigation & Water Conservation", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128))
        Text(if (isHindi) "पानी का कुशल उपयोग और खेत की नमी बचाने के व्यावहारिक तरीके" else "Practical ways to use water efficiently and conserve field moisture", fontSize = 13.sp, color = Color(0xFF66766B))
        androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            items(filters) { key ->
                val label = when (key) {
                    "all" -> if (isHindi) "सभी" else "All"
                    "planning" -> if (isHindi) "योजना" else "Planning"
                    "methods" -> if (isHindi) "तरीके" else "Methods"
                    "conserve" -> if (isHindi) "जल बचत" else "Conservation"
                    "soil" -> if (isHindi) "मिट्टी" else "Soil"
                    "quality" -> if (isHindi) "जल गुणवत्ता" else "Water quality"
                    else -> if (isHindi) "सावधानी" else "Safety"
                }
                FilterChip(selected = selected == key, onClick = { selected = key }, label = { Text(label) })
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(topics) { topic ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (isHindi) topic.hiTitle else topic.enTitle, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF247A45))
                        Text(if (isHindi) topic.hiSummary else topic.enSummary, fontSize = 13.sp, lineHeight = 18.sp, color = Color(0xFF394A40))
                        (if (isHindi) topic.hiPoints else topic.enPoints).forEach { point ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("•", color = Color(0xFF247A45), fontWeight = FontWeight.Bold)
                                Text(point, modifier = Modifier.weight(1f), fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF526157))
                            }
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(2.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3D9)), shape = RoundedCornerShape(14.dp)) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(if (isHindi) "महत्वपूर्ण सूचना" else "Important note", fontWeight = FontWeight.Bold, color = Color(0xFF72521A))
                        Text(
                            if (isHindi) "यह सामान्य जानकारी है, किसी फसल के लिए निश्चित सिंचाई मात्रा या समय-सारणी नहीं। मिट्टी, मौसम, फसल अवस्था और पानी की गुणवत्ता के अनुसार निर्णय लें। संदेह होने पर स्थानीय KVK/कृषि विशेषज्ञ से सलाह लें।" else "This is general information, not a fixed irrigation quantity or schedule for a particular crop. Decide based on soil, weather, crop stage and water quality. Consult your local KVK or agricultural specialist when unsure.",
                            fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF62543A)
                        )
                    }
                }
            }
        }
    }
}
