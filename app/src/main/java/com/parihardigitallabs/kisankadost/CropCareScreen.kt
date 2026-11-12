package com.parihardigitallabs.kisankadost

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class CropGuide(
    val nameEn: String, val nameHi: String, val seasonEn: String, val seasonHi: String,
    val soilEn: String, val soilHi: String, val sowingEn: String, val sowingHi: String,
    val waterEn: String, val waterHi: String, val careEn: String, val careHi: String,
    val harvestEn: String, val harvestHi: String, val group: String
)

private val rajasthanCrops = listOf(
    CropGuide("Pearl millet", "बाजरा", "Kharif", "खरीफ", "Sandy to sandy-loam; avoid prolonged waterlogging.", "रेतीली से बलुई दोमट मिट्टी; लंबे जलभराव से बचें।", "Generally with monsoon onset; sowing window varies by district and rainfall.", "आमतौर पर मानसून की शुरुआत के साथ; बुवाई का समय जिले और वर्षा पर निर्भर है।", "Mostly rainfed; provide lifesaving irrigation during prolonged dry spells if available.", "मुख्यतः वर्षा आधारित; लंबा सूखा पड़ने पर उपलब्धता अनुसार जीवन रक्षक सिंचाई दें।", "Use locally recommended varieties, seed treatment and timely weed control. Monitor shoot fly and downy mildew.", "स्थानीय अनुशंसित किस्म, बीजोपचार और समय पर खरपतवार नियंत्रण करें। तना मक्खी और डाउनी मिल्ड्यू की निगरानी करें।", "Harvest when ears mature and grains are hard; dry properly before storage.", "बालियाँ पकने और दाने सख्त होने पर कटाई करें; भंडारण से पहले अच्छी तरह सुखाएँ।", "Kharif"),
    CropGuide("Guar", "ग्वार", "Kharif", "खरीफ", "Well-drained sandy loam; tolerates relatively dry conditions.", "अच्छी जल निकासी वाली बलुई दोमट; अपेक्षाकृत सूखा सहनशील।", "Sow with suitable monsoon moisture; local sowing window differs by region.", "उपयुक्त मानसूनी नमी मिलने पर बुवाई करें; क्षेत्र अनुसार समय अलग होता है।", "Usually rainfed; avoid excess irrigation and waterlogging.", "आमतौर पर वर्षा आधारित; अधिक सिंचाई और जलभराव से बचें।", "Use quality seed and suitable spacing; monitor bacterial blight, powdery mildew and insect pests.", "गुणवत्तापूर्ण बीज और उचित दूरी रखें; जीवाणु झुलसा, चूर्णिल आसिता और कीटों की निगरानी करें।", "Harvest pods at the recommended maturity for the intended grain or vegetable use.", "उद्देश्य के अनुसार फलियों की उचित परिपक्वता पर तुड़ाई करें।", "Kharif"),
    CropGuide("Moong bean", "मूंग", "Kharif / Zaid", "खरीफ / जायद", "Well-drained sandy loam to loam.", "अच्छी जल निकासी वाली बलुई दोमट से दोमट।", "Choose a locally recommended short-duration variety and sow in the appropriate seasonal window.", "स्थानीय अनुशंसित कम अवधि वाली किस्म को उपयुक्त मौसम में बोएँ।", "Sensitive to waterlogging; irrigate according to soil moisture and crop stage.", "जलभराव के प्रति संवेदनशील; मिट्टी की नमी और फसल अवस्था अनुसार सिंचाई करें।", "Monitor whitefly, yellow mosaic symptoms and pod borers; use integrated pest management.", "सफेद मक्खी, पीला मोजेक और फली छेदक की निगरानी करें; समेकित कीट प्रबंधन अपनाएँ।", "Pick mature pods in stages where needed; dry grain safely.", "आवश्यकतानुसार पकी फलियों की चरणबद्ध तुड़ाई करें; दानों को सुरक्षित सुखाएँ।", "Kharif"),
    CropGuide("Moth bean", "मोठ", "Kharif", "खरीफ", "Sandy and light soils; suited to arid areas.", "रेतीली और हल्की मिट्टी; शुष्क क्षेत्रों के अनुकूल।", "Sow with monsoon onset using locally recommended seed.", "मानसून की शुरुआत पर स्थानीय अनुशंसित बीज से बुवाई करें।", "Primarily rainfed; conserve soil moisture and avoid water stagnation.", "मुख्यतः वर्षा आधारित; मिट्टी की नमी बचाएँ और पानी न रुकने दें।", "Use clean seed, timely weeding and monitor pod pests.", "स्वच्छ बीज, समय पर निराई और फली के कीटों की निगरानी करें।", "Harvest at crop maturity and dry before threshing/storage.", "फसल पकने पर कटाई करें और मड़ाई/भंडारण से पहले सुखाएँ।", "Kharif"),
    CropGuide("Groundnut", "मूंगफली", "Kharif", "खरीफ", "Loose, well-drained sandy loam.", "भुरभुरी, अच्छी जल निकासी वाली बलुई दोमट।", "Sow in a suitable monsoon window; use certified seed and recommended spacing.", "उपयुक्त मानसूनी समय में प्रमाणित बीज और अनुशंसित दूरी पर बुवाई करें।", "Avoid moisture stress at flowering, pegging and pod development.", "फूल, पेगिंग और फली विकास के समय नमी की कमी न होने दें।", "Maintain a weed-free crop early; monitor leaf spots and root/soil pests.", "शुरुआती अवस्था में खरपतवार नियंत्रण करें; पत्ती धब्बा और मिट्टी के कीट देखें।", "Harvest when pods are mature; dry pods thoroughly to reduce storage risk.", "फलियाँ पकने पर खुदाई करें; भंडारण जोखिम घटाने के लिए अच्छी तरह सुखाएँ।", "Kharif"),
    CropGuide("Mustard", "सरसों", "Rabi", "रबी", "Well-drained loam to sandy loam.", "अच्छी जल निकासी वाली दोमट से बलुई दोमट।", "Sow in the locally recommended Rabi window; avoid excessively late sowing.", "स्थानीय अनुशंसित रबी समय में बुवाई करें; बहुत देर से बुवाई न करें।", "Critical moisture at flowering and siliqua formation; avoid waterlogging.", "फूल और फलियों के विकास पर नमी महत्वपूर्ण; जलभराव से बचें।", "Monitor aphids and Alternaria/white rust symptoms; use balanced nutrients based on soil test.", "माहू और अल्टरनेरिया/सफेद रतुआ के लक्षण देखें; मिट्टी जाँच के आधार पर संतुलित पोषण दें।", "Harvest when siliquae turn yellow and seeds are firm; avoid shattering losses.", "फलियाँ पीली और दाने सख्त होने पर कटाई करें; दाने झड़ने से बचाएँ।", "Rabi"),
    CropGuide("Wheat", "गेहूँ", "Rabi", "रबी", "Fertile, well-drained loam; soil test is useful.", "उपजाऊ, अच्छी जल निकासी वाली दोमट; मिट्टी जाँच उपयोगी है।", "Select suitable variety and sow within the locally recommended window.", "उपयुक्त किस्म चुनें और स्थानीय अनुशंसित समय पर बुवाई करें।", "Plan irrigation around crown-root initiation, tillering, flowering and grain filling as locally advised.", "स्थानीय सलाह अनुसार क्राउन रूट इनिशिएशन, कल्ले, फूल और दाना भरने पर सिंचाई योजना बनाएँ।", "Use certified seed, seed treatment and balanced fertilizer based on soil test; monitor rust and weeds.", "प्रमाणित बीज, बीजोपचार और मिट्टी जाँच आधारित खाद दें; रतुआ और खरपतवार देखें।", "Harvest at full maturity when grains are hard and crop is dry.", "दाने सख्त और फसल सूखी होने पर पूर्ण परिपक्वता में कटाई करें।", "Rabi"),
    CropGuide("Chickpea", "चना", "Rabi", "रबी", "Well-drained loam to sandy loam; sensitive to waterlogging.", "अच्छी जल निकासी वाली दोमट से बलुई दोमट; जलभराव के प्रति संवेदनशील।", "Use suitable variety, rhizobium inoculation where recommended, and timely sowing.", "उपयुक्त किस्म, अनुशंसित होने पर राइजोबियम उपचार और समय पर बुवाई करें।", "Avoid excess irrigation; moisture is important at flowering and pod formation.", "अधिक सिंचाई से बचें; फूल और फली बनते समय नमी जरूरी है।", "Monitor pod borer and wilt; rotate crops and use integrated management.", "फली छेदक और उकठा रोग देखें; फसल चक्र और समेकित प्रबंधन अपनाएँ।", "Harvest when most pods are mature and plants dry.", "अधिकांश फलियाँ पकने और पौधे सूखने पर कटाई करें।", "Rabi"),
    CropGuide("Barley", "जौ", "Rabi", "रबी", "Well-drained loam and lighter soils.", "अच्छी जल निकासी वाली दोमट और हल्की मिट्टी।", "Use locally recommended variety and sow within the recommended window.", "स्थानीय अनुशंसित किस्म और समय अपनाएँ।", "Irrigation depends on soil and crop purpose; avoid waterlogging.", "सिंचाई मिट्टी और उपयोग पर निर्भर; जलभराव से बचें।", "Monitor weeds and foliar diseases; use balanced nutrition.", "खरपतवार और पत्ती रोग देखें; संतुलित पोषण दें।", "Harvest when ears and grains reach maturity.", "बालियाँ और दाने पकने पर कटाई करें।", "Rabi"),
    CropGuide("Cumin", "जीरा", "Rabi", "रबी", "Well-drained, light to medium soil; avoid waterlogging.", "अच्छी जल निकासी वाली हल्की से मध्यम मिट्टी; जलभराव से बचें।", "Sow in the recommended cool-season window using clean seed.", "अनुशंसित ठंडे मौसम में स्वच्छ बीज से बुवाई करें।", "Use careful, light irrigation as advised; excess humidity can favour disease.", "सलाह अनुसार सावधानी से हल्की सिंचाई करें; अधिक नमी रोग बढ़ा सकती है।", "Monitor wilt, blight and powdery mildew; use disease-free seed and crop rotation.", "उकठा, झुलसा और चूर्णिल आसिता देखें; रोगमुक्त बीज और फसल चक्र अपनाएँ।", "Harvest when plants turn yellow-brown and seeds are mature; dry carefully.", "पौधे पीले-भूरे और बीज पकने पर कटाई करें; सावधानी से सुखाएँ।", "Rabi"),
    CropGuide("Coriander", "धनिया", "Rabi", "रबी", "Well-drained loam to clay loam.", "अच्छी जल निकासी वाली दोमट से चिकनी दोमट।", "Sow in a cool season with locally recommended seed and spacing.", "ठंडे मौसम में स्थानीय अनुशंसित बीज और दूरी से बुवाई करें।", "Maintain suitable moisture at branching, flowering and seed formation.", "शाखा, फूल और बीज बनने पर उचित नमी बनाए रखें।", "Monitor aphids and powdery mildew; avoid dense, poorly ventilated stands.", "माहू और चूर्णिल आसिता देखें; बहुत घनी और कम हवा वाली फसल से बचें।", "Harvest for leaves as needed or for seed when umbels mature.", "जरूरत अनुसार पत्तियों की कटाई करें या छत्र पकने पर बीज हेतु कटाई करें।", "Rabi"),
    CropGuide("Fenugreek", "मेथी", "Rabi", "रबी", "Well-drained loam.", "अच्छी जल निकासी वाली दोमट।", "Sow in the cool season; seed rate and spacing depend on leaf or seed purpose.", "ठंडे मौसम में बुवाई करें; बीज मात्रा और दूरी पत्ती या बीज उद्देश्य पर निर्भर।", "Keep moisture even during establishment; avoid excess water.", "स्थापना के समय समान नमी रखें; अधिक पानी से बचें।", "Use clean seed, timely weeding and monitor aphids and powdery mildew.", "स्वच्छ बीज, समय पर निराई करें और माहू व चूर्णिल आसिता देखें।", "Cut leaves at suitable stage or harvest mature pods for seed.", "उचित अवस्था में पत्तियाँ काटें या बीज हेतु पकी फलियाँ लें।", "Rabi"),
    CropGuide("Isabgol", "ईसबगोल", "Rabi", "रबी", "Light, well-drained soil.", "हल्की, अच्छी जल निकासी वाली मिट्टी।", "Use certified seed and locally recommended sowing time.", "प्रमाणित बीज और स्थानीय अनुशंसित बुवाई समय अपनाएँ।", "Light irrigation as required; avoid heavy irrigation and lodging.", "आवश्यकतानुसार हल्की सिंचाई; भारी सिंचाई और फसल गिरने से बचें।", "Monitor downy mildew and weeds; avoid excess nitrogen.", "डाउनी मिल्ड्यू और खरपतवार देखें; अधिक नाइट्रोजन से बचें।", "Harvest when spikes mature and plants dry; thresh carefully.", "बालियाँ पकने और पौधे सूखने पर कटाई करें; सावधानी से मड़ाई करें।", "Rabi"),
    CropGuide("Cotton", "कपास", "Kharif", "खरीफ", "Deep, well-drained soil; suitability depends on local conditions.", "गहरी, अच्छी जल निकासी वाली मिट्टी; उपयुक्तता स्थानीय स्थिति पर निर्भर।", "Use approved varieties/hybrids and recommended sowing window.", "स्वीकृत किस्म/हाइब्रिड और अनुशंसित बुवाई समय अपनाएँ।", "Manage irrigation by soil moisture and crop stage; avoid waterlogging.", "मिट्टी की नमी और फसल अवस्था अनुसार सिंचाई करें; जलभराव से बचें।", "Scout regularly for pink bollworm, whitefly and sucking pests; follow refuge and IPM guidance.", "गुलाबी सुंडी, सफेद मक्खी और रस चूसक कीट देखें; रिफ्यूज और IPM सलाह अपनाएँ।", "Pick fully opened kapas in clean, dry conditions.", "पूरी तरह खुले टिंडे की चुनाई साफ और सूखे मौसम में करें।", "Kharif"),
    CropGuide("Sorghum", "ज्वार", "Kharif / Rabi", "खरीफ / रबी", "Well-drained medium soils; local type and season matter.", "अच्छी जल निकासी वाली मध्यम मिट्टी; किस्म और मौसम स्थानीय स्थिति पर निर्भर।", "Choose fodder or grain type and sow in the recommended local window.", "चारा या दाना उद्देश्य चुनकर स्थानीय अनुशंसित समय में बुवाई करें।", "Rainfed or limited irrigation depending on season and soil.", "मौसम और मिट्टी के अनुसार वर्षा आधारित या सीमित सिंचाई।", "Monitor shoot fly, stem borer and grain mould; harvest fodder at suitable stage.", "तना मक्खी, तना छेदक और दाना फफूंद देखें; चारे की उचित अवस्था पर कटाई करें।", "Harvest grain at maturity; for fodder, use recommended safe cutting stage.", "दाना पकने पर कटाई करें; चारे के लिए सुरक्षित अनुशंसित अवस्था अपनाएँ।", "Both")
)

