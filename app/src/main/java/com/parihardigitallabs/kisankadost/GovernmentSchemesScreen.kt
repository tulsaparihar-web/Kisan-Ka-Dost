package com.parihardigitallabs.kisankadost

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class FarmerScheme(
    val category: String,
    val titleHi: String,
    val titleEn: String,
    val departmentHi: String,
    val departmentEn: String,
    val summaryHi: String,
    val summaryEn: String,
    val detailsHi: List<String>,
    val detailsEn: List<String>,
    val keywords: String,
    val url: String
)

private val farmerSchemes = listOf(
    FarmerScheme(
        "rajasthan",
        "राज किसान साथी – कृषि एवं बागवानी सेवाएँ",
        "Raj Kisan Saathi – Agriculture & Horticulture Services",
        "राजस्थान सरकार • कृषि, उद्यानिकी एवं कृषि विपणन विभाग",
        "Government of Rajasthan • Agriculture, Horticulture & Agricultural Marketing",
        "राजस्थान के किसानों के लिए कृषि और बागवानी से जुड़ी विभिन्न सरकारी सेवाओं व अनुदान की जानकारी का आधिकारिक एकल पोर्टल।",
        "Official single-window portal for Rajasthan farmers to explore agriculture and horticulture services and government assistance.",
        listOf(
            "पोर्टल पर खेत तलाई, तारबंदी, सिंचाई पाइपलाइन, डिग्गी और कृषि यंत्र जैसी सेवाएँ सूचीबद्ध हैं।",
            "बागवानी सेवाओं में ड्रिप/फव्वारा सिंचाई, ग्रीनहाउस, शेडनेट, प्लास्टिक मल्च और फल बगीचे जैसी मदें शामिल हैं।",
            "पात्रता, उपलब्धता, आवेदन अवधि और सहायता की राशि योजना/वर्ष के अनुसार बदल सकती है। आवेदन से पहले पोर्टल पर नवीनतम नियम देखें।"
        ),
        listOf(
            "The portal lists services such as farm ponds, fencing, irrigation pipelines, diggies and farm implements.",
            "Horticulture services include drip/sprinkler irrigation, greenhouses, shade nets, plastic mulch and orchards.",
            "Eligibility, availability, application windows and assistance amounts can change by scheme and year. Check current rules on the portal before applying."
        ),
        "rajasthan rajkisan agriculture horticulture subsidy farm pond fencing pipeline diggi implements drip sprinkler greenhouse",
        "https://rajkisan.rajasthan.gov.in/"
    ),
    FarmerScheme(
        "central",
        "प्रधानमंत्री किसान सम्मान निधि (PM-KISAN)",
        "Pradhan Mantri Kisan Samman Nidhi (PM-KISAN)",
        "भारत सरकार • कृषि एवं किसान कल्याण विभाग",
        "Government of India • Department of Agriculture & Farmers Welfare",
        "पात्र किसान परिवारों के लिए केंद्र सरकार की आय सहायता योजना।",
        "A Central Government income-support scheme for eligible farmer families.",
        listOf(
            "योजना की वर्तमान पात्रता, अपवर्जन श्रेणियाँ, e-KYC और लाभ की स्थिति आधिकारिक पोर्टल पर जाँचें।",
            "लाभार्थी विवरण और भुगतान की स्थिति देखने के लिए आधिकारिक पोर्टल का उपयोग करें।",
            "इस ऐप में राशि या पात्रता की स्थायी गारंटी नहीं दी जाती; नवीनतम सरकारी दिशा-निर्देश मान्य होंगे।"
        ),
        listOf(
            "Check current eligibility, exclusion categories, e-KYC requirements and benefit status on the official portal.",
            "Use the official portal to review beneficiary details and payment status.",
            "This app does not guarantee a fixed benefit amount or eligibility; current government guidelines prevail."
        ),
        "pm kisan samman nidhi income support beneficiary ekyc status",
        "https://pmkisan.gov.in/"
    ),
    FarmerScheme(
        "central",
        "प्रधानमंत्री फसल बीमा योजना (PMFBY)",
        "Pradhan Mantri Fasal Bima Yojana (PMFBY)",
        "भारत सरकार • कृषि एवं किसान कल्याण विभाग",
        "Government of India • Department of Agriculture & Farmers Welfare",
        "अधिसूचित फसलों और क्षेत्रों के लिए प्राकृतिक जोखिमों से फसल नुकसान पर बीमा सुरक्षा की सरकारी योजना।",
        "Government crop-insurance programme for notified crops and areas against specified crop-loss risks.",
        listOf(
            "बीमा उपलब्धता अधिसूचित फसल, क्षेत्र, मौसम और लागू सरकारी नियमों पर निर्भर करती है।",
            "आवेदन की अंतिम तिथि, किसान अंशदान और दावा प्रक्रिया हर मौसम/अधिसूचना के अनुसार जाँचें।",
            "अपने क्षेत्र की अधिसूचना और आवेदन विकल्प आधिकारिक पोर्टल पर देखें।"
        ),
        listOf(
            "Insurance availability depends on notified crops, areas, season and applicable government rules.",
            "Check the enrolment deadline, farmer premium and claim process for each season/notification.",
            "Review your area's notification and enrolment options on the official portal."
        ),
        "pmfby crop insurance fasal bima crop loss premium claim",
        "https://pmfby.gov.in/"
    )
)

