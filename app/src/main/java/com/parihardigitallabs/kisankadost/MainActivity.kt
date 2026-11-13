package com.parihardigitallabs.kisankadost

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel

private val Forest = Color(0xFF1F6842)
private val ForestDark = Color(0xFF174B32)
private val Leaf = Color(0xFF6D9F58)
private val Harvest = Color(0xFFF1C66D)
private val WarmWhite = Color(0xFFF5F7F2)
private val Ink = Color(0xFF1D3026)
private val Muted = Color(0xFF68786E)
private val Line = Color(0xFFE5EBE5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KisanKaDostApp() }
    }
}

private data class Copy(
    val greeting: String, val subtitle: String, val weather: String,
    val weatherHint: String, val explore: String, val tip: String,
    val tipText: String, val home: String, val diary: String,
    val learn: String, val settings: String, val crops: String,
    val plants: String, val animals: String, val trees: String,
    val soil: String, val water: String, val schemes: String, val coming: String,
    val language: String, val about: String, val tagline: String
)

private val hi = Copy(
    "नमस्ते किसान भाई!", "खेती, बागवानी और पशुपालन — सब एक जगह",
    "मौसम", "मौसम सेवा अभी जुड़ी नहीं है",
    "आप क्या करना चाहते हैं?", "खेती का सुरक्षा सुझाव",
    "फसल में कोई भी खाद या दवा डालने से पहले मिट्टी की जरूरत, उत्पाद का लेबल और स्थानीय कृषि विशेषज्ञ की सलाह जरूर देखें।",
    "होम", "खेत डायरी", "सीखें", "सेटिंग्स",
    "फसल देखभाल", "पौधा डॉक्टर", "पशुपालन", "बागवानी व गार्डन", "मिट्टी और खाद", "सिंचाई और जल संरक्षण",
    "सरकारी योजनाएं", "यह सुविधा अगले चरण में जोड़ी जाएगी",
    "भाषा", "ऐप के बारे में", "खेती • बागवानी • पशुपालन"
)
private val en = Copy(
    "Namaste, Kisan!", "Farming, horticulture and animal care — together",
    "Weather", "A live weather service is not connected yet",
    "What would you like to do?", "A safer farming reminder",
    "Before applying fertilizer or pesticides, check the crop's needs, product label and advice from a local agricultural expert.",
    "Home", "Farm Diary", "Learn", "Settings",
    "Crop Care", "Plant Doctor", "Animal Care", "Horticulture & Garden", "Soil & Fertilizer", "Irrigation & Water Saving",
    "Government Schemes", "This feature will be added in a future milestone",
    "Language", "About the app", "Farming • Horticulture • Animal Husbandry"
)

private data class Feature(
    val titleHi: String,
    val titleEn: String,
    val icon: ImageVector,
    val route: String,
    val tint: Color
)

