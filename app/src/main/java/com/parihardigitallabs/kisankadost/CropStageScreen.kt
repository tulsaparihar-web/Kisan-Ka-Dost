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

private data class StageTopic(
    val group: String,
    val hiTitle: String,
    val enTitle: String,
    val hiSummary: String,
    val enSummary: String,
    val hiPoints: List<String>,
    val enPoints: List<String>
)

private val cropStageTopics = listOf(
    StageTopic("stages", "खेत की तैयारी और बुवाई", "Field preparation and sowing",
        "अच्छी शुरुआत के लिए खेत, बीज और बुवाई की योजना स्थानीय परिस्थितियों के अनुसार बनाएं।",
        "Plan field preparation, seed and sowing around local conditions for a strong start.",
        listOf("मिट्टी की स्थिति, पिछली फसल, खरपतवार और जल निकास देखें।", "मिट्टी जाँच और स्थानीय अनुशंसा के अनुसार खेत की तैयारी और पोषण तय करें।", "प्रमाणित/विश्वसनीय बीज और अनुशंसित किस्म चुनें; बीज उपचार केवल स्वीकृत उत्पाद के लेबल अनुसार करें।", "बुवाई की गहराई, दूरी और समय फसल तथा किस्म की स्थानीय सलाह से तय करें।"),
        listOf("Check soil condition, previous crop, weeds and drainage.", "Plan land preparation and nutrients using soil-test results and local recommendations.", "Choose quality seed and a recommended variety; use only approved seed treatments according to the label.", "Set sowing depth, spacing and timing using crop- and variety-specific local advice.")),
    StageTopic("stages", "अंकुरण और शुरुआती बढ़वार", "Emergence and early growth",
        "इस अवस्था में पौध संख्या, खरपतवार और शुरुआती तनाव पर ध्यान दें।",
        "At this stage, pay attention to plant population, weeds and early stress.",
        listOf("अंकुरण और खाली स्थान देखें; समस्या होने पर कारण पहचानें।", "शुरुआती खरपतवार नियंत्रण समय पर करें, लेकिन फसल को नुकसान न पहुँचाएँ।", "जलभराव, सूखे और कीट के लक्षणों की नियमित निगरानी करें।", "पोषक तत्व या सिंचाई की कमी का निर्णय केवल लक्षण देखकर न करें; खेत और मिट्टी की स्थिति जाँचें।"),
        listOf("Check emergence and gaps; identify the cause before taking corrective action.", "Manage early weeds on time without damaging the crop.", "Monitor waterlogging, drought stress and pest symptoms regularly.", "Do not decide nutrient or irrigation corrections from symptoms alone; assess field and soil conditions.")),
    StageTopic("stages", "वनस्पतिक बढ़वार और शाखाएँ", "Vegetative growth and branching",
        "स्वस्थ पत्तियाँ और जड़ें आगे की फसल अवस्था का आधार हैं।",
        "Healthy leaves and roots support the crop through later growth stages.",
        listOf("फसल की समान बढ़वार, पत्तियों का रंग और पौधों की मजबूती देखें।", "मिट्टी जाँच और अनुशंसित मात्रा के आधार पर ही पोषक तत्व दें।", "फसल के अनुसार निराई-गुड़ाई और मिट्टी चढ़ाने की जरूरत देखें।", "कीटों के साथ लाभकारी कीटों की भी पहचान करें; अनावश्यक छिड़काव से बचें।"),
        listOf("Observe uniform growth, leaf colour and plant strength.", "Apply nutrients only according to soil testing and crop recommendations.", "Check whether weeding, interculture or earthing-up is appropriate for the crop.", "Recognize beneficial insects as well as pests; avoid unnecessary spraying.")),
    StageTopic("stages", "फूल और फल/दाना बनना", "Flowering and fruit/grain formation",
        "कई फसलों में यह संवेदनशील अवस्था होती है; नमी और कीट निगरानी महत्वपूर्ण है।",
        "This is a sensitive period for many crops; moisture and pest monitoring matter.",
        listOf("फसल-विशिष्ट महत्वपूर्ण अवस्थाओं पर स्थानीय सलाह अनुसार नमी बनाए रखें।", "फूल, फल/फली या दाना बनने में बाधा के कारणों को अलग-अलग पहचानें।", "परागण करने वाले कीटों और प्राकृतिक शत्रुओं को बचाएँ।", "किसी भी कीटनाशक का उपयोग तभी करें जब पहचान और आवश्यकता की पुष्टि हो तथा लेबल/स्थानीय सलाह उसका समर्थन करे।"),
        listOf("Maintain moisture at crop-specific critical stages according to local guidance.", "Distinguish possible causes of poor flowering, fruit/pod set or grain formation.", "Protect pollinators and natural enemies.", "Use a pesticide only when the pest and need are confirmed and the label/local recommendation supports its use.")),
    StageTopic("stages", "परिपक्वता और कटाई", "Maturity and harvest",
        "समय पर कटाई और सही सुखाई से गुणवत्ता तथा भंडारण सुरक्षा बेहतर होती है।",
        "Timely harvest and proper drying help protect quality and storage safety.",
        listOf("फसल-विशिष्ट परिपक्वता संकेत पहचानें; केवल तारीख के आधार पर कटाई न करें।", "बारिश/ओस से बचाकर कटाई और मड़ाई की योजना बनाएं।", "उपज को सुरक्षित नमी स्तर तक सुखाएं; साफ और सूखे भंडार में रखें।", "भंडारण में नमी, कीट और कृंतक की नियमित जाँच करें।"),
        listOf("Identify crop-specific maturity signs; do not harvest by date alone.", "Plan harvest and threshing to avoid rain and dew where possible.", "Dry produce to a safe moisture level and store in a clean, dry place.", "Regularly inspect stored produce for moisture, insects and rodents.")),
    StageTopic("ipm", "समेकित कीट प्रबंधन (IPM)", "Integrated Pest Management (IPM)",
        "IPM में रोकथाम, निगरानी, जैविक और यांत्रिक उपाय तथा जरूरत पड़ने पर जिम्मेदार नियंत्रण शामिल हैं।",
        "IPM combines prevention, monitoring, biological and mechanical methods, and responsible control only when needed.",
        listOf("स्वस्थ बीज, फसल चक्र, संतुलित पोषण और खेत की स्वच्छता से शुरुआत करें।", "खेत का नियमित निरीक्षण करें और कीट/रोग, फसल अवस्था तथा फैलाव का रिकॉर्ड रखें।", "जहाँ उपयुक्त हो, फेरोमोन/पीले स्टिकी ट्रैप और यांत्रिक नियंत्रण का उपयोग स्थानीय सलाह से करें।", "लाभकारी कीटों और प्राकृतिक शत्रुओं को बचाएं; केवल कीट दिखने पर तुरंत छिड़काव न करें।"),
        listOf("Start with healthy seed, crop rotation, balanced nutrition and field sanitation.", "Scout regularly and record pest/disease, crop stage and extent of spread.", "Where suitable, use pheromone/yellow sticky traps and mechanical controls with local guidance.", "Protect beneficial insects and natural enemies; do not spray automatically whenever an insect is seen.")),
    StageTopic("ipm", "रोग और पोषक कमी की पहचान", "Recognizing disease and nutrient stress",
        "एक जैसे लक्षण कई कारणों से हो सकते हैं—रोग, कीट, पोषण, पानी, तापमान या रसायन का असर।",
        "Similar symptoms can have different causes: disease, pests, nutrition, water, temperature or chemical injury.",
        listOf("लक्षण किस पत्ती/भाग पर शुरू हुए, कितनी तेजी से फैले और खेत में कहाँ दिखे—यह नोट करें।", "पूरे पौधे और खेत के अलग-अलग हिस्सों की साफ तस्वीर लें।", "मिट्टी की नमी, हाल की खाद/स्प्रे, मौसम और सिंचाई इतिहास देखें।", "संदेह होने पर कृषि विशेषज्ञ/KVK से पुष्टि कराएँ; फोटो-आधारित AI को अंतिम निदान न मानें।"),
        listOf("Record which leaf/plant part was affected first, how quickly it spread and where it appears in the field.", "Take clear photos of the whole plant and affected areas across the field.", "Check soil moisture, recent fertilizer/spray, weather and irrigation history.", "Seek confirmation from an agricultural expert/KVK; do not treat photo-based AI as a final diagnosis.")),
    StageTopic("ipm", "रसायनों का जिम्मेदार उपयोग", "Responsible pesticide use",
        "रसायन अंतिम विकल्पों में से एक हैं और गलत उपयोग से स्वास्थ्य, पर्यावरण तथा लाभकारी जीवों को नुकसान हो सकता है।",
        "Chemical control is only one possible tool; misuse can harm people, the environment and beneficial organisms.",
        listOf("पहले कीट/रोग की सही पहचान और आर्थिक/व्यावहारिक आवश्यकता की पुष्टि करें।", "केवल संबंधित फसल और लक्ष्य के लिए पंजीकृत/लेबल-अनुमोदित उत्पाद चुनें।", "लेबल पर लिखी मात्रा, सुरक्षा उपकरण, पुनः प्रवेश अवधि और कटाई-पूर्व अंतराल का पालन करें।", "हवा, पानी, बच्चों, पशुओं और मधुमक्खियों के जोखिम को ध्यान में रखें; खाली डिब्बे सुरक्षित तरीके से निपटाएँ।"),
        listOf("Confirm pest/disease identity and whether intervention is actually necessary.", "Use only a product registered and label-approved for the crop and target.", "Follow label directions for rate, protective equipment, re-entry interval and pre-harvest interval.", "Consider risks to wind, water, children, animals and bees; dispose of empty containers safely."))
)

