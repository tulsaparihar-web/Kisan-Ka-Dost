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

private data class SeasonPlan(
    val id: String,
    val titleHi: String,
    val titleEn: String,
    val monthsHi: String,
    val monthsEn: String,
    val sowingHi: String,
    val sowingEn: String,
    val cropHi: String,
    val cropEn: String,
    val tasksHi: List<Pair<String, String>>,
    val tasksEn: List<Pair<String, String>>
)

private val seasonPlans = listOf(
    SeasonPlan(
        "Kharif", "खरीफ", "Kharif",
        "सामान्यतः जून–जुलाई (मानसून पर निर्भर)", "Indicatively June–July (depends on monsoon onset)",
        "पर्याप्त और स्थिर मानसूनी नमी मिलने पर बुवाई करें। केवल कैलेंडर तारीख देखकर सूखी मिट्टी में बुवाई न करें।",
        "Sow after adequate and reasonably stable monsoon moisture. Do not sow dry soil just because a calendar date has arrived.",
        "बाजरा, ग्वार, मूंग, मोठ, मूंगफली, कपास और ज्वार (स्थानीय उपयुक्तता के अनुसार)",
        "Pearl millet, guar, moong, moth bean, groundnut, cotton and sorghum (subject to local suitability)",
        listOf(
            "जून" to "खेत की तैयारी, जल निकास और बीज/किस्म की स्थानीय अनुशंसा जाँचें।",
            "जुलाई" to "मानसून नमी के अनुसार बुवाई, पौध संख्या और शुरुआती खरपतवार नियंत्रण देखें।",
            "अगस्त" to "फसल की बढ़वार, पोषण, जलभराव और कीट/रोग की नियमित निगरानी करें।",
            "सितंबर–अक्टूबर" to "फसल अवस्था के अनुसार सिंचाई/सुरक्षा और पकने की निगरानी करें।",
            "अक्टूबर–दिसंबर" to "फसल और किस्म के अनुसार परिपक्वता पर कटाई, सुखाई और सुरक्षित भंडारण करें।"
        ),
        listOf(
            "June" to "Prepare fields, check drainage and confirm locally recommended seed/variety.",
            "July" to "Sow according to monsoon moisture; check plant stand and early weed control.",
            "August" to "Monitor crop growth, nutrition, waterlogging and pests/diseases.",
            "September–October" to "Manage irrigation/protection by crop stage and monitor maturity.",
            "October–December" to "Harvest at crop-specific maturity, dry properly and store safely."
        )
    ),
    SeasonPlan(
        "Rabi", "रबी", "Rabi",
        "सामान्यतः अक्टूबर–नवंबर", "Indicatively October–November",
        "खरीफ फसल के बाद खेत की नमी, तापमान, उपलब्ध सिंचाई और फसल अवधि देखकर बुवाई का समय तय करें।",
        "Choose sowing time based on residual soil moisture, temperature, irrigation availability and crop duration after Kharif.",
        "गेहूँ, सरसों, चना, जौ, जीरा, धनिया, मेथी और ईसबगोल (क्षेत्र/किस्म अनुसार)",
        "Wheat, mustard, chickpea, barley, cumin, coriander, fenugreek and isabgol (by region/variety)",
        listOf(
            "अक्टूबर" to "खेत और बीज तैयार करें; जल्दी/समय पर बोई जाने वाली फसलों की स्थानीय सलाह देखें।",
            "नवंबर" to "मुख्य रबी बुवाई; बीजोपचार और मिट्टी जाँच आधारित पोषण अपनाएँ।",
            "दिसंबर" to "अंकुरण, पौध संख्या, खरपतवार और शुरुआती सिंचाई की जरूरत जाँचें।",
            "जनवरी–फरवरी" to "फसल अवस्था अनुसार सिंचाई और माहू/रतुआ/अन्य स्थानीय जोखिमों की निगरानी करें।",
            "मार्च–अप्रैल" to "परिपक्वता पर कटाई करें; दाने/उपज को अच्छी तरह सुखाकर भंडारित करें।"
        ),
        listOf(
            "October" to "Prepare fields and seed; check local advice for early/normal sowing crops.",
            "November" to "Main Rabi sowing period; use seed treatment and soil-test-based nutrition.",
            "December" to "Check emergence, plant stand, weeds and early irrigation needs.",
            "January–February" to "Irrigate by crop stage and monitor aphids, rust and other local risks.",
            "March–April" to "Harvest at maturity; dry produce well before storage."
        )
    ),
    SeasonPlan(
        "Zaid", "जायद", "Zaid",
        "सामान्यतः फरवरी–मार्च", "Indicatively February–March",
        "गर्मी की फसलें तभी चुनें जब सिंचाई जल उपलब्ध हो और स्थानीय तापमान/मिट्टी अनुकूल हो।",
        "Choose summer crops only when irrigation water is available and local temperature/soil conditions are suitable.",
        "ग्रीष्मकालीन मूंग, चारा फसलें और उपयुक्त मौसमी सब्जियाँ (स्थानीय सलाह अनुसार)",
        "Summer moong, fodder crops and suitable seasonal vegetables (according to local advice)",
        listOf(
            "फरवरी" to "जल उपलब्धता, फसल अवधि और गर्मी सहनशील किस्म देखकर योजना बनाएँ।",
            "मार्च" to "उपयुक्त नमी में बुवाई/रोपाई करें; मल्चिंग और जल बचत उपाय अपनाएँ।",
            "अप्रैल" to "गर्मी और तेज हवा में मिट्टी की नमी पर नजर रखें; सिंचाई का कुशल उपयोग करें।",
            "मई" to "कीट/रोग और गर्मी तनाव की निगरानी करें; कटाई/तुड़ाई समय पर करें।",
            "मई–जून" to "उपज की सफाई, सुखाई और भंडारण करें; अगली खरीफ तैयारी शुरू करें।"
        ),
        listOf(
            "February" to "Plan around water availability, crop duration and heat-tolerant varieties.",
            "March" to "Sow/transplant with suitable moisture; use mulching and water-saving practices.",
            "April" to "Watch soil moisture during heat and hot winds; use irrigation efficiently.",
            "May" to "Monitor pests, diseases and heat stress; harvest/pick on time.",
            "May–June" to "Clean, dry and store produce; begin Kharif preparations."
        )
    )
)

