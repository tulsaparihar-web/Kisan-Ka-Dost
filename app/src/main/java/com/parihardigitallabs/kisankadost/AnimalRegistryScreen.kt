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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parihardigitallabs.kisankadost.data.AnimalRecordEntity

private val RegistryGreen = Color(0xFF247A45)
private val RegistryInk = Color(0xFF203128)

@Composable
fun AnimalRegistryScreen(isHindi: Boolean, viewModel: AnimalRegistryViewModel) {
    val records by viewModel.records.collectAsState()
    var name by rememberSaveable { mutableStateOf("") }
    var species by rememberSaveable { mutableStateOf("Cattle") }
    var tag by rememberSaveable { mutableStateOf("") }
    var sex by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var purpose by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var pendingDelete by remember { mutableStateOf<AnimalRecordEntity?>(null) }

    val speciesOptions = listOf(
        "Cattle" to "गाय/बैल", "Buffalo" to "भैंस", "Goat" to "बकरी",
        "Sheep" to "भेड़", "Camel" to "ऊँट", "Poultry" to "मुर्गी"
    )
    val speciesLabel: (String) -> String = { key ->
        if (isHindi) speciesOptions.firstOrNull { it.first == key }?.second ?: key else key
    }

    Column(
        Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(if (isHindi) "मेरे पशु" else "My Animals", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = RegistryInk)
        Text(
            if (isHindi) "अपने पशुओं का निजी रिकॉर्ड इस फोन में ऑफलाइन रखें।" else "Keep private animal records on this phone, available offline.",
            fontSize = 13.sp, color = Color(0xFF66766B)
        )
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF3E9))) {
            Text(
                if (isHindi) "नाम, पहचान टैग और सामान्य विवरण दर्ज करें। स्वास्थ्य/टीकाकरण की तारीख केवल पशु चिकित्सक से पुष्टि के बाद अपनी डायरी में जोड़ें।" else
                    "Add a name, identification tag and basic details. Record health or vaccination dates only after confirming them with your veterinarian.",
                Modifier.padding(12.dp), fontSize = 12.sp, lineHeight = 17.sp, color = RegistryInk
            )
        }
        Text(if (isHindi) "नया पशु जोड़ें" else "Add an animal", fontWeight = FontWeight.Bold, color = RegistryGreen, fontSize = 16.sp)
        OutlinedTextField(
            value = name, onValueChange = { name = it; message = "" },
            label = { Text(if (isHindi) "पशु का नाम *" else "Animal name *") },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
        )
        Text(if (isHindi) "पशु का प्रकार" else "Animal type", fontSize = 12.sp, color = Color(0xFF66766B))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            speciesOptions.chunked(3).forEach { group ->
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    group.forEach { (key, hi) ->
                        FilterChip(
                            selected = species == key,
                            onClick = { species = key },
                            label = { Text(if (isHindi) hi else key) }
                        )
                    }
                }
            }
        }
        OutlinedTextField(value = tag, onValueChange = { tag = it; message = "" },
            label = { Text(if (isHindi) "पहचान टैग / कान नंबर" else "Identification tag / ear number") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        Text(if (isHindi) "लिंग" else "Sex", fontSize = 12.sp, color = Color(0xFF66766B))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Female" to "मादा", "Male" to "नर", "Unknown" to "पता नहीं").forEach { (key, hi) ->
                FilterChip(selected = sex == key, onClick = { sex = key }, label = { Text(if (isHindi) hi else key) })
            }
        }
        OutlinedTextField(value = age, onValueChange = { age = it; message = "" },
            label = { Text(if (isHindi) "उम्र / जन्म तिथि (वैकल्पिक)" else "Age / date of birth (optional)") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = purpose, onValueChange = { purpose = it; message = "" },
            label = { Text(if (isHindi) "उद्देश्य (दूध, प्रजनन, पालन आदि)" else "Purpose (dairy, breeding, rearing etc.)") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = notes, onValueChange = { notes = it; message = "" },
            label = { Text(if (isHindi) "नोट (वैकल्पिक)" else "Notes (optional)") },
            modifier = Modifier.fillMaxWidth(), minLines = 2)
        Button(
            onClick = {
                if (name.isBlank()) {
                    message = if (isHindi) "कृपया पशु का नाम भरें।" else "Please enter an animal name."
                } else {
                    viewModel.add(
                        AnimalRecordEntity(
                            name = name.trim(), species = species, tagNumber = tag.trim(),
                            sex = sex, ageOrDob = age.trim(), purpose = purpose.trim(), notes = notes.trim()
                        ),
                        onSaved = {
                            name = ""; tag = ""; sex = ""; age = ""; purpose = ""; notes = ""
                            message = if (isHindi) "पशु का रिकॉर्ड सुरक्षित है।" else "Animal record saved."
                        },
                        onError = { message = if (isHindi) "रिकॉर्ड सुरक्षित नहीं हो सका। दोबारा प्रयास करें।" else "Could not save the record. Please try again." }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Icon(Icons.Default.Pets, contentDescription = null); Spacer(Modifier.padding(3.dp)); Text(if (isHindi) "पशु सुरक्षित करें" else "Save animal") }
        if (message.isNotBlank()) Text(message, color = if (message.contains("saved", true) || message.contains("सुरक्षित")) RegistryGreen else MaterialTheme.colorScheme.error, fontSize = 12.sp)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(if (isHindi) "मेरे पशुओं की सूची" else "Animal list", fontWeight = FontWeight.Bold, color = RegistryInk)
            Text(if (isHindi) "कुल: ${records.size}" else "Total: ${records.size}", color = RegistryGreen, fontSize = 13.sp)
        }
        if (records.isEmpty()) {
            Text(if (isHindi) "अभी कोई पशु दर्ज नहीं है। ऊपर से पहला रिकॉर्ड जोड़ें।" else "No animals recorded yet. Add your first animal above.", color = Color(0xFF66766B), fontSize = 13.sp)
        } else {
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(records, key = { it.id }) { record ->
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)) {
                        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(record.name, fontWeight = FontWeight.Bold, color = RegistryInk, fontSize = 16.sp)
                                Text(speciesLabel(record.species) + if (record.tagNumber.isNotBlank()) " • ${record.tagNumber}" else "", color = RegistryGreen, fontSize = 12.sp)
                                val details = listOf(record.sex.takeIf { it.isNotBlank() }?.let { if (isHindi) if (it == "Female") "मादा" else if (it == "Male") "नर" else "अज्ञात" else it }, record.ageOrDob, record.purpose).filterNotNull().filter { it.isNotBlank() }
                                if (details.isNotEmpty()) Text(details.joinToString(" • "), fontSize = 12.sp, color = Color(0xFF66766B))
                                if (record.notes.isNotBlank()) Text(record.notes, fontSize = 12.sp, color = RegistryInk)
                            }
                            IconButton(onClick = { pendingDelete = record }) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = if (isHindi) "हटाएँ" else "Delete", tint = Color(0xFF9B3D32))
                            }
                        }
                    }
                }
            }
        }
    }
    pendingDelete?.let { record ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text(if (isHindi) "पशु रिकॉर्ड हटाएँ?" else "Delete animal record?") },
            text = { Text(if (isHindi) "${record.name} का रिकॉर्ड इस फोन से हट जाएगा।" else "The record for ${record.name} will be removed from this phone.") },
            confirmButton = {
                TextButton(onClick = { viewModel.delete(record); pendingDelete = null }) {
                    Text(if (isHindi) "हटाएँ" else "Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { pendingDelete = null }) { Text(if (isHindi) "रद्द करें" else "Cancel") } }
        )
    }
}