@Composable
fun CropStageScreen(isHindi: Boolean) {
    var filter by remember { mutableStateOf("all") }
    val filters = listOf("all", "stages", "ipm")
    val topics = if (filter == "all") cropStageTopics else cropStageTopics.filter { it.group == filter }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(if (isHindi) "फसल अवस्था और फसल सुरक्षा" else "Crop Stages & Crop Protection", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128))
        Text(if (isHindi) "बुवाई से कटाई तक देखभाल और समेकित कीट प्रबंधन की सामान्य जानकारी" else "General guidance from sowing to harvest, with integrated pest management basics", fontSize = 13.sp, color = Color(0xFF66766B))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            filters.forEach { key ->
                FilterChip(selected = filter == key, onClick = { filter = key }, label = { Text(when (key) { "all" -> if (isHindi) "सभी" else "All"; "stages" -> if (isHindi) "फसल अवस्थाएँ" else "Crop stages"; else -> if (isHindi) "IPM और सुरक्षा" else "IPM & Safety" }) })
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(topics) { topic ->
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
                    Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (isHindi) topic.hiTitle else topic.enTitle, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF247A45))
                        Text(if (isHindi) topic.hiSummary else topic.enSummary, fontSize = 13.sp, lineHeight = 18.sp, color = Color(0xFF394A40))
                        (if (isHindi) topic.hiPoints else topic.enPoints).forEach { point ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("•", color = Color(0xFF247A45), fontWeight = FontWeight.Bold)
                                Text(point, Modifier.weight(1f), fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF526157))
                            }
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(2.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3D9)), shape = RoundedCornerShape(14.dp)) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(if (isHindi) "फसल सुरक्षा चेतावनी" else "Crop protection caution", fontWeight = FontWeight.Bold, color = Color(0xFF72521A))
                        Text(if (isHindi) "यह सामान्य शैक्षिक मार्गदर्शन है, कीटनाशक की सिफारिश या मात्रा नहीं। किसी भी रोग/कीट की पुष्टि और नियंत्रण के लिए स्थानीय KVK/कृषि विभाग की सलाह तथा उत्पाद का वैध लेबल देखें। दवा मिलाने या छिड़काव के लिए AI अनुमान पर भरोसा न करें।" else "This is general education, not a pesticide recommendation or dose. Confirm the pest/disease and control approach with your local KVK/Agriculture Department and the product's valid label. Never rely on an AI prediction to mix or spray a chemical.",
                            fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF62543A))
                    }
                }
            }
        }
    }
}
