package com.parihardigitallabs.kisankadost

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*

private val Forest = Color(0xFF247A45)
private val Leaf = Color(0xFF77B85A)
private val Harvest = Color(0xFFF2C66D)
private val WarmWhite = Color(0xFFF7F6EF)
private val Ink = Color(0xFF203128)

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
    val soil: String, val schemes: String, val coming: String,
    val language: String, val about: String, val tagline: String
)

private val hi = Copy(
    "नमस्ते किसान भाई!", "आपका अपना खेती और पशुपालन साथी",
    "आज का मौसम", "मौसम अपडेट के लिए इंटरनेट कनेक्शन उपलब्ध होने पर सेवा जोड़ेंगे",
    "क्या जानना चाहेंगे?", "आज की खेती सलाह",
    "फसल में कोई भी खाद या दवा डालने से पहले स्थानीय कृषि विशेषज्ञ की सलाह और उत्पाद का लेबल अवश्य पढ़ें।",
    "होम", "खेत डायरी", "जानकारी", "सेटिंग्स",
    "फसल देखभाल", "पौधे", "पशुपालन", "पेड़-पौधे", "मिट्टी और खाद",
    "सरकारी योजनाएं", "यह सुविधा अगले चरण में जोड़ी जाएगी",
    "भाषा", "ऐप के बारे में", "हर किसान का अपना डिजिटल साथी"
)
private val en = Copy(
    "Namaste, Kisan!", "Your companion for farming and animal care",
    "Today's weather", "Weather updates will be available when an internet service is added",
    "What would you like to explore?", "Farming tip of the day",
    "Before applying fertilizer or pesticides, check the product label and consult a local agricultural expert.",
    "Home", "Farm Diary", "Learn", "Settings",
    "Crop Care", "Plants", "Animal Care", "Trees", "Soil & Fertilizer",
    "Government Schemes", "This feature will be added in a future milestone",
    "Language", "About the app", "A digital companion for every farmer"
)

private data class Feature(val titleHi: String, val titleEn: String, val icon: ImageVector, val route: String, val tint: Color)

@Composable
private fun KisanKaDostApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("kisan_preferences", Context.MODE_PRIVATE) }
    var isHindi by remember { mutableStateOf(prefs.getBoolean("is_hindi", true)) }
    val c = if (isHindi) hi else en
    val navController = rememberNavController()

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Forest, onPrimary = Color.White, secondary = Leaf,
            background = WarmWhite, surface = Color.White, onSurface = Ink
        )
    ) {
        Scaffold(
            containerColor = WarmWhite,
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
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
                composable("diary") { PlaceholderScreen(if (isHindi) "खेत डायरी" else "Farm Diary", c.coming, Icons.Default.EditNote) }
                composable("learn") { PlaceholderScreen(if (isHindi) "किसान ज्ञान" else "Farmer Knowledge", c.coming, Icons.Default.MenuBook) }
                composable("settings") {
                    SettingsScreen(c, isHindi, onLanguageChange = {
                        isHindi = it
                        prefs.edit().putBoolean("is_hindi", it).apply()
                    })
                }
                listOf("crops", "plants", "animals", "trees", "soil", "schemes").forEach { route ->
                    composable(route) {
                        val title = when (route) {
                            "crops" -> c.crops
                            "plants" -> c.plants
                            "animals" -> c.animals
                            "trees" -> c.trees
                            "soil" -> c.soil
                            else -> c.schemes
                        }
                        PlaceholderScreen(title, c.coming, Icons.Default.Agriculture)
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(c: Copy, isHindi: Boolean, onFeature: (String) -> Unit) {
    val features = listOf(
        Feature("फसल देखभाल", "Crop Care", Icons.Default.Grass, "crops", Color(0xFFE5F2E6)),
        Feature("पौधे", "Plants", Icons.Default.Spa, "plants", Color(0xFFEAF3DF)),
        Feature("पशुपालन", "Animal Care", Icons.Default.Pets, "animals", Color(0xFFFFF0D8)),
        Feature("पेड़-पौधे", "Trees", Icons.Default.Park, "trees", Color(0xFFE4F0E8)),
        Feature("मिट्टी और खाद", "Soil & Fertilizer", Icons.Default.Grain, "soil", Color(0xFFF5EBDD)),
        Feature("सरकारी योजनाएं", "Government Schemes", Icons.Default.AccountBalance, "schemes", Color(0xFFE8EAF5))
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(c.greeting, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
                Text(c.subtitle, fontSize = 13.sp, color = Color(0xFF66766B))
            }
            Box(
                Modifier.size(46.dp).clip(CircleShape).background(Color(0xFFE4F0E5)),
                contentAlignment = Alignment.Center
            ) { Text("🌾", fontSize = 25.sp) }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = Forest),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(c.weather, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(c.weatherHint, color = Color.White.copy(alpha = 0.86f), fontSize = 12.sp, lineHeight = 17.sp)
                }
                Icon(Icons.Default.WbSunny, null, tint = Harvest, modifier = Modifier.size(42.dp))
            }
        }
        Text(c.explore, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 4.dp)
        ) { items(features) { feature -> FeatureCard(feature, isHindi) { onFeature(feature.route) } } }
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3D9)),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        ) {
            Row(Modifier.padding(15.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Lightbulb, null, tint = Color(0xFF9B6B12))
                Spacer(Modifier.width(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(c.tip, fontWeight = FontWeight.Bold, color = Ink)
                    Text(c.tipText, fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF62543A))
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(feature: Feature, isHindi: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(126.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Box(
                Modifier.size(43.dp).clip(RoundedCornerShape(14.dp)).background(feature.tint),
                contentAlignment = Alignment.Center
            ) { Icon(feature.icon, null, tint = Forest, modifier = Modifier.size(25.dp)) }
            Text(if (isHindi) feature.titleHi else feature.titleEn, fontWeight = FontWeight.SemiBold, color = Ink, fontSize = 14.sp)
        }
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
        Text(message, color = Color(0xFF66766B), fontSize = 14.sp)
    }
}

@Composable
private fun SettingsScreen(c: Copy, isHindi: Boolean, onLanguageChange: (Boolean) -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Text(c.settings, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Ink)
        Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(c.language, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("हिन्दी", Modifier.weight(1f))
                    Switch(checked = !isHindi, onCheckedChange = { onLanguageChange(!it) })
                    Text("English", Modifier.padding(start = 10.dp))
                }
                Text(
                    if (isHindi) "भाषा बदलने के लिए स्विच का उपयोग करें।" else "Use the switch to change the app language.",
                    color = Color(0xFF66766B), fontSize = 12.sp
                )
            }
        }
        Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(c.about, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("Kisan Ka Dost", color = Forest, fontWeight = FontWeight.Bold)
                Text(c.tagline, color = Color(0xFF66766B), fontSize = 13.sp)
                Text("Parihar Digital Labs  •  Version 1.0.0", color = Color(0xFF66766B), fontSize = 12.sp)
            }
        }
    }
}
