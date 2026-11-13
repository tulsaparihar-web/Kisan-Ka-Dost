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
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parihardigitallabs.kisankadost.data.FarmerProfileEntity

private val FarmGreen = Color(0xFF247A45)
private val FarmInk = Color(0xFF203128)
private val MutedText = Color(0xFF66766B)

@Composable
fun MyFarmScreen(
    isHindi: Boolean,
    viewModel: FarmerSetupViewModel
) {
    val savedProfile by viewModel.profile.collectAsState()
    var loaded by remember { mutableStateOf(false) }

    var farmerName by rememberSaveable { mutableStateOf("") }
    var mobileNumber by rememberSaveable { mutableStateOf("") }
    var village by rememberSaveable { mutableStateOf("") }
    var tehsil by rememberSaveable { mutableStateOf("") }
    var district by rememberSaveable { mutableStateOf("") }
    var landArea by rememberSaveable { mutableStateOf("") }
    var landUnit by rememberSaveable { mutableStateOf("Bigha") }
    var irrigationSource by rememberSaveable { mutableStateOf("Canal") }
    var mainCrops by rememberSaveable { mutableStateOf("") }
    var livestock by rememberSaveable { mutableStateOf("") }
    var saveMessage by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(savedProfile) {
        val profile = savedProfile
        if (profile != null && !loaded) {
            farmerName = profile.farmerName
            mobileNumber = profile.mobileNumber
            village = profile.village
            tehsil = profile.tehsil
            district = profile.district
            landArea = profile.landArea
            landUnit = profile.landUnit
            irrigationSource = profile.irrigationSource
            mainCrops = profile.mainCrops
            livestock = profile.livestock
            loaded = true
        }
    }

    val units = listOf(
        "Bigha" to if (isHindi) "बीघा" else "Bigha",
        "Acre" to if (isHindi) "एकड़" else "Acre",
        "Hectare" to if (isHindi) "हेक्टेयर" else "Hectare"
    )
    val waterSources = listOf(
        "Canal" to if (isHindi) "नहर" else "Canal",
        "Tubewell" to if (isHindi) "ट्यूबवेल" else "Tubewell",
        "Well" to if (isHindi) "कुआँ" else "Well",
        "Rainfed" to if (isHindi) "वर्षा आधारित" else "Rainfed"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Text(
            if (isHindi) "मेरा खेत" else "My Farm",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = FarmInk
        )
        Text(
            if (isHindi) "अपनी जानकारी भरें ताकि सलाह आपकी खेती के अनुसार हो।" else "Add your details so that guidance can match your farm.",
            color = MutedText,
            fontSize = 13.sp
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF3E9)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Icon(
                    Icons.Default.CloudOff,
                    contentDescription = null,
                    tint = FarmGreen
                )
                Spacer(Modifier.padding(start = 5.dp))
                Text(
                    if (isHindi) "आपकी जानकारी इसी फोन में ऑफलाइन सुरक्षित रहेगी।" else "Your information is saved locally on this phone and works offline.",
                    color = FarmInk,
                    fontSize = 12.sp
                )
            }
        }

        FormSectionTitle(if (isHindi) "किसान की जानकारी" else "Farmer Details")
        OutlinedTextField(
            value = farmerName,
            onValueChange = { farmerName = it; saveMessage = "" },
            label = { Text(if (isHindi) "किसान का नाम *" else "Farmer name *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it.filter(Char::isDigit).take(10); saveMessage = "" },
            label = { Text(if (isHindi) "मोबाइल नंबर" else "Mobile number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        FormSectionTitle(if (isHindi) "खेत का स्थान" else "Farm Location")
        OutlinedTextField(
            value = village,
            onValueChange = { village = it; saveMessage = "" },
            label = { Text(if (isHindi) "गाँव" else "Village") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = tehsil,
            onValueChange = { tehsil = it; saveMessage = "" },
            label = { Text(if (isHindi) "तहसील" else "Tehsil") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = district,
            onValueChange = { district = it; saveMessage = "" },
            label = { Text(if (isHindi) "जिला *" else "District *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        FormSectionTitle(if (isHindi) "खेत का विवरण" else "Farm Details")
        OutlinedTextField(
            value = landArea,
            onValueChange = { landArea = it; saveMessage = "" },
            label = { Text(if (isHindi) "कुल भूमि क्षेत्र" else "Total land area") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Text(if (isHindi) "भूमि की इकाई" else "Land unit", color = MutedText, fontSize = 13.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            units.forEach { (value, label) ->
                FilterChip(
                    selected = landUnit == value,
                    onClick = { landUnit = value },
                    label = { Text(label) }
                )
            }
        }

        Text(if (isHindi) "सिंचाई का मुख्य स्रोत" else "Main irrigation source", color = MutedText, fontSize = 13.sp)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            waterSources.chunked(2).forEach { group ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    group.forEach { (value, label) ->
                        FilterChip(
                            selected = irrigationSource == value,
                            onClick = { irrigationSource = value },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }

        FormSectionTitle(if (isHindi) "फसल और पशुधन" else "Crops & Livestock")
        OutlinedTextField(
            value = mainCrops,
            onValueChange = { mainCrops = it; saveMessage = "" },
            label = { Text(if (isHindi) "मुख्य फसलें" else "Main crops") },
            placeholder = { Text(if (isHindi) "जैसे: गेहूँ, सरसों, बाजरा" else "e.g. Wheat, mustard, pearl millet") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
        OutlinedTextField(
            value = livestock,
            onValueChange = { livestock = it; saveMessage = "" },
            label = { Text(if (isHindi) "पशुधन / पशु" else "Livestock / animals") },
            placeholder = { Text(if (isHindi) "जैसे: गाय, भैंस, बकरी" else "e.g. Cow, buffalo, goat") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Button(
            onClick = {
                when {
                    farmerName.isBlank() -> saveMessage = if (isHindi) "कृपया किसान का नाम भरें।" else "Please enter the farmer name."
                    district.isBlank() -> saveMessage = if (isHindi) "कृपया जिला भरें।" else "Please enter the district."
                    mobileNumber.isNotBlank() && mobileNumber.length != 10 ->
                        saveMessage = if (isHindi) "मोबाइल नंबर 10 अंकों का होना चाहिए।" else "Mobile number must contain 10 digits."
                    landArea.isNotBlank() && (landArea.toDoubleOrNull()?.let { it.isFinite() && it > 0.0 } != true) ->
                        saveMessage = if (isHindi) "भूमि क्षेत्र शून्य से अधिक सही संख्या में भरें।" else "Enter a valid land area greater than zero."
                    else -> viewModel.save(
                        profile = FarmerProfileEntity(
                            farmerName = farmerName.trim(),
                            mobileNumber = mobileNumber,
                            village = village.trim(),
                            tehsil = tehsil.trim(),
                            district = district.trim(),
                            landArea = landArea.trim(),
                            landUnit = landUnit,
                            irrigationSource = irrigationSource,
                            mainCrops = mainCrops.trim(),
                            livestock = livestock.trim()
                        ),
                        onSaved = {
                            saveMessage = if (isHindi) "जानकारी इस फोन में सुरक्षित कर दी गई है।" else "Your farm details have been saved on this phone."
                        },
                        onError = {
                            saveMessage = if (isHindi) "जानकारी सुरक्षित नहीं हो सकी। कृपया दोबारा प्रयास करें।" else "Could not save your details. Please try again."
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isHindi) "जानकारी सुरक्षित करें" else "Save My Farm")
        }

        if (saveMessage.isNotBlank()) {
            Text(
                saveMessage,
                color = if (saveMessage.contains("saved", true) || saveMessage.contains("सुरक्षित कर")) FarmGreen else MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun FormSectionTitle(title: String) {
    Text(
        title,
        color = FarmGreen,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 5.dp)
    )
}