@Composable
private fun KisanKaDostApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("kisan_preferences", Context.MODE_PRIVATE) }
    var isHindi by remember { mutableStateOf(prefs.getBoolean("is_hindi", true)) }
    val c = if (isHindi) hi else en
    val navController = rememberNavController()
    val farmerSetupViewModel: FarmerSetupViewModel = viewModel()
    val animalRegistryViewModel: AnimalRegistryViewModel = viewModel()
    val diaryViewModel: DiaryViewModel = viewModel()

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Forest,
            onPrimary = Color.White,
            primaryContainer = Color(0xFFE1EFE5),
            onPrimaryContainer = ForestDark,
            secondary = Leaf,
            tertiary = Harvest,
            background = WarmWhite,
            surface = Color.White,
            surfaceVariant = Color(0xFFEEF2ED),
            onSurface = Ink,
            onSurfaceVariant = Muted,
            outline = Line
        )
    ) {
        Scaffold(
            containerColor = WarmWhite,
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 4.dp
                ) {
                    val items = listOf(
                        Triple("home", c.home, Icons.Default.Home),
                        Triple("diary", c.diary, Icons.Default.MenuBook),
                        Triple("learn", c.learn, Icons.Default.School),
                        Triple("settings", c.settings, Icons.Default.Settings)
                    )
                    val current = navController.currentBackStackEntryAsState().value?.destination?.route
                    items.forEach { (route, label, icon) ->
                        NavigationBarItem(
                            selected = current == route,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label, maxLines = 1) }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(padding)
            ) {
                composable("home") {
                    HomeScreen(c, isHindi, onFeature = { navController.navigate(it) })
                }
                composable("diary") { DiaryScreen(isHindi = isHindi, viewModel = diaryViewModel) }
                composable("learn") {
                    LearningHubScreen(isHindi) { navController.navigate(it) }
                }
                composable("myfarm") { MyFarmScreen(isHindi = isHindi, viewModel = farmerSetupViewModel) }
                composable("animal-records") { AnimalRegistryScreen(isHindi = isHindi, viewModel = animalRegistryViewModel) }
                composable("settings") {
                    SettingsScreen(c, isHindi, onLanguageChange = {
                        isHindi = it
                        prefs.edit().putBoolean("is_hindi", it).apply()
                    })
                }
                listOf("crops", "calendar", "plants", "animals", "trees", "soil", "water", "protection", "harvest", "schemes").forEach { route ->
                    composable(route) {
                        when (route) {
                            "animals" -> AnimalCareScreen(isHindi = isHindi, onManageRecords = { navController.navigate("animal-records") })
                            "crops" -> CropCareScreen(isHindi = isHindi)
                            "calendar" -> CropCalendarScreen(isHindi = isHindi)
                            "soil" -> SoilHealthScreen(isHindi = isHindi)
                            "water" -> IrrigationScreen(isHindi = isHindi)
                            "protection" -> CropStageScreen(isHindi = isHindi)
                            "harvest" -> HarvestPostHarvestScreen(isHindi = isHindi)
                            "plants" -> PlantDoctorScreen(isHindi = isHindi)
                            "trees" -> HorticultureScreen(isHindi = isHindi)
                            "schemes" -> SchemesInfoScreen(isHindi = isHindi)
                            else -> PlaceholderScreen(c.schemes, c.coming, Icons.Default.AccountBalance)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(c: Copy, isHindi: Boolean, onFeature: (String) -> Unit) {
    val pillars = listOf(
        Feature("खेती", "Field crops", Icons.Default.Grass, "crops", Color(0xFFE6F1E7)),
        Feature("बागवानी", "Horticulture", Icons.Default.Park, "trees", Color(0xFFEAF1E4)),
        Feature("पशुपालन", "Animal husbandry", Icons.Default.Pets, "animals", Color(0xFFFFF1D9))
    )
    val tools = listOf(
        Feature("फसल कैलेंडर", "Crop calendar", Icons.Default.EventNote, "calendar", Color(0xFFEAF0FA)),
        Feature("पौधा डॉक्टर", "Plant Doctor", Icons.Default.Spa, "plants", Color(0xFFEAF3DF)),
        Feature("मिट्टी और खाद", "Soil & nutrients", Icons.Default.Grain, "soil", Color(0xFFF5EBDD)),
        Feature("सिंचाई और जल", "Irrigation & water", Icons.Default.WaterDrop, "water", Color(0xFFE1F1F7)),
        Feature("फसल सुरक्षा", "Crop protection", Icons.Default.Shield, "protection", Color(0xFFF3E9E5)),
        Feature("कटाई और भंडारण", "Harvest & storage", Icons.Default.Inventory2, "harvest", Color(0xFFF2EBDD)),
        Feature("सरकारी योजनाएं", "Government schemes", Icons.Default.AccountBalance, "schemes", Color(0xFFE8EAF5)),
        Feature("मेरा खेत", "My Farm", Icons.Default.Agriculture, "myfarm", Color(0xFFE3F0E7))
    )

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(if (isHindi) "किसान का दोस्त" else "Kisan Ka Dost", color = Forest, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(c.greeting, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
                Text(c.subtitle, fontSize = 13.sp, color = Muted)
            }
            Box(
                Modifier.size(52.dp).clip(CircleShape).background(Color(0xFFE3EFE5)),
                contentAlignment = Alignment.Center
            ) { Text("🌾", fontSize = 27.sp) }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = ForestDark),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color.White.copy(alpha = 0.14f), shape = RoundedCornerShape(50)) {
                        Text(
                            if (isHindi) "आपका खेती साथी" else "YOUR FARM COMPANION",
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.Eco, contentDescription = null, tint = Harvest, modifier = Modifier.size(30.dp))
                }
                Text(
                    if (isHindi) "खेत से पशुधन तक, हर कदम पर साथ।" else "From your fields to your livestock, help at every step.",
                    color = Color.White,
                    fontSize = 20.sp,
                    lineHeight = 27.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (isHindi) "जानकारी पढ़ें, रिकॉर्ड रखें और अपने रोज़ के काम व्यवस्थित करें।" else "Learn, keep records and organise everyday farm work.",
                    color = Color.White.copy(alpha = 0.82f),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Button(
                        onClick = { onFeature("myfarm") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = ForestDark),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isHindi) "मेरा खेत" else "My Farm")
                    }
                    OutlinedButton(
                        onClick = { onFeature("diary") },
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color.White.copy(alpha = 0.65f))),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isHindi) "डायरी खोलें" else "Open diary")
                    }
                }
            }
        }

        SectionHeading(if (isHindi) "आपकी खेती के तीन आधार" else "Three pillars of your farm", if (isHindi) "अपनी जरूरत का विभाग चुनें" else "Choose the area you need")
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            pillars.forEach { feature ->
                PillarCard(feature, isHindi) { onFeature(feature.route) }
            }
        }

        SectionHeading(if (isHindi) "रोज़मर्रा के उपयोगी टूल" else "Everyday farm tools", if (isHindi) "सीखें, जांचें और योजना बनाएं" else "Learn, check and plan")
        tools.chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                pair.forEach { feature ->
                    CompactFeatureCard(feature, isHindi, Modifier.weight(1f)) { onFeature(feature.route) }
                }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4DE)),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)
        ) {
            Row(Modifier.padding(15.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFF9B6B12))
                Spacer(Modifier.width(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(c.tip, fontWeight = FontWeight.Bold, color = Ink)
                    Text(c.tipText, fontSize = 12.sp, lineHeight = 18.sp, color = Color(0xFF62543A))
                }
            }
        }
    }
}