@Composable
fun CropCareScreen(isHindi: Boolean) {
    var search by remember { mutableStateOf("") }
    var season by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf<String?>(null) }
    var showAgroZones by remember { mutableStateOf(false) }
    val seasons = listOf("All", "Kharif", "Rabi", "Both")
    val filtered = rajasthanCrops.filter { crop ->
        (season == "All" || crop.group == season || (season == "Kharif" && crop.group == "Kharif / Zaid") || (season == "Rabi" && crop.group == "Rabi / Zaid")) &&
            (search.isBlank() || crop.nameEn.contains(search, true) || crop.nameHi.contains(search))
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(if (isHindi) "खेती और फसल मार्गदर्शिका" else "Farming & Crop Guide", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128))
        Text(if (isHindi) "राजस्थान की प्रमुख फसलों की मौसम, मिट्टी, बुवाई और देखभाल संबंधी जानकारी।" else "Season, soil, sowing and care guidance for important Rajasthan crops.", color = Color(0xFF66766B), fontSize = 13.sp)
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF3E9))) {
            Text(if (isHindi) "ध्यान दें: समय, किस्म और कृषि पद्धति जिले, वर्षा, मिट्टी तथा स्थानीय कृषि विश्वविद्यालय/KVK की सलाह के अनुसार बदल सकती है।" else "Note: timing, varieties and practices vary by district, rainfall and soil. Follow current local university/KVK recommendations.", Modifier.padding(13.dp), fontSize = 12.sp, color = Color(0xFF203128), lineHeight = 17.sp)
        }
        Card(
            onClick = { showAgroZones = !showAgroZones },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F3E8)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(13.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(if (isHindi) "राजस्थान के 10 कृषि-जलवायु क्षेत्र" else "Rajasthan's 10 agro-climatic zones",
                    fontWeight = FontWeight.Bold, color = Color(0xFF203128), fontSize = 14.sp)
                Text(if (showAgroZones) { if (isHindi) "संक्षिप्त फसल संदर्भ छिपाएँ −" else "Hide crop reference −" } else { if (isHindi) "क्षेत्रवार प्रमुख फसलों का सामान्य संदर्भ देखें +" else "View broad crop patterns by zone +"},
                    color = Color(0xFF247A45), fontSize = 12.sp)
                if (showAgroZones) {
                    val zones = listOf(
                        Triple("I-A · Arid Western Plain", "बाजरा, मोठ, तिल", "गेहूँ, सरसों, जीरा"),
                        Triple("I-B · Irrigated North-Western Plain", "कपास, ग्वार", "गेहूँ, सरसों, चना"),
                        Triple("I-C · Hyper-Arid Partial-Irrigated Western Plain", "बाजरा, मोठ, ग्वार", "गेहूँ, सरसों, चना"),
                        Triple("II-A · Transitional Inland Drainage Plain", "बाजरा, ग्वार, दलहन", "सरसों, चना"),
                        Triple("II-B · Transitional Luni Basin Plain", "बाजरा, ग्वार, तिल", "गेहूँ, सरसों"),
                        Triple("III-A · Semi-Arid Eastern Plain", "बाजरा, ग्वार, ज्वार", "गेहूँ, सरसों, चना"),
                        Triple("III-B · Flood-Prone Eastern Plain", "बाजरा, ग्वार, मूंगफली", "गेहूँ, जौ, सरसों, चना"),
                        Triple("IV-A · Sub-Humid Southern Plain", "मक्का, दलहन, ज्वार", "गेहूँ, चना"),
                        Triple("IV-B · Humid Southern Plain", "मक्का, धान, ज्वार, उड़द", "गेहूँ, चना"),
                        Triple("V · Humid South-Eastern Plain", "ज्वार, सोयाबीन", "गेहूँ, सरसों")
                    )
                    zones.forEach { zone ->
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(zone.first, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFF203128))
                            Text((if (isHindi) "खरीफ: " else "Kharif: ") + zone.second + "   " +
                                (if (isHindi) "रबी: " else "Rabi: ") + zone.third, fontSize = 12.sp, color = Color(0xFF526258))
                        }
                    }
                    Text(if (isHindi) "यह राजस्थान सरकार के आर्थिक समीक्षा 2023–24 में दिए गए प्रमुख फसल पैटर्न का सामान्य संदर्भ है; जिले/तहसील की वर्तमान सीमा, वर्षा, सिंचाई और नवीनतम स्थानीय पैकेज ऑफ प्रैक्टिस के अनुसार चयन की पुष्टि करें।" else "Broad crop patterns are adapted from the Rajasthan Economic Review 2023–24. Confirm current district/tehsil boundaries, rainfall, irrigation and the latest local Package of Practices before choosing a crop.",
                        fontSize = 11.sp, lineHeight = 16.sp, color = Color(0xFF66766B))
                }
            }
        }
        OutlinedTextField(value = search, onValueChange = { search = it }, modifier = Modifier.fillMaxWidth(), singleLine = true, leadingIcon = { Icon(Icons.Default.Search, null) }, label = { Text(if (isHindi) "फसल खोजें" else "Search crops") })
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            seasons.forEach { value ->
                FilterChip(selected = season == value, onClick = { season = value }, label = { Text(when(value) { "All" -> if (isHindi) "सभी" else "All"; "Kharif" -> if (isHindi) "खरीफ" else "Kharif"; "Rabi" -> if (isHindi) "रबी" else "Rabi"; else -> if (isHindi) "दोनों" else "Both" }) })
            }
        }
        Text(if (isHindi) "${filtered.size} फसलें" else "${filtered.size} crops", color = Color(0xFF66766B), fontSize = 12.sp)
        filtered.forEach { crop ->
            Card(shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(if (isHindi) crop.nameHi else crop.nameEn, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF203128))
                            Text(if (isHindi) crop.seasonHi else crop.seasonEn, color = Color(0xFF247A45), fontSize = 12.sp)
                        }
                        TextButton(onClick = { expanded = if (expanded == crop.nameEn) null else crop.nameEn }) {
                            Text(if (expanded == crop.nameEn) { if (isHindi) "कम देखें" else "Less" } else { if (isHindi) "विवरण" else "Details" })
                        }
                    }
                    if (expanded == crop.nameEn) {
                        GuideLine(if (isHindi) "मिट्टी" else "Soil", if (isHindi) crop.soilHi else crop.soilEn)
                        GuideLine(if (isHindi) "बुवाई" else "Sowing", if (isHindi) crop.sowingHi else crop.sowingEn)
                        GuideLine(if (isHindi) "सिंचाई" else "Irrigation", if (isHindi) crop.waterHi else crop.waterEn)
                        GuideLine(if (isHindi) "देखभाल और सुरक्षा" else "Care & protection", if (isHindi) crop.careHi else crop.careEn)
                        GuideLine(if (isHindi) "कटाई" else "Harvest", if (isHindi) crop.harvestHi else crop.harvestEn)
                    }
                }
            }
        }
        if (filtered.isEmpty()) Text(if (isHindi) "कोई फसल नहीं मिली।" else "No crops found.")
    }
}

@Composable
private fun GuideLine(title: String, description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFF247A45))
        Text(description, fontSize = 13.sp, lineHeight = 18.sp, color = Color(0xFF203128))
    }
}
