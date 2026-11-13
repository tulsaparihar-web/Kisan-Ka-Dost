package com.parihardigitallabs.kisankadost

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parihardigitallabs.kisankadost.data.AnimalRecordEntity
import com.parihardigitallabs.kisankadost.data.AnimalHealthRecordEntity
import androidx.core.content.ContextCompat
import java.time.LocalDate

private val RegistryGreen = Color(0xFF247A45)
private val RegistryInk = Color(0xFF203128)

@Composable
fun AnimalRegistryScreen(isHindi: Boolean, viewModel: AnimalRegistryViewModel) {
    val records by viewModel.records.collectAsState()
    val healthRecords by viewModel.healthRecords.collectAsState()
    val context = LocalContext.current
    var pendingReminderId by remember { mutableStateOf<Long?>(null) }
    var pendingReminderDate by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var species by rememberSaveable { mutableStateOf("Cattle") }
    var tag by rememberSaveable { mutableStateOf("") }
    var sex by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var purpose by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var pendingDelete by remember { mutableStateOf<AnimalRecordEntity?>(null) }
    var editingRecord by remember { mutableStateOf<AnimalRecordEntity?>(null) }
    var healthAnimal by remember { mutableStateOf<AnimalRecordEntity?>(null) }
    var healthTitle by rememberSaveable { mutableStateOf("") }
    var healthDate by rememberSaveable { mutableStateOf("") }
    var nextReminderDate by rememberSaveable { mutableStateOf("") }
    var vetConfirmed by rememberSaveable { mutableStateOf(false) }
    var healthNotes by rememberSaveable { mutableStateOf("") }
    var healthError by remember { mutableStateOf("") }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val id = pendingReminderId
        if (granted && id != null && pendingReminderDate.isNotBlank()) {
            AnimalReminderScheduler.schedule(context, id, pendingReminderDate)
            viewModel.rescheduleReminders()
            message = if (isHindi) "अगली तारीख के लिए सूचना सेट हो गई।" else "Notification reminder scheduled."
        } else if (!granted) {
            message = if (isHindi) "रिकॉर्ड सुरक्षित है, लेकिन सूचना अनुमति बंद है। सेटिंग्स में अनुमति दें।" else "Record saved, but notifications are disabled. Allow them in app settings."
        }
        pendingReminderId = null
        pendingReminderDate = ""
    }

    LaunchedEffect(Unit) {
        val allowed = Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        if (allowed) viewModel.rescheduleReminders()
    }

    val speciesOptions = listOf(
        "Cattle" to "गाय/बैल", "Buffalo" to "भैंस", "Goat" to "बकरी",
        "Sheep" to "भेड़", "Camel" to "ऊँट", "Poultry" to "मुर्गी"
    )
    val speciesLabel: (String) -> String = { key ->
        if (isHindi) speciesOptions.firstOrNull { it.first == key }?.second ?: key else key
    }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 12.dp),
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
                    val existing = editingRecord
                    val draft = AnimalRecordEntity(
                        id = existing?.id ?: 0L,
                        name = name.trim(), species = species, tagNumber = tag.trim(),
                        sex = sex, ageOrDob = age.trim(), purpose = purpose.trim(), notes = notes.trim(),
                        createdAt = existing?.createdAt ?: System.currentTimeMillis()
                    )
                    val saved: () -> Unit = {
                        name = ""; tag = ""; sex = ""; age = ""; purpose = ""; notes = ""
                        editingRecord = null
                        message = if (isHindi) "पशु का रिकॉर्ड सुरक्षित है।" else "Animal record saved."
                    }
                    val failed: (String) -> Unit = {
                        message = if (isHindi) "रिकॉर्ड सुरक्षित नहीं हो सका। दोबारा प्रयास करें।" else "Could not save the record. Please try again."
                    }
                    if (existing == null) viewModel.add(draft, onSaved = saved, onError = failed)
                    else viewModel.update(draft, onSaved = saved, onError = failed)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Icon(Icons.Default.Pets, contentDescription = null); Spacer(Modifier.padding(3.dp)); Text(if (editingRecord != null) { if (isHindi) "बदलाव सुरक्षित करें" else "Save changes" } else { if (isHindi) "पशु सुरक्षित करें" else "Save animal" }) }
        if (editingRecord != null) {
            TextButton(onClick = {
                editingRecord = null
                name = ""; tag = ""; sex = ""; age = ""; purpose = ""; notes = ""
                message = ""
            }) { Text(if (isHindi) "संपादन रद्द करें" else "Cancel editing") }
        }
        if (message.isNotBlank()) Text(message, color = if (message.contains("saved", true) || message.contains("सुरक्षित")) RegistryGreen else MaterialTheme.colorScheme.error, fontSize = 12.sp)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(if (isHindi) "मेरे पशुओं की सूची" else "Animal list", fontWeight = FontWeight.Bold, color = RegistryInk)
            Text(if (isHindi) "कुल: ${records.size}" else "Total: ${records.size}", color = RegistryGreen, fontSize = 13.sp)
        }
        if (records.isEmpty()) {
            Text(if (isHindi) "अभी कोई पशु दर्ज नहीं है। ऊपर से पहला रिकॉर्ड जोड़ें।" else "No animals recorded yet. Add your first animal above.", color = Color(0xFF66766B), fontSize = 13.sp)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                records.forEach { record ->
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)) {
                        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text(record.name, fontWeight = FontWeight.Bold, color = RegistryInk, fontSize = 16.sp)
                                    Text(speciesLabel(record.species) + if (record.tagNumber.isNotBlank()) " • ${record.tagNumber}" else "", color = RegistryGreen, fontSize = 12.sp)
                                    val details = listOf(record.sex.takeIf { it.isNotBlank() }?.let { if (isHindi) if (it == "Female") "मादा" else if (it == "Male") "नर" else "अज्ञात" else it }, record.ageOrDob, record.purpose).filterNotNull().filter { it.isNotBlank() }
                                    if (details.isNotEmpty()) Text(details.joinToString(" • "), fontSize = 12.sp, color = Color(0xFF66766B))
                                    if (record.notes.isNotBlank()) Text(record.notes, fontSize = 12.sp, color = RegistryInk)
                                }
                                IconButton(onClick = {
                                    editingRecord = record
                                    name = record.name; species = record.species; tag = record.tagNumber
                                    sex = record.sex; age = record.ageOrDob; purpose = record.purpose; notes = record.notes
                                    message = if (isHindi) "रिकॉर्ड संपादित किया जा रहा है।" else "Editing this record."
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = if (isHindi) "बदलें" else "Edit", tint = RegistryGreen)
                                }
                                IconButton(onClick = { pendingDelete = record }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = if (isHindi) "हटाएँ" else "Delete", tint = Color(0xFF9B3D32))
                                }
                            }
                            val eventsForAnimal = healthRecords.filter { it.animalId == record.id }
                            if (eventsForAnimal.isNotEmpty()) {
                                Text(if (isHindi) "स्वास्थ्य रिकॉर्ड" else "Health records", fontWeight = FontWeight.SemiBold, color = RegistryGreen, fontSize = 12.sp)
                                eventsForAnimal.forEach { event ->
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text("${event.eventTitle} • ${event.eventDate}", fontSize = 12.sp, color = RegistryInk)
                                            if (event.nextReminderDate.isNotBlank()) Text((if (isHindi) "अगली याद दिलाने की तारीख: " else "Next reminder date: ") + event.nextReminderDate, fontSize = 11.sp, color = RegistryGreen)
                                            Text(if (event.veterinarianConfirmed) { if (isHindi) "पशु चिकित्सक से पुष्टि" else "Veterinarian confirmed" } else { if (isHindi) "स्वयं दर्ज • पुष्टि नहीं" else "Self-entered • unconfirmed" }, fontSize = 10.sp, color = Color(0xFF66766B))
                                            if (event.notes.isNotBlank()) Text(event.notes, fontSize = 11.sp, color = Color(0xFF66766B))
                                        }
                                        IconButton(onClick = { viewModel.deleteHealthRecord(event) }) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = if (isHindi) "स्वास्थ्य रिकॉर्ड हटाएँ" else "Delete health record", tint = Color(0xFF9B3D32))
                                        }
                                    }
                                }
                            }
                            TextButton(onClick = {
                                healthAnimal = record
                                healthTitle = ""; healthDate = ""; nextReminderDate = ""
                                vetConfirmed = false; healthNotes = ""; healthError = ""
                            }) { Text(if (isHindi) "स्वास्थ्य रिकॉर्ड / अगली तारीख जोड़ें" else "Add health record / next date") }
                        }
                    }
                }
            }
        }
    }
    healthAnimal?.let { animal ->
        AlertDialog(
            onDismissRequest = { healthAnimal = null },
            title = { Text(if (isHindi) "${animal.name} – स्वास्थ्य रिकॉर्ड" else "${animal.name} – Health record") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = healthTitle, onValueChange = { healthTitle = it; healthError = "" },
                        label = { Text(if (isHindi) "घटना / देखभाल का नाम *" else "Event / care title *") }, singleLine = true)
                    OutlinedTextField(value = healthDate, onValueChange = { healthDate = it; healthError = "" },
                        label = { Text(if (isHindi) "तारीख (YYYY-MM-DD) *" else "Date (YYYY-MM-DD) *") }, singleLine = true)
                    OutlinedTextField(value = nextReminderDate, onValueChange = { nextReminderDate = it; healthError = "" },
                        label = { Text(if (isHindi) "अगली तारीख (YYYY-MM-DD)" else "Next date (YYYY-MM-DD)") }, singleLine = true)
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Checkbox(checked = vetConfirmed, onCheckedChange = { vetConfirmed = it; healthError = "" })
                        Text(if (isHindi) "पशु चिकित्सक से तारीख/योजना की पुष्टि हुई है" else "Date/plan confirmed by a veterinarian", fontSize = 12.sp)
                    }
                    OutlinedTextField(value = healthNotes, onValueChange = { healthNotes = it },
                        label = { Text(if (isHindi) "नोट (वैकल्पिक)" else "Notes (optional)") }, minLines = 2)
                    Text(if (isHindi) "अगली तारीख केवल पशु चिकित्सक से पुष्टि के बाद भरें। सूचना स्थानीय समयानुसार सुबह 9 बजे के बाद भेजने का प्रयास होगा; Android बैटरी प्रबंधन के कारण देर हो सकती है और सूचना अनुमति जरूरी है।" else
                        "Enter a next date only after veterinary confirmation. A notification is scheduled for around 9:00 AM local time; Android battery management may delay it, and notification permission is required.", fontSize = 11.sp, color = Color(0xFF66766B))
                    if (healthError.isNotBlank()) Text(healthError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val parsedEventDate = runCatching { LocalDate.parse(healthDate) }.getOrNull()
                    val parsedReminderDate = if (nextReminderDate.isBlank()) null else runCatching { LocalDate.parse(nextReminderDate) }.getOrNull()
                    when {
                        healthTitle.isBlank() -> healthError = if (isHindi) "घटना का नाम भरें।" else "Enter an event title."
                        parsedEventDate == null -> healthError = if (isHindi) "सही तारीख YYYY-MM-DD में भरें।" else "Enter a valid date as YYYY-MM-DD."
                        nextReminderDate.isNotBlank() && parsedReminderDate == null -> healthError = if (isHindi) "अगली तारीख सही YYYY-MM-DD में भरें।" else "Enter a valid next date as YYYY-MM-DD."
                        parsedReminderDate != null && parsedReminderDate.isBefore(parsedEventDate) -> healthError = if (isHindi) "अगली तारीख घटना की तारीख से पहले नहीं हो सकती।" else "Next date cannot be before the event date."
                        parsedReminderDate != null && parsedReminderDate.isBefore(LocalDate.now()) -> healthError = if (isHindi) "अगली तारीख आज या भविष्य की होनी चाहिए।" else "Next date must be today or in the future."
                        nextReminderDate.isNotBlank() && !vetConfirmed -> healthError = if (isHindi) "अगली तारीख के लिए पशु चिकित्सक की पुष्टि जरूरी है।" else "Veterinarian confirmation is required for a next date."
                        else -> viewModel.addHealthRecord(
                            AnimalHealthRecordEntity(
                                animalId = animal.id, eventTitle = healthTitle.trim(), eventDate = healthDate,
                                nextReminderDate = nextReminderDate, veterinarianConfirmed = vetConfirmed, notes = healthNotes.trim()
                            ),
                            onSaved = { savedId ->
                                if (nextReminderDate.isNotBlank()) {
                                    val permissionGranted = Build.VERSION.SDK_INT < 33 ||
                                        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                                    if (permissionGranted) {
                                        AnimalReminderScheduler.schedule(context, savedId, nextReminderDate)
                                        message = if (isHindi) "रिकॉर्ड सुरक्षित और सूचना सेट हो गई।" else "Record saved and reminder scheduled."
                                    } else {
                                        pendingReminderId = savedId
                                        pendingReminderDate = nextReminderDate
                                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                } else {
                                    message = if (isHindi) "स्वास्थ्य रिकॉर्ड सुरक्षित है।" else "Health record saved."
                                }
                                healthAnimal = null
                            },
                            onError = { healthError = if (isHindi) "रिकॉर्ड सुरक्षित नहीं हो सका।" else "Could not save the record." }
                        )
                    }
                }) { Text(if (isHindi) "सुरक्षित करें" else "Save") }
            },
            dismissButton = { TextButton(onClick = { healthAnimal = null }) { Text(if (isHindi) "रद्द करें" else "Cancel") } }
        )
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
