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

private data class GardenGuide(
    val nameHi: String,
    val nameEn: String,
    val categoryHi: String,
    val categoryEn: String,
    val seasonHi: String,
    val seasonEn: String,
    val sunlightHi: String,
    val sunlightEn: String,
    val waterHi: String,
    val waterEn: String,
    val careHi: String,
    val careEn: String
)

private val gardenGuides = listOf(
    GardenGuide("अनार", "Pomegranate", "फलदार पौधे", "Fruit plants", "गर्म और शुष्क क्षेत्रों में उपयुक्त; रोपण का समय स्थानीय सिंचाई और मौसम के अनुसार चुनें।", "Suitable for warm, relatively dry areas; choose planting time according to local irrigation and weather.", "खुली धूप", "Full sun", "जड़ जमने तक नियमित सिंचाई; बाद में मिट्टी और मौसम देखकर पानी दें।", "Water regularly while establishing; later adjust to soil and weather.", "जलभराव से बचाएँ। सूखी/रोगग्रस्त टहनियाँ हटाएँ और फल फटने या धब्बों की निगरानी करें।", "Avoid waterlogging. Remove dead or diseased branches and monitor fruit cracking and spots."),
    GardenGuide("अमरूद", "Guava", "फलदार पौधे", "Fruit plants", "स्थानीय नर्सरी से क्षेत्र के अनुकूल पौधा लें; मानसून या सिंचित मौसम में रोपण किया जा सकता है।", "Choose a locally adapted plant; planting can be done during monsoon or an irrigated season.", "धूप", "Sun", "नई पौध को नियमित पानी दें; स्थापित पेड़ में जरूरत के अनुसार सिंचाई करें।", "Water young plants regularly; irrigate established trees as needed.", "पेड़ को खुला और हवादार रखें। फल मक्खी प्रभावित फल हटाएँ और स्वच्छता रखें।", "Keep the canopy open and airy. Remove fruit-fly-damaged fruit and maintain orchard hygiene."),
    GardenGuide("नींबू / किन्नू", "Lemon / Kinnow", "फलदार पौधे", "Fruit plants", "सही किस्म और रूटस्टॉक का चयन स्थानीय कृषि/बागवानी सलाह से करें।", "Select variety and rootstock with local horticulture advice.", "धूप", "Sun", "हल्की मिट्टी में कम मात्रा में अपेक्षाकृत बार-बार पानी देना उपयोगी हो सकता है।", "On light soils, smaller and more frequent irrigation may be useful.", "जलनिकास अच्छा रखें। पत्तियों का मुड़ना, सूखना और तने के रोग दिखें तो विशेषज्ञ से पहचान कराएँ।", "Ensure drainage. Get expert identification if leaf curling, dieback or trunk symptoms appear."),
    GardenGuide("बेर", "Ber", "फलदार पौधे", "Fruit plants", "शुष्क और अर्ध-शुष्क क्षेत्रों के लिए उपयोगी; किस्म क्षेत्र के अनुसार चुनें।", "Useful in arid and semi-arid areas; select a region-suitable variety.", "खुली धूप", "Full sun", "स्थापना के समय सिंचाई; बाद में वर्षा और मिट्टी के अनुसार।", "Irrigate during establishment; later according to rainfall and soil.", "उचित छँटाई से नई फलदार बढ़वार को बढ़ावा मिलता है। स्थानीय अनुशंसित किस्म अपनाएँ।", "Suitable pruning encourages productive new growth. Prefer locally recommended varieties."),
    GardenGuide("आम", "Mango", "फलदार पौधे", "Fruit plants", "स्थानीय जलवायु के अनुकूल किस्म चुनें; पौधे के लिए पर्याप्त जगह रखें।", "Choose a climate-adapted variety and allow adequate space.", "खुली धूप", "Full sun", "नए पौधे को नियमित पानी; बड़े पेड़ में फूल/फल अवस्था और मिट्टी के अनुसार।", "Water young plants regularly; manage mature trees according to soil and flowering/fruit stage.", "छत्र को हवादार रखें। फूल और फल पर कीट/रोग के लक्षणों की नियमित निगरानी करें।", "Maintain an airy canopy. Monitor flowers and fruit regularly for pest and disease symptoms."),
    GardenGuide("टमाटर", "Tomato", "सब्जियाँ", "Vegetables", "स्थानीय मौसम के अनुसार उपयुक्त खरीफ, रबी या जायद किस्म चुनें।", "Choose a suitable Kharif, Rabi or Zaid variety for the local climate.", "धूप", "Sun", "मिट्टी में समान नमी रखें; पत्तियों पर देर तक पानी न ठहरने दें।", "Maintain even soil moisture; avoid prolonged leaf wetness.", "सहारा दें, फसल चक्र अपनाएँ और रोगग्रस्त पत्तियाँ हटाएँ। दवा का चयन पहचान और लेबल के अनुसार ही करें।", "Stake plants, rotate crops and remove diseased leaves. Select treatments only after diagnosis and label review."),
    GardenGuide("मिर्च", "Chilli", "सब्जियाँ", "Vegetables", "तापमान और स्थानीय मौसम के अनुसार रोपाई का समय चुनें।", "Choose transplanting time according to temperature and local conditions.", "धूप", "Sun", "जलभराव न होने दें; मिट्टी की नमी देखकर सिंचाई करें।", "Avoid waterlogging; irrigate based on soil moisture.", "थ्रिप्स, माइट और पत्ती मुड़ने के लक्षण देखें; केवल लक्षण देखकर दवा न छिड़कें।", "Watch for thrips, mites and leaf-curl symptoms; do not spray based on symptoms alone."),
    GardenGuide("भिंडी", "Okra", "सब्जियाँ", "Vegetables", "गर्म मौसम की फसल; बुवाई से पहले मिट्टी में पर्याप्त नमी रखें।", "A warm-season crop; ensure adequate soil moisture before sowing.", "धूप", "Sun", "अंकुरण और फल बनने के समय नमी की कमी न होने दें।", "Avoid moisture stress during emergence and pod formation.", "फलियों की समय पर तुड़ाई करें। पीली नसों या असामान्य पत्तियों पर रोग पहचान कराएँ।", "Harvest pods on time. Seek diagnosis for yellow-vein or unusual leaf symptoms."),
    GardenGuide("बैंगन", "Brinjal", "सब्जियाँ", "Vegetables", "स्थानीय मौसम के अनुकूल स्वस्थ पौध की रोपाई करें।", "Transplant healthy seedlings suited to the local season.", "धूप", "Sun", "नियमित लेकिन नियंत्रित सिंचाई करें; जलभराव से बचें।", "Irrigate regularly but carefully; avoid waterlogging.", "फल एवं तना छेदक से प्रभावित भाग हटाकर नष्ट करें; खेत की स्वच्छता और निगरानी रखें।", "Remove and destroy fruit and shoot borer-affected parts; maintain field hygiene and monitoring."),
    GardenGuide("पालक", "Spinach", "सब्जियाँ", "Vegetables", "ठंडे मौसम में बेहतर बढ़वार; स्थानीय तापमान के अनुसार बुवाई करें।", "Grows well in cooler weather; sow according to local temperatures.", "धूप से हल्की छाया", "Sun to partial shade", "ऊपरी मिट्टी सूखने न दें; हल्की और समान सिंचाई करें।", "Keep the topsoil from drying; use light, even irrigation.", "पत्तियों की नियमित कटाई करें और खाद अच्छी तरह सड़ी हुई ही मिलाएँ।", "Harvest leaves regularly and use only well-decomposed compost."),
    GardenGuide("गेंदा", "Marigold", "फूल", "Flowers", "मौसम और किस्म के अनुसार नर्सरी पौध लगाएँ; फूलों के लिए उचित दूरी रखें।", "Raise or transplant seedlings according to season and variety; provide spacing.", "धूप", "Sun", "मिट्टी में नमी रखें लेकिन पानी जमा न होने दें।", "Keep soil moist but never waterlogged.", "मुरझाए फूल हटाएँ और पौधों के बीच हवा का आवागमन रखें।", "Remove spent blooms and maintain airflow between plants."),
    GardenGuide("गुलाब", "Rose", "फूल", "Flowers", "गर्म क्षेत्रों में अत्यधिक गर्मी से बचाकर उपयुक्त मौसम में रोपण करें।", "In hot regions, plant in a suitable season and protect from extreme heat.", "सुबह की धूप", "Morning sun", "गमले में जलनिकास अच्छा रखें; मिट्टी की ऊपरी परत देखकर पानी दें।", "Ensure pot drainage; water after checking the topsoil.", "सूखी टहनियाँ हटाएँ, हवा का संचार रखें और पत्ती धब्बा/मिलीबग की निगरानी करें।", "Prune dead stems, maintain airflow and monitor for leaf spot and mealybugs."),
    GardenGuide("चमेली / मोगरा", "Jasmine", "फूल", "Flowers", "स्थानीय गर्म मौसम में अच्छी बढ़वार; पौधे को सहारा और जगह दें।", "Grows well in warm conditions; provide support and space.", "धूप से हल्की छाया", "Sun to partial shade", "गमले की मिट्टी सूखने लगे तो पानी दें; जलभराव न हो।", "Water when potting mix begins to dry; avoid standing water.", "फूल आने के बाद हल्की छँटाई करें और नई बढ़वार पर कीट देखें।", "Prune lightly after flowering and inspect new growth for pests."),
    GardenGuide("तुलसी", "Holy Basil", "जड़ी-बूटी", "Herbs", "गमले या जमीन में लगाया जा सकता है; स्थानीय मौसम में अच्छी धूप दें।", "Can grow in pots or beds; provide good light in local conditions.", "धूप", "Sun", "ऊपरी मिट्टी सूखने पर पानी दें; छोटे गमले जल्दी सूख सकते हैं।", "Water when the topsoil dries; small pots dry faster.", "फूल/बीज के लिए कुछ मंजरी छोड़ें; नियमित हल्की कटाई से पौधा घना रहता है।", "Leave some flower spikes for seed; light regular pinching keeps plants bushy."),
    GardenGuide("पुदीना", "Mint", "जड़ी-बूटी", "Herbs", "गमले में उगाना बेहतर, क्योंकि यह तेजी से फैल सकता है।", "Best grown in a pot because it can spread rapidly.", "हल्की छाया", "Partial shade", "मिट्टी में समान नमी रखें; तेज गर्मी में अधिक बार जाँचें।", "Keep soil evenly moist; check more often in hot weather.", "फैलाव नियंत्रित करें और समय-समय पर नई मिट्टी/कम्पोस्ट दें।", "Control spreading and refresh the potting mix or compost periodically."),
    GardenGuide("एलोवेरा", "Aloe Vera", "इनडोर / सजावटी", "Indoor / Ornamental", "गमले में अच्छी जलनिकासी वाला माध्यम रखें; पाला और अत्यधिक ठंड से बचाएँ।", "Use a well-draining potting mix; protect from frost and severe cold.", "तेज रोशनी", "Bright light", "मिट्टी पूरी तरह सूखने के बाद ही पानी दें।", "Water only after the potting mix has dried out.", "पानी जमा होने से जड़ सड़ सकती है। पत्तियों में नरमी या कालेपन पर सिंचाई जाँचें।", "Standing water can cause root rot. Check watering if leaves become soft or dark."),
    GardenGuide("मनी प्लांट", "Money Plant / Pothos", "इनडोर / सजावटी", "Indoor / Ornamental", "घर के अंदर उजली अप्रत्यक्ष रोशनी वाली जगह चुनें।", "Choose a bright indoor spot with indirect light.", "अप्रत्यक्ष रोशनी", "Indirect light", "ऊपरी मिट्टी सूखने पर पानी दें; प्लेट में पानी जमा न रहने दें।", "Water when the topsoil dries; do not leave water in the saucer.", "पीली पत्तियाँ अधिक पानी या कम रोशनी का संकेत हो सकती हैं; दोनों स्थितियाँ जाँचें।", "Yellow leaves may indicate overwatering or low light; check both conditions."),
    GardenGuide("स्नेक प्लांट", "Snake Plant", "इनडोर / सजावटी", "Indoor / Ornamental", "गमले में जलनिकास छेद अनिवार्य रखें।", "Use a pot with drainage holes.", "कम से तेज अप्रत्यक्ष रोशनी", "Low to bright indirect light", "मिट्टी सूखने के बाद ही पानी दें; सर्दियों में कम सिंचाई की जरूरत हो सकती है।", "Water only after soil dries; watering needs may reduce in winter.", "अधिक पानी सबसे आम समस्या है। पत्तियों के आधार में गलन देखें।", "Overwatering is a common problem. Check for rot near the leaf base."),
    GardenGuide("गुड़हल", "Hibiscus", "फूल", "Flowers", "गमले या जमीन में धूप वाली जगह पर लगाएँ; गर्मी में तेज लू से बचाएँ।", "Plant in a sunny pot or bed; protect from harsh hot winds.", "धूप", "Sun", "गर्म मौसम में मिट्टी की नमी नियमित जाँचें।", "Check soil moisture regularly in hot weather.", "नई कोंपलों पर माहू/मिलीबग की निगरानी करें और सूखे फूल हटाएँ।", "Monitor new shoots for aphids and mealybugs; remove spent flowers."),
    GardenGuide("रसोई बगीचा", "Kitchen Garden", "घर का बगीचा", "Home gardens", "घर में उपलब्ध धूप और मौसम के अनुसार पालक, मेथी, धनिया, मूली या मौसमी सब्जियाँ चुनें।", "Choose spinach, fenugreek, coriander, radish or seasonal vegetables based on sunlight and local weather.", "कम से कम 4–6 घंटे धूप वाली जगह चुनें; फसल के अनुसार जरूरत बदलती है।", "Choose a spot with about 4–6 hours of sunlight; crop needs vary.", "क्यारी में समान नमी रखें और जल निकास अच्छा रखें।", "Maintain even moisture in beds and ensure good drainage.", "एक साथ बहुत अधिक फसलें न लगाएँ; परिवार की जरूरत और उपलब्ध जगह के अनुसार चरणबद्ध बुवाई करें। कम्पोस्ट पूरी तरह सड़ी हुई हो।", "Avoid overcrowding; stagger sowing according to household needs and available space. Use fully decomposed compost."),
    GardenGuide("छत और ग्रो बैग", "Terrace & Grow Bags", "घर का बगीचा", "Home gardens", "हल्के, मजबूत और जल निकास वाले कंटेनर चुनें; छत की भार क्षमता और पानी निकासी पहले जाँचें।", "Choose lightweight, sturdy containers with drainage; check roof load capacity and water runoff first.", "फसल के अनुसार धूप; गर्म छत पर दोपहर की तेज गर्मी से सुरक्षा दें।", "Sunlight depends on crop; protect plants from extreme afternoon heat on hot roofs.", "कंटेनर जल्दी सूख सकते हैं; मिट्टी की नमी देखकर पानी दें, प्लेट में पानी जमा न रहने दें।", "Containers may dry quickly; check moisture and do not leave standing water in trays.", "अच्छा drainage mix, पर्याप्त गहराई और हवा का आवागमन रखें। तेज हवा में पौधों को सहारा दें।", "Use a well-draining mix and suitable container depth; provide airflow and support plants in strong winds."),
    GardenGuide("नर्सरी और पौध तैयार करना", "Nursery & Seedlings", "नर्सरी व प्रवर्धन", "Nursery & propagation", "साफ ट्रे/कंटेनर और रोगमुक्त, स्वच्छ माध्यम का उपयोग करें।", "Use clean trays/containers and a clean, disease-free growing medium.", "अंकुरण के लिए पर्याप्त रोशनी दें; बहुत तेज धूप से नन्हे पौधों को बचाएँ।", "Provide adequate light for germination; protect young seedlings from harsh direct sun.", "माध्यम नम रखें, गीला नहीं; अतिरिक्त पानी निकलने दें।", "Keep the medium moist, not saturated; allow excess water to drain.", "स्वस्थ बीज चुनें, ट्रे साफ रखें और पौधों को बाहर लगाने से पहले धीरे-धीरे बाहरी परिस्थितियों का अभ्यस्त करें।", "Select healthy seed, keep trays clean and harden seedlings gradually before transplanting."),
    GardenGuide("कलम और पौध प्रवर्धन", "Cuttings & Propagation", "नर्सरी व प्रवर्धन", "Nursery & propagation", "कलम/लेयरिंग/ग्राफ्टिंग की विधि पौधे की प्रजाति और किस्म पर निर्भर करती है।", "Cuttings, layering and grafting methods depend on the plant species and variety.", "मूल पौधे की जरूरत के अनुसार उजली छाया या नियंत्रित रोशनी दें।", "Provide bright shade or controlled light as required by the parent plant.", "प्रवर्धन माध्यम में नमी रखें लेकिन सड़न से बचाएँ।", "Maintain moisture in the propagation medium while preventing rot.", "स्वस्थ और सही पहचान वाले मातृ पौधे से सामग्री लें; फलदार पौधों में प्रमाणित नर्सरी और सही rootstock/scion चुनें।", "Take material from healthy, correctly identified mother plants; for fruit trees, use reputable nurseries and suitable rootstock/scion."),
    GardenGuide("फलदार पेड़ों की छँटाई", "Fruit Tree Pruning", "फलदार पौधे", "Fruit plants", "छँटाई का समय और तरीका फल प्रजाति, उम्र और स्थानीय मौसम पर निर्भर करता है।", "Pruning time and method depend on fruit species, tree age and local climate.", "पेड़ को पर्याप्त धूप मिलने दें; घने छत्र में प्रकाश और हवा का रास्ता रखें।", "Allow adequate light; maintain airflow through dense canopies.", "छँटाई के बाद पानी और पोषण का प्रबंधन पेड़ की अवस्था के अनुसार करें।", "Manage water and nutrition after pruning according to tree condition.", "सूखी, टूटी और रोगग्रस्त शाखाएँ हटाने के लिए साफ औजार उपयोग करें। भारी छँटाई से पहले उद्यानिकी विशेषज्ञ से सलाह लें।", "Use clean tools to remove dead, broken and diseased branches. Seek horticulture advice before heavy pruning."),
    GardenGuide("शेड नेट और संरक्षित खेती", "Shade Net & Protected Growing", "संरक्षित खेती", "Protected cultivation", "शेड नेट/लो टनल/पॉलीहाउस का चुनाव फसल, हवा, तापमान, पानी और निवेश क्षमता देखकर करें।", "Choose shade net, low tunnel or polyhouse based on crop, wind, temperature, water and investment capacity.", "संरचना में रोशनी और तापमान फसल के अनुसार नियंत्रित करें।", "Manage light and temperature inside the structure according to the crop.", "ड्रिप या नियंत्रित सिंचाई उपयोगी हो सकती है; अधिक नमी और संघनन से बचें।", "Drip or controlled irrigation may help; avoid excess humidity and condensation.", "वेंटिलेशन, कीट-जाली, स्वच्छता और नियमित निरीक्षण जरूरी हैं। निर्माण से पहले लागत और स्थानीय तकनीकी सलाह लें।", "Ventilation, insect netting, sanitation and regular scouting are important. Assess cost and obtain local technical advice before construction."),
    GardenGuide("इनडोर पौधों की सामान्य देखभाल", "Indoor Plant Care Basics", "इनडोर / सजावटी", "Indoor / Ornamental", "पौधे की प्रजाति के अनुसार गमला और हवा पार होने वाला माध्यम चुनें।", "Choose a pot and airy growing medium suited to the plant species.", "अधिकांश इनडोर पौधों को उजली अप्रत्यक्ष रोशनी चाहिए; हर पौधा कम रोशनी सहन नहीं करता।", "Many indoor plants prefer bright indirect light; not every plant tolerates low light.", "पानी देने से पहले मिट्टी की नमी जाँचें; एक तय साप्ताहिक नियम सब पर लागू नहीं होता।", "Check potting-mix moisture before watering; one weekly schedule does not fit all plants.", "पत्तियों के नीचे कीट देखें, गमलों के drainage holes खुले रखें और नई खरीदी पौधों को कुछ समय अलग रखें।", "Inspect leaf undersides for pests, keep drainage holes open and isolate newly purchased plants for a short observation period.")
)

