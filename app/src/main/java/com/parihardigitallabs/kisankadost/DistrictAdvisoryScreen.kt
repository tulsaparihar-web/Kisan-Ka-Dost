package com.parihardigitallabs.kisankadost

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DistrictAdvisoryScreen(
    isHindi: Boolean,
    viewModel: FarmerSetupViewModel
) {
    val profile by viewModel.profile.collectAsState()
    var manualDistrict by rememberSaveable { mutableStateOf("") }
    val savedDistrict = profile?.district?.trim().orEmpty()
    val selectedDistrict = manualDistrict.trim().ifBlank { savedDistrict }
    val uriHandler = LocalUriHandler.current
    val muted = Color(0xFF66766B)
    val ink = Color(0xFF203128)
    val green = Color(0xFF247A45)

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            if (isHindi) "जिला कृषि सलाह" else "District Farm Advisory",
            fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ink
        )
        Text(
            if (isHindi) "अपने जिले के अनुसार मौसम चेतावनी और कृषि सलाह देखने की तैयारी।"
            else "A district-based place for weather warnings and practical farm advisories.",
            fontSize = 13.sp, color = muted
        )

        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF3E9))) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Icon(Icons.Default.LocationOn, null, tint = green)
                    Text(
                        if (isHindi) "चुना हुआ जिला" else "Selected district",
                        fontWeight = FontWeight.SemiBold, color = ink
                    )
                }
                Text(
                    selectedDistrict.ifBlank {
                        if (isHindi) "अभी जिला उपलब्ध नहीं है" else "No district selected yet"
                    },
                    fontSize = 19.sp, fontWeight = FontWeight.Bold, color = ink
                )
                if (savedDistrict.isNotBlank()) {
                    Text(
                        if (isHindi) "यह जिला आपके 'मेरा खेत' प्रोफाइल से लिया गया है।"
                        else "This district is taken from your My Farm profile.",
                        color = muted, fontSize = 12.sp
                    )
                } else {
                    Text(
                        if (isHindi) "मेरा खेत में जिला सेव करें या नीचे अस्थायी रूप से चुनें।"
                        else "Save a district in My Farm, or enter one below for this visit.",
                        color = muted, fontSize = 12.sp
                    )
                }
            }
        }

        OutlinedTextField(
            value = manualDistrict,
            onValueChange = { manualDistrict = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(if (isHindi) "दूसरा जिला चुनें (वैकल्पिक)" else "Choose another district (optional)") },
            placeholder = { Text(if (isHindi) "जैसे: जयपुर" else "e.g. Jaipur") }
        )
        if (manualDistrict.isNotBlank()) {
            OutlinedButton(onClick = { manualDistrict = "" }, modifier = Modifier.fillMaxWidth()) {
                Text(if (isHindi) "मेरे खेत का सेव किया जिला इस्तेमाल करें" else "Use my saved farm district")
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3D9))) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Icon(Icons.Default.CloudOff, null, tint = Color(0xFF9B6B12))
                    Text(
                        if (isHindi) "लाइव मौसम सेवा अभी जुड़ी नहीं है" else "Live weather service is not connected yet",
                        fontWeight = FontWeight.Bold, color = ink
                    )
                }
                Text(
                    if (isHindi) "इसलिए अभी कोई तापमान, बारिश या चेतावनी लाइव डेटा के रूप में नहीं दिखाई जा रही। आधिकारिक डेटा स्रोत और सुरक्षित एक्सेस की पुष्टि के बाद ही अपडेट जोड़े जाएंगे।"
                    else "No temperature, rainfall or warning is being presented as live data yet. Updates will be added only after official data access and source handling are verified.",
                    color = ink, fontSize = 13.sp, lineHeight = 19.sp
                )
            }
        }

        Text(
            if (isHindi) "आधिकारिक स्रोत" else "Official sources",
            fontSize = 17.sp, fontWeight = FontWeight.Bold, color = ink
        )
        Text(
            if (isHindi) "इन स्रोतों पर उपलब्ध जिलेवार बुलेटिन और मौसम जानकारी देख सकते हैं। ऐप में स्वचालित डेटा लाना अगला तकनीकी चरण है।"
            else "You can consult district bulletins and weather information from these sources. Automated in-app data retrieval is a later integration step.",
            color = muted, fontSize = 12.sp
        )
        Button(
            onClick = { uriHandler.openUri("https://imdagrimet.gov.in/Services/DistrictBulletin.php") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isHindi) "IMD जिला कृषि बुलेटिन खोलें" else "Open IMD District Agromet Bulletin")
            Spacer(Modifier.height(0.dp))
        }
        OutlinedButton(
            onClick = { uriHandler.openUri("https://api.imd.gov.in/public/") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isHindi) "IMD API पोर्टल खोलें" else "Open IMD API Portal")
            androidx.compose.material3.Icon(Icons.Default.OpenInNew, null)
        }
        Spacer(Modifier.height(8.dp))
    }
}