@Composable
fun GovernmentSchemesScreen(isHindi: Boolean) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") }
    val categories = listOf("all", "rajasthan", "central")
    val filtered = farmerSchemes.filter { scheme ->
        val categoryMatches = selectedCategory == "all" || scheme.category == selectedCategory
        val searchable = listOf(
            scheme.titleHi, scheme.titleEn, scheme.departmentHi, scheme.departmentEn,
            scheme.summaryHi, scheme.summaryEn, scheme.keywords
        ).joinToString(" ").lowercase()
        categoryMatches && (query.isBlank() || searchable.contains(query.trim().lowercase()))
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            if (isHindi) "किसान सरकारी योजनाएँ" else "Government Schemes for Farmers",
            fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128)
        )
        Text(
            if (isHindi) "राजस्थान और केंद्र सरकार की योजनाओं की ऑफलाइन संदर्भ जानकारी" else "Offline reference guide to Rajasthan and Central Government farmer schemes",
            fontSize = 13.sp, color = Color(0xFF66766B)
        )
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(if (isHindi) "योजना खोजें" else "Search schemes") },
            placeholder = { Text(if (isHindi) "नाम या विषय लिखें" else "Type a name or keyword") }
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = {
                        Text(when (category) {
                            "rajasthan" -> if (isHindi) "राजस्थान" else "Rajasthan"
                            "central" -> if (isHindi) "केंद्र सरकार" else "Central"
                            else -> if (isHindi) "सभी" else "All"
                        })
                    }
                )
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filtered) { scheme ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            if (isHindi) scheme.titleHi else scheme.titleEn,
                            fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF247A45)
                        )
                        Text(
                            if (isHindi) scheme.departmentHi else scheme.departmentEn,
                            fontSize = 11.sp, color = Color(0xFF66766B)
                        )
                        Text(
                            if (isHindi) scheme.summaryHi else scheme.summaryEn,
                            fontSize = 13.sp, lineHeight = 19.sp, color = Color(0xFF394A40)
                        )
                        (if (isHindi) scheme.detailsHi else scheme.detailsEn).forEach { detail ->
                            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                                Text("•", color = Color(0xFF247A45), fontWeight = FontWeight.Bold)
                                Text(detail, modifier = Modifier.weight(1f), fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF526157))
                            }
                        }
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(scheme.url))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (isHindi) "आधिकारिक सरकारी पोर्टल खोलें" else "Open official government portal")
                        }
                    }
                }
            }
            if (filtered.isEmpty()) {
                item {
                    Text(
                        if (isHindi) "इस खोज से कोई योजना नहीं मिली। दूसरा शब्द आज़माएँ।" else "No schemes matched this search. Try another keyword.",
                        modifier = Modifier.padding(16.dp), color = Color(0xFF66766B)
                    )
                }
            }
            item {
                Spacer(Modifier.height(2.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3D9)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(
                            if (isHindi) "महत्वपूर्ण सूचना" else "Important note",
                            fontWeight = FontWeight.Bold, color = Color(0xFF72521A)
                        )
                        Text(
                            if (isHindi) "यह जानकारी ऐप में स्थानीय रूप से उपलब्ध है और इंटरनेट के बिना पढ़ी जा सकती है। आधिकारिक पोर्टल खोलने तथा आवेदन करने के लिए इंटरनेट आवश्यक हो सकता है। पात्रता, सहायता राशि, दस्तावेज़ और अंतिम तिथियाँ बदल सकती हैं—आवेदन से पहले संबंधित विभाग से पुष्टि करें।" else "This information is bundled in the app and can be read offline. Internet may be needed to open an official portal or submit an application. Eligibility, benefit amounts, documents and deadlines can change; confirm details with the relevant department before applying.",
                            fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF62543A)
                        )
                        Text(
                            if (isHindi) "संदर्भ जाँच: 19 नवम्बर 2026" else "Reference checked: 19 November 2026",
                            fontSize = 11.sp, color = Color(0xFF72521A), fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