@Composable
fun HorticultureScreen(isHindi: Boolean) {
    var search by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") }
    var expandedName by remember { mutableStateOf("") }

    val categories = listOf(
        Triple("all", "सभी", "All"),
        Triple("Fruit plants", "फल", "Fruit"),
        Triple("Vegetables", "सब्जियाँ", "Vegetables"),
        Triple("Flowers", "फूल", "Flowers"),
        Triple("Herbs", "जड़ी-बूटी", "Herbs"),
        Triple("Home gardens", "घर का बगीचा", "Home garden"),
        Triple("Nursery & propagation", "नर्सरी", "Nursery"),
        Triple("Protected cultivation", "संरक्षित खेती", "Protected"),
        Triple("Indoor / Ornamental", "इनडोर", "Indoor")
    )
    val filtered = gardenGuides.filter { guide ->
        val matchesCategory = selectedCategory == "all" || guide.categoryEn == selectedCategory
        val q = search.trim()
        val matchesSearch = q.isBlank() ||
            guide.nameHi.contains(q, ignoreCase = true) ||
            guide.nameEn.contains(q, ignoreCase = true) ||
            guide.categoryHi.contains(q, ignoreCase = true) ||
            guide.categoryEn.contains(q, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            if (isHindi) "बागवानी और गार्डन" else "Horticulture & Gardening",
            fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color(0xFF203128)
        )
        Text(
            if (isHindi) "फल, सब्जियाँ, फूल, जड़ी-बूटियाँ और घर के पौधों की देखभाल" else "Practical guides for fruit, vegetables, flowers, herbs and home plants",
            fontSize = 13.sp, color = Color(0xFF66766B)
        )
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            placeholder = { Text(if (isHindi) "पौधे का नाम खोजें" else "Search a plant") }
        )
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { (key, hi, en) ->
                FilterChip(
                    selected = selectedCategory == key,
                    onClick = { selectedCategory = key },
                    label = { Text(if (isHindi) hi else en) }
                )
            }
        }
        Text(
            if (isHindi) "${filtered.size} पौधों की जानकारी" else "${filtered.size} plant guides",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF66766B)
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered, key = { it.nameEn }) { guide ->
                val expanded = expandedName == guide.nameEn
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    onClick = { expandedName = if (expanded) "" else guide.nameEn }
                ) {
                    Column(Modifier.fillMaxWidth().padding(15.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(if (isHindi) guide.nameHi else guide.nameEn, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF203128))
                                Text(if (isHindi) guide.categoryHi else guide.categoryEn, fontSize = 12.sp, color = Color(0xFF247A45))
                            }
                            Text(if (expanded) "−" else "+", fontSize = 22.sp, color = Color(0xFF247A45))
                        }
                        if (expanded) {
                            GuideDetailRow(if (isHindi) "मौसम" else "Season", if (isHindi) guide.seasonHi else guide.seasonEn)
                            GuideDetailRow(if (isHindi) "धूप" else "Light", if (isHindi) guide.sunlightHi else guide.sunlightEn)
                            GuideDetailRow(if (isHindi) "पानी" else "Water", if (isHindi) guide.waterHi else guide.waterEn)
                            GuideDetailRow(if (isHindi) "देखभाल" else "Care", if (isHindi) guide.careHi else guide.careEn)
                        } else {
                            Text(if (isHindi) "विस्तृत जानकारी देखने के लिए टैप करें" else "Tap to view growing and care details", fontSize = 12.sp, color = Color(0xFF66766B))
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(6.dp))
                Text(
                    if (isHindi) "ध्यान दें: यह सामान्य शैक्षिक जानकारी है। किस्म, जिला, मौसम, मिट्टी और स्थानीय परिस्थितियों के अनुसार सलाह बदल सकती है। व्यावसायिक बागवानी से पहले स्थानीय कृषि विज्ञान केंद्र या उद्यानिकी विशेषज्ञ से पुष्टि करें।" else "Note: These are general educational guides. Advice varies by variety, district, season, soil and local conditions. Confirm commercial horticulture decisions with your local KVK or horticulture expert.",
                    fontSize = 11.sp, color = Color(0xFF66766B), lineHeight = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun GuideDetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFF247A45))
        Text(value, fontSize = 13.sp, color = Color(0xFF394A40), lineHeight = 18.sp)
    }
}