@Composable
private fun SectionHeading(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
        Text(subtitle, fontSize = 12.sp, color = Muted)
    }
}

@Composable
private fun PillarCard(feature: Feature, isHindi: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Box(
                Modifier.size(48.dp).clip(RoundedCornerShape(15.dp)).background(feature.tint),
                contentAlignment = Alignment.Center
            ) { Icon(feature.icon, contentDescription = null, tint = Forest, modifier = Modifier.size(26.dp)) }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(if (isHindi) feature.titleHi else feature.titleEn, fontWeight = FontWeight.Bold, color = Ink, fontSize = 16.sp)
                Text(
                    when (feature.route) {
                        "crops" -> if (isHindi) "फसल की जानकारी और मौसम अनुसार योजना" else "Crop guidance and seasonal planning"
                        "trees" -> if (isHindi) "फल, सब्ज़ी, फूल और पौधों की देखभाल" else "Fruit, vegetables, flowers and plant care"
                        else -> if (isHindi) "पशु देखभाल और व्यक्तिगत रिकॉर्ड" else "Animal care and individual records"
                    },
                    fontSize = 11.sp,
                    color = Muted
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Muted)
        }
    }
}

@Composable
private fun CompactFeatureCard(feature: Feature, isHindi: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(112.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Box(
                Modifier.size(36.dp).clip(RoundedCornerShape(11.dp)).background(feature.tint),
                contentAlignment = Alignment.Center
            ) { Icon(feature.icon, contentDescription = null, tint = Forest, modifier = Modifier.size(21.dp)) }
            Text(
                if (isHindi) feature.titleHi else feature.titleEn,
                fontWeight = FontWeight.SemiBold,
                color = Ink,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun LearningHubScreen(isHindi: Boolean, onFeature: (String) -> Unit) {
    val topics = listOf(
        Feature("फसल की जानकारी", "Crop guides", Icons.Default.Grass, "crops", Color(0xFFE6F1E7)),
        Feature("फसल कैलेंडर", "Crop calendar", Icons.Default.EventNote, "calendar", Color(0xFFEAF0FA)),
        Feature("पौधों की जांच", "Plant health", Icons.Default.Spa, "plants", Color(0xFFEAF3DF)),
        Feature("बागवानी", "Horticulture", Icons.Default.Park, "trees", Color(0xFFE4F0E8)),
        Feature("पशुपालन", "Animal care", Icons.Default.Pets, "animals", Color(0xFFFFF0D8)),
        Feature("मिट्टी और पोषण", "Soil health", Icons.Default.Grain, "soil", Color(0xFFF5EBDD)),
        Feature("पानी और सिंचाई", "Water management", Icons.Default.WaterDrop, "water", Color(0xFFE1F1F7)),
        Feature("फसल सुरक्षा", "Crop protection", Icons.Default.Shield, "protection", Color(0xFFF3E9E5)),
        Feature("कटाई के बाद", "Harvest & storage", Icons.Default.Inventory2, "harvest", Color(0xFFF2EBDD))
    )
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(if (isHindi) "किसान ज्ञान केंद्र" else "Farmer Learning Centre", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Ink)
        Text(
            if (isHindi) "खेती, बागवानी और पशुपालन की ऑफलाइन जानकारी एक जगह।" else "Practical offline information for crops, horticulture and animal care.",
            fontSize = 13.sp,
            color = Muted
        )
        topics.forEach { feature -> PillarCard(feature, isHindi) { onFeature(feature.route) } }
        Text(
            if (isHindi) "सलाह सामान्य जानकारी के लिए है। स्थानीय फसल और पशु संबंधी निर्णय में कृषि विशेषज्ञ या पशु चिकित्सक की सलाह लें।"
            else "Guides are educational. Consult local agricultural experts or veterinarians for decisions specific to your crop or animal.",
            fontSize = 12.sp,
            color = Muted,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )
    }
}

@Composable
private fun SchemesInfoScreen(isHindi: Boolean) {
    val uriHandler = LocalUriHandler.current
    val resources = listOf(
        Triple("राज किसान साथी पोर्टल", "Rajasthan Raj Kisan portal", "https://rajkisan.rajasthan.gov.in/"),
        Triple("पीएम-किसान पोर्टल", "PM-KISAN portal", "https://pmkisan.gov.in/"),
        Triple("प्रधानमंत्री फसल बीमा योजना", "PM Fasal Bima Yojana", "https://pmfby.gov.in/")
    )
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Text(if (isHindi) "सरकारी योजनाएं और सेवाएं" else "Government schemes & services", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
        Text(
            if (isHindi) "आवेदन, पात्रता और अंतिम तिथि की जानकारी हमेशा संबंधित आधिकारिक पोर्टल पर जांचें। इस ऐप में अभी लाइव पात्रता या आवेदन स्थिति उपलब्ध नहीं है।"
            else "Always verify eligibility, application details and deadlines on the relevant official portal. Live eligibility checks and application status are not available in this app yet.",
            fontSize = 13.sp,
            lineHeight = 19.sp,
            color = Muted
        )
        resources.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { runCatching { uriHandler.openUri(item.third) } },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE8EFE8)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Forest)
                    }
                    Column(Modifier.weight(1f).padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(if (isHindi) item.first else item.second, color = Ink, fontWeight = FontWeight.SemiBold)
                        Text(if (isHindi) "आधिकारिक वेबसाइट खोलें" else "Open official website", color = Muted, fontSize = 11.sp)
                    }
                    Icon(Icons.Default.OpenInNew, contentDescription = if (isHindi) "वेबसाइट खोलें" else "Open website", tint = Forest)
                }
            }
        }
        Text(
            if (isHindi) "सावधानी: किसी एजेंट को OTP, बैंक PIN या पासवर्ड न दें। सरकारी लाभ के लिए केवल आधिकारिक चैनल का उपयोग करें।"
            else "Safety: Never share OTPs, bank PINs or passwords with agents. Use official channels for government benefits.",
            color = Color(0xFF8A5A1A),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun PlaceholderScreen(title: String, message: String, icon: ImageVector) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.size(82.dp).clip(CircleShape).background(Color(0xFFE4F0E5)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Forest, modifier = Modifier.size(42.dp))
        }
        Spacer(Modifier.height(18.dp))
        Text(title, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.height(8.dp))
        Text(message, color = Muted, fontSize = 14.sp)
    }
}