@Composable
fun CropCalendarScreen(isHindi: Boolean) {
    var selectedSeason by remember { mutableStateOf("Kharif") }
    val selected = seasonPlans.first { it.id == selectedSeason }
    val tasks = if (isHindi) selected.tasksHi else selected.tasksEn

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            if (isHindi) "फसल कैलेंडर और योजना" else "Crop Calendar & Planner",
            fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128)
        )
        Text(
            if (isHindi) "राजस्थान की प्रमुख फसल ऋतुओं के अनुसार सामान्य योजना" else "A general seasonal planning guide for Rajasthan crops",
            fontSize = 13.sp, color = Color(0xFF66766B)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            seasonPlans.forEach { plan ->
                FilterChip(
                    selected = selectedSeason == plan.id,
                    onClick = { selectedSeason = plan.id },
                    label = { Text(if (isHindi) plan.titleHi else plan.titleEn) }
                )
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF3E9))
                ) {
                    Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (isHindi) selected.titleHi else selected.titleEn, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128))
                        Text(if (isHindi) selected.monthsHi else selected.monthsEn, fontWeight = FontWeight.SemiBold, color = Color(0xFF247A45))
                        Text(if (isHindi) selected.sowingHi else selected.sowingEn, fontSize = 13.sp, lineHeight = 18.sp, color = Color(0xFF394A40))
                        Text(if (isHindi) "उदाहरण फसलें" else "Example crops", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFF247A45))
                        Text(if (isHindi) selected.cropHi else selected.cropEn, fontSize = 13.sp, lineHeight = 18.sp, color = Color(0xFF203128))
                    }
                }
            }
            item {
                Text(if (isHindi) "महीने के अनुसार काम" else "Month-by-month activities", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128))
            }
            items(tasks) { (month, task) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(month, fontWeight = FontWeight.Bold, color = Color(0xFF247A45), modifier = Modifier.weight(0.34f))
                        Text(task, fontSize = 13.sp, lineHeight = 18.sp, color = Color(0xFF203128), modifier = Modifier.weight(0.66f))
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
                        Text(if (isHindi) "महत्वपूर्ण स्थानीय सलाह" else "Important local guidance", fontWeight = FontWeight.Bold, color = Color(0xFF72521A))
                        Text(
                            if (isHindi) "यह सामान्य शैक्षिक कैलेंडर है, जिले-वार आधिकारिक बुवाई सलाह नहीं। राजस्थान में मानसून आगमन, वर्षा, मिट्टी, सिंचाई, किस्म और ऊँचाई के अनुसार समय बदलता है। बुवाई से पहले अपने KVK/कृषि विश्वविद्यालय या कृषि विभाग की नवीनतम सलाह की पुष्टि करें।" else "This is a general educational calendar, not an official district-wise sowing advisory. Timing in Rajasthan varies with monsoon onset, rainfall, soil, irrigation, variety and elevation. Confirm current recommendations with your KVK, agricultural university or Agriculture Department before sowing.",
                            fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF62543A)
                        )
                    }
                }
            }
        }
    }
}
