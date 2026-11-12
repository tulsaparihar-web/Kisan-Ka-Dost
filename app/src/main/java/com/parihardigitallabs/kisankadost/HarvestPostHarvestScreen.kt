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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class HarvestGuide(
    val titleHi: String, val titleEn: String,
    val stepsHi: List<String>, val stepsEn: List<String>
)

private val harvestGuides = listOf(
    HarvestGuide("कटाई का सही समय", "Harvest at the right maturity", listOf(
        "फसल की परिपक्वता पहचानने के लिए दाने/फल का रंग, आकार, कठोरता और पौधे की अवस्था देखें।",
        "बहुत जल्दी कटाई से उपज/गुणवत्ता घट सकती है; बहुत देर से कटाई में दाने झड़ना, पक्षी, बारिश या कीट का नुकसान हो सकता है।",
        "मौसम पूर्वानुमान, खेत की नमी और स्थानीय फसल सलाह देखकर कटाई की योजना बनाएँ।"
    ), listOf(
        "Check crop maturity using grain/fruit colour, size, firmness and plant condition.",
        "Early harvest may reduce yield or quality; delayed harvest can increase shattering, bird, rain or pest losses.",
        "Plan harvest using weather, field moisture and local crop guidance."
    )),
    HarvestGuide("सुरक्षित कटाई और मड़ाई", "Safe harvesting and threshing", listOf(
        "साफ औजार और साफ तिरपाल/फर्श उपयोग करें; मिट्टी, पत्थर और कचरे का मिश्रण रोकें।",
        "मशीन की गार्डिंग और सुरक्षा निर्देशों का पालन करें; ढीले कपड़े/दुपट्टे घूमते भागों से दूर रखें।",
        "अनाज को अलग-अलग खेत/किस्म के अनुसार पहचानकर रखें ताकि गुणवत्ता और ट्रेसबिलिटी बनी रहे।"
    ), listOf(
        "Use clean tools and a clean tarpaulin/floor; prevent soil, stones and debris from mixing with produce.",
        "Follow machine guards and safety instructions; keep loose clothing away from moving parts.",
        "Keep produce identified by field and variety to preserve quality and traceability."
    )),
    HarvestGuide("सुखाना और नमी", "Drying and moisture management", listOf(
        "अनाज/बीज को साफ, सूखी और हवादार सतह पर समान परत में फैलाएँ; बीच-बीच में पलटें।",
        "बारिश, ओस, धूल और पशुओं से बचाएँ; रात में नमी बढ़ने पर ढककर सुरक्षित रखें।",
        "भंडारण से पहले नमी की जाँच करें। सुरक्षित नमी सीमा फसल और भंडारण अवधि के अनुसार अलग होती है—स्थानीय मानक/विशेषज्ञ से पुष्टि करें।"
    ), listOf(
        "Spread grain or seed in an even layer on a clean, dry, ventilated surface and turn periodically.",
        "Protect from rain, dew, dust and animals; cover safely if night-time moisture is likely.",
        "Check moisture before storage. Safe moisture limits vary by crop and storage duration—confirm the relevant standard locally."
    )),
    HarvestGuide("भंडारण और कीट रोकथाम", "Storage and pest prevention", listOf(
        "भंडार, बोरी और कंटेनर साफ तथा पूरी तरह सूखे हों; पुराना संक्रमित अनाज/कचरा हटाएँ।",
        "अनाज को जमीन और दीवार से सीधे संपर्क से बचाकर, सूखी और हवादार जगह रखें।",
        "भंडार की नियमित जाँच करें। फफूंद, दुर्गंध, कीट या नमी दिखे तो प्रभावित स्टॉक अलग करें और विशेषज्ञ से सलाह लें।"
    ), listOf(
        "Keep stores, bags and containers clean and fully dry; remove old infested grain and debris.",
        "Keep grain off the floor and away from direct wall contact in a dry, ventilated place.",
        "Inspect stores regularly. Isolate affected stock if mould, odour, insects or moisture appear and seek expert advice."
    )),
    HarvestGuide("फल और सब्जियों की कटाई-पश्चात देखभाल", "Fruit and vegetable handling", listOf(
        "फल/सब्जियाँ उचित परिपक्वता पर सावधानी से तोड़ें; चोट, दबाव और धूप में लंबे समय तक रखने से बचें।",
        "छाया में रखें, क्षतिग्रस्त उपज अलग करें और साफ क्रेट/टोकरी का उपयोग करें।",
        "धुलाई, ग्रेडिंग, पैकिंग और भंडारण फसल-विशिष्ट स्वच्छता व तापमान जरूरतों के अनुसार करें।"
    ), listOf(
        "Harvest fruits and vegetables at suitable maturity; avoid bruising, compression and prolonged sun exposure.",
        "Keep produce shaded, separate damaged items and use clean crates or baskets.",
        "Wash, grade, pack and store according to crop-specific hygiene and temperature requirements."
    )),
    HarvestGuide("मूल्य और गुणवत्ता बनाए रखें", "Preserve value and quality", listOf(
        "उपज की किस्म, खेत, कटाई तारीख और मात्रा का रिकॉर्ड रखें।",
        "बाजार/खरीदार की गुणवत्ता, ग्रेड और पैकिंग शर्तें पहले समझें।",
        "भंडारण या प्रसंस्करण से पहले स्थानीय कृषि विज्ञान केंद्र/कृषि विश्वविद्यालय की फसल-विशिष्ट सलाह लें।"
    ), listOf(
        "Record variety, field, harvest date and quantity.",
        "Understand buyer or market requirements for quality, grade and packaging in advance.",
        "Seek crop-specific post-harvest advice from the local KVK or agricultural university before storage or processing."
    ))
)

@Composable
fun HarvestPostHarvestScreen(isHindi: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(if (isHindi) "कटाई और कटाई-पश्चात देखभाल" else "Harvest & Post-Harvest Care",
            fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128))
        Text(if (isHindi) "साफ कटाई, सही सुखाई और सुरक्षित भंडारण से नुकसान कम करने की सामान्य जानकारी।"
            else "General guidance to reduce losses through clean harvesting, proper drying and safe storage.",
            fontSize = 13.sp, color = Color(0xFF66766B))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(9.dp), modifier = Modifier.weight(1f)) {
            items(harvestGuides, key = { it.titleEn }) { guide ->
                Card(shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text(if (isHindi) guide.titleHi else guide.titleEn, fontWeight = FontWeight.Bold,
                            fontSize = 16.sp, color = Color(0xFF203128))
                        (if (isHindi) guide.stepsHi else guide.stepsEn).forEachIndexed { index, step ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("${index + 1}.", color = Color(0xFF247A45), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(step, color = Color(0xFF394A40), fontSize = 13.sp, lineHeight = 18.sp)
                            }
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(4.dp))
                Text(if (isHindi) "यह सामान्य शैक्षिक मार्गदर्शन है। फसल-विशिष्ट नमी सीमा, भंडारण उपचार और खाद्य सुरक्षा मानकों के लिए वर्तमान स्थानीय/आधिकारिक सलाह लें।" else
                    "This is general educational guidance. Check current local or official advice for crop-specific moisture limits, storage treatments and food-safety standards.",
                    fontSize = 11.sp, lineHeight = 16.sp, color = Color(0xFF66766B), modifier = Modifier.padding(bottom = 12.dp))
            }
        }
    }
}