@Composable
private fun SettingsScreen(c: Copy, isHindi: Boolean, onLanguageChange: (Boolean) -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(c.settings, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Ink)
        Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(c.language, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Ink)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = isHindi,
                        onClick = { if (!isHindi) onLanguageChange(true) },
                        label = { Text("हिन्दी") },
                        leadingIcon = if (isHindi) { { Icon(Icons.Default.Check, contentDescription = null) } } else null
                    )
                    FilterChip(
                        selected = !isHindi,
                        onClick = { if (isHindi) onLanguageChange(false) },
                        label = { Text("English") },
                        leadingIcon = if (!isHindi) { { Icon(Icons.Default.Check, contentDescription = null) } } else null
                    )
                }
                Text(
                    if (isHindi) "ऐप पहली बार हिन्दी में खुलेगा। आप यहाँ से कभी भी English चुन सकते हैं।"
                    else "The app starts in Hindi on a fresh install. You can switch back to Hindi anytime.",
                    color = Muted,
                    fontSize = 12.sp
                )
            }
        }
        Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(c.about, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Ink)
                Text(if (isHindi) "किसान का दोस्त" else "Kisan Ka Dost", color = Forest, fontWeight = FontWeight.Bold)
                Text(c.tagline, color = Muted, fontSize = 13.sp)
                Text("Parihar Digital Labs  •  Version 1.0.0", color = Muted, fontSize = 12.sp)
                HorizontalDivider(color = Line)
                Text(
                    if (isHindi) "यह ऐप किसानों के लिए निःशुल्क है। आपके रिकॉर्ड डिफ़ॉल्ट रूप से इसी फोन में रहते हैं।"
                    else "This app is free for farmers. Your records stay on this phone by default.",
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
