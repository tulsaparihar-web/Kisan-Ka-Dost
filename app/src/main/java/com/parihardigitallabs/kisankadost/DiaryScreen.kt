package com.parihardigitallabs.kisankadost

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.parihardigitallabs.kisankadost.data.DiaryRecordEntity
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

private val DiaryGreen=Color(0xFF247A45)
private val DiaryInk=Color(0xFF203128)
private val diaryTypes=listOf("FIELD","GARDEN","ANIMAL","EXPENSE","LABOUR","INPUT","PRODUCTION","SALES")
private fun diaryLabel(t:String,hi:Boolean)=when(t){
 "FIELD"->if(hi)"खेती" else "Field";"GARDEN"->if(hi)"बागवानी" else "Garden"
 "ANIMAL"->if(hi)"पशुपालन" else "Animal";"EXPENSE"->if(hi)"खर्च" else "Expense"
 "LABOUR"->if(hi)"मजदूरी" else "Labour";"INPUT"->if(hi)"कृषि सामग्री" else "Input"
 "PRODUCTION"->if(hi)"उत्पादन" else "Production";"SALES"->if(hi)"बिक्री" else "Sales";else->t
}
private fun photoNames(value:String)=value.lineSequence().filter{it.isNotBlank()}.toList()

@Composable
private fun DiaryPhotoThumbnail(context: Context, name: String, isHindi: Boolean) {
 val thumbnail by produceState<ImageBitmap?>(initialValue=null, key1=name) {
  value=withContext(Dispatchers.IO) {
   DiaryPhotoStore.file(context,name)?.let { file ->
    BitmapFactory.decodeFile(file.absolutePath,BitmapFactory.Options().apply{inSampleSize=4})?.asImageBitmap()
   }
  }
 }
 if(thumbnail!=null) Image(thumbnail!!,contentDescription=if(isHindi)"डायरी फोटो" else "Diary photo",
  modifier=Modifier.size(52.dp),contentScale=ContentScale.Crop)
}

@Composable
fun DiaryScreen(isHindi:Boolean,viewModel:DiaryViewModel){
 val records by viewModel.records.collectAsState()
 val context=LocalContext.current
 val scope=rememberCoroutineScope()
 var photoImporting by remember { mutableStateOf(false) }
 var filter by rememberSaveable{mutableStateOf("ALL")}
 var query by rememberSaveable{mutableStateOf("")}
 var showForm by rememberSaveable{mutableStateOf(false)}
 var editing by remember{mutableStateOf<DiaryRecordEntity?>(null)}
 var deleting by remember{mutableStateOf<DiaryRecordEntity?>(null)}
 var pendingBackup by remember{mutableStateOf<Uri?>(null)}
 var type by rememberSaveable{mutableStateOf("FIELD")}
 var title by rememberSaveable{mutableStateOf("")}
 var date by rememberSaveable{mutableStateOf(LocalDate.now().toString())}
 var subject by rememberSaveable{mutableStateOf("")}
 var quantity by rememberSaveable{mutableStateOf("")}
 var unit by rememberSaveable{mutableStateOf("")}
 var amount by rememberSaveable{mutableStateOf("")}
 var notes by rememberSaveable{mutableStateOf("")}
 var reminder by rememberSaveable{mutableStateOf("")}
 var photoPathsRaw by rememberSaveable{mutableStateOf("")}
 var newPhotoPathsRaw by rememberSaveable{mutableStateOf("")}
 var removedPhotoPathsRaw by rememberSaveable{mutableStateOf("")}
 var error by remember{mutableStateOf("")}
 var message by remember{mutableStateOf("")}
 var pendingId by remember{mutableStateOf<Long?>(null)}
 var pendingDate by remember{mutableStateOf("")}

 val permissionLauncher=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){granted->
  val id=pendingId
  if(granted&&id!=null&&pendingDate.isNotBlank())viewModel.schedule(id,pendingDate)
  message=if(granted){if(isHindi)"रिमाइंडर सेट हो गया।" else "Reminder scheduled."}else{if(isHindi)"रिकॉर्ड सुरक्षित है, सूचना अनुमति बंद है।" else "Record saved; notifications are disabled."}
  pendingId=null;pendingDate=""
 }
 val photoPicker=rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()){uris->
  val room=(5-photoNames(photoPathsRaw).size).coerceAtLeast(0)
  scope.launch {
   photoImporting=true
   var added=0
   var failed=0
   try {
    uris.take(room).forEach{uri->
     val name=withContext(Dispatchers.IO){DiaryPhotoStore.copyFromUri(context,uri)}
     if(name!=null){
      photoPathsRaw=(photoNames(photoPathsRaw)+name).joinToString("\n")
      newPhotoPathsRaw=(photoNames(newPhotoPathsRaw)+name).joinToString("\n")
      added++
     }else failed++
    }
    val skipped=(uris.size-room).coerceAtLeast(0)
    message=if(isHindi)"$added फोटो जोड़े गए; $failed फोटो पढ़े नहीं जा सके; अधिकतम 5 फोटो।" else "$added photo(s) attached; $failed could not be read; maximum 5 photos."
    if(skipped>0) message += if(isHindi)" $skipped अतिरिक्त फोटो सीमा के कारण छोड़े गए।" else " $skipped extra photo(s) skipped due to the limit."
   } finally { photoImporting=false }
  }
 }
 val csvLauncher=rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/csv")){uri->
  if(uri!=null)runCatching{context.contentResolver.openOutputStream(uri)?.bufferedWriter(Charsets.UTF_8)?.use{it.write(DiaryCsvExporter.export(records))}?:error("No output stream")}
   .onSuccess{message=if(isHindi)"CSV फाइल सुरक्षित हो गई।" else "CSV exported."}
   .onFailure{message=if(isHindi)"CSV निर्यात विफल रहा।" else "CSV export failed."}
 }
 val backupLauncher=rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/zip")){uri->
  if(uri!=null)viewModel.exportBackup(uri,{message=if(isHindi)"पूरा बैकअप सुरक्षित हो गया।" else "Full backup created."},{reason->message=(if(isHindi)"बैकअप विफल: " else "Backup failed: ")+reason})
 }
 val restoreLauncher=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()){uri->pendingBackup=uri}
 LaunchedEffect(Unit){
  val allowed=Build.VERSION.SDK_INT<33||ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED
  if(allowed)viewModel.reschedule()
 }
 fun clearForm(discardNew:Boolean=true){
  if(discardNew)photoNames(newPhotoPathsRaw).forEach{DiaryPhotoStore.delete(context,it)}
  editing=null;type="FIELD";title="";date=LocalDate.now().toString();subject="";quantity="";unit="";amount="";notes="";reminder=""
  photoPathsRaw="";newPhotoPathsRaw="";removedPhotoPathsRaw="";error="";showForm=false
 }
 fun save(){
  val parsedDate=runCatching{LocalDate.parse(date)}.getOrNull()
  val parsedReminder=if(reminder.isBlank())null else runCatching{LocalDate.parse(reminder)}.getOrNull()
  val q=quantity.toDoubleOrNull();val money=amount.toDoubleOrNull()
  when{
   title.isBlank()->error=if(isHindi)"गतिविधि का नाम भरें।" else "Enter an activity title."
   parsedDate==null->error=if(isHindi)"तारीख YYYY-MM-DD में सही भरें।" else "Enter a valid date (YYYY-MM-DD)."
   reminder.isNotBlank()&&parsedReminder==null->error=if(isHindi)"रिमाइंडर की तारीख सही भरें।" else "Enter a valid reminder date."
   parsedReminder!=null&&parsedReminder.isBefore(LocalDate.now())->error=if(isHindi)"रिमाइंडर आज या भविष्य में होना चाहिए।" else "Reminder must be today or later."
   quantity.isNotBlank()&&(q==null||!q.isFinite()||q<0)->error=if(isHindi)"मात्रा सही संख्या होनी चाहिए।" else "Enter a valid quantity."
   amount.isNotBlank()&&(money==null||!money.isFinite()||money<0)->error=if(isHindi)"राशि सही संख्या होनी चाहिए।" else "Enter a valid amount."
   title.length>160||subject.length>120||unit.length>40||notes.length>3000->error=if(isHindi)"कुछ विवरण बहुत लंबे हैं।" else "Some fields are too long."
   else->{
    val old=editing
    val item=DiaryRecordEntity(id=old?.id?:0,recordType=type,title=title.trim(),recordDate=date,subject=subject.trim(),
     quantity=q?:0.0,unit=unit.trim(),amount=money?:0.0,notes=notes.trim(),reminderDate=reminder,photoPaths=photoNames(photoPathsRaw).joinToString("\n"))
    val saved:(Long)->Unit={id->
     photoNames(removedPhotoPathsRaw).forEach{DiaryPhotoStore.delete(context,it)}
     if(reminder.isNotBlank()){
      val allowed=Build.VERSION.SDK_INT<33||ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED
      if(allowed){viewModel.schedule(id,reminder);message=if(isHindi)"रिकॉर्ड और रिमाइंडर सुरक्षित हैं।" else "Record and reminder saved."}
      else{pendingId=id;pendingDate=reminder;permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)}
     }else message=if(isHindi)"डायरी रिकॉर्ड सुरक्षित है।" else "Diary record saved."
     newPhotoPathsRaw="";clearForm(false)
    }
    if(old==null)viewModel.add(item,saved,{
     photoNames(newPhotoPathsRaw).forEach{DiaryPhotoStore.delete(context,it)}
     newPhotoPathsRaw=""
     error=if(isHindi)"रिकॉर्ड सुरक्षित नहीं हो सका।" else "Could not save record."
    })
    else viewModel.update(item,{saved(old.id)},{
     photoNames(newPhotoPathsRaw).forEach{DiaryPhotoStore.delete(context,it)}
     newPhotoPathsRaw=""
     error=if(isHindi)"रिकॉर्ड अपडेट नहीं हो सका।" else "Could not update record."
    })
   }
  }
 }
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  Row(verticalAlignment=Alignment.CenterVertically){
   Column(Modifier.weight(1f)){Text(if(isHindi)"खेत डायरी" else "Farm Diary",fontSize=23.sp,fontWeight=FontWeight.Bold,color=DiaryInk);Text(if(isHindi)"खेत, बगीचे और पशुओं के रिकॉर्ड — ऑफलाइन" else "Field, garden and livestock records — offline",fontSize=12.sp,color=Color(0xFF66766B))}
   IconButton(onClick={csvLauncher.launch("Kisan-Ka-Dost-Diary.csv")},enabled=records.isNotEmpty()){Icon(Icons.Default.FileDownload,contentDescription="CSV",tint=DiaryGreen)}
   IconButton(onClick={backupLauncher.launch("Kisan-Ka-Dost-Full-Backup.zip")}){Icon(Icons.Default.Backup,contentDescription=if(isHindi)"बैकअप" else "Backup",tint=DiaryGreen)}
   IconButton(onClick={restoreLauncher.launch(arrayOf("application/zip","application/octet-stream"))}){Icon(Icons.Default.Restore,contentDescription=if(isHindi)"बहाली" else "Restore",tint=DiaryGreen)}
  }
  Card(colors=CardDefaults.cardColors(containerColor=Color(0xFFEFF6F0)),shape=RoundedCornerShape(12.dp)){
   Text(if(isHindi)"बैकअप में किसान प्रोफाइल, पशु, स्वास्थ्य रिकॉर्ड, डायरी और फोटो शामिल हैं। ZIP पासवर्ड से सुरक्षित नहीं है; सुरक्षित स्थान पर रखें। बहाली मौजूदा स्थानीय डेटा को बदल देगी।" else "Backup includes farmer profile, animals, health records, diary entries and photos. The ZIP is not password-protected; store it securely. Restoring replaces current local data.",modifier=Modifier.padding(10.dp),fontSize=11.sp,color=DiaryInk)
  }
  OutlinedTextField(query,{query=it},Modifier.fillMaxWidth(),singleLine=true,label={Text(if(isHindi)"डायरी में खोजें" else "Search diary entries")})
  Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(5.dp)){
   (listOf("ALL")+diaryTypes).forEach{t->FilterChip(selected=filter==t,onClick={filter=t},label={Text(if(t=="ALL"){if(isHindi)"सभी" else "All"}else diaryLabel(t,isHindi))})}
  }
  Button(onClick={if(showForm)clearForm() else{editing=null;showForm=true}},modifier=Modifier.fillMaxWidth(),enabled=!photoImporting){Text(if(showForm){if(isHindi)"फॉर्म बंद करें" else "Close form"}else{if(isHindi)"＋ नया रिकॉर्ड जोड़ें" else "＋ Add a record"})}
  if(showForm)Card(colors=CardDefaults.cardColors(containerColor=Color.White),shape=RoundedCornerShape(16.dp)){
   Column(Modifier.fillMaxWidth().padding(12.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){
    Text(if(isHindi)"रिकॉर्ड का प्रकार" else "Record type",fontWeight=FontWeight.SemiBold,color=DiaryGreen)
    Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(5.dp)){diaryTypes.forEach{t->FilterChip(selected=type==t,onClick={type=t},label={Text(diaryLabel(t,isHindi))})}}
    OutlinedTextField(title,{title=it;error=""},Modifier.fillMaxWidth(),label={Text(if(isHindi)"गतिविधि / विवरण *" else "Activity / description *")},singleLine=true)
    OutlinedTextField(date,{date=it;error=""},Modifier.fillMaxWidth(),label={Text(if(isHindi)"तारीख (YYYY-MM-DD)" else "Date (YYYY-MM-DD)")},singleLine=true)
    OutlinedTextField(subject,{subject=it},Modifier.fillMaxWidth(),label={Text(if(isHindi)"फसल / पौधा / पशु / संबंधित नाम" else "Crop / plant / animal / related item")},singleLine=true)
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
     OutlinedTextField(quantity,{quantity=it;error=""},Modifier.weight(1f),label={Text(if(isHindi)"मात्रा" else "Quantity")},singleLine=true)
     OutlinedTextField(unit,{unit=it},Modifier.weight(1f),label={Text(if(isHindi)"इकाई" else "Unit")},singleLine=true)
    }
    OutlinedTextField(amount,{amount=it;error=""},Modifier.fillMaxWidth(),label={Text(when(type){"SALES"->if(isHindi)"प्राप्त राशि / बिक्री (₹)" else "Sale amount / income (₹)";"EXPENSE"->if(isHindi)"खर्च की राशि (₹)" else "Expense amount (₹)";else->if(isHindi)"राशि (₹)" else "Amount (₹)"})},singleLine=true)
    OutlinedTextField(notes,{notes=it},Modifier.fillMaxWidth(),label={Text(if(isHindi)"नोट" else "Notes")},minLines=2)
    OutlinedTextField(reminder,{reminder=it;error=""},Modifier.fillMaxWidth(),label={Text(if(isHindi)"रिमाइंडर तारीख (YYYY-MM-DD, वैकल्पिक)" else "Reminder date (YYYY-MM-DD, optional)")},singleLine=true)
    Row(verticalAlignment=Alignment.CenterVertically){
     Text(if(isHindi)"फोटो (${photoNames(photoPathsRaw).size}/5)" else "Photos (${photoNames(photoPathsRaw).size}/5)",modifier=Modifier.weight(1f),fontWeight=FontWeight.SemiBold,color=DiaryGreen)
     OutlinedButton(onClick={photoPicker.launch("image/*")},enabled=photoNames(photoPathsRaw).size<5 && !photoImporting){Icon(Icons.Default.AddAPhoto,contentDescription=null);Spacer(Modifier.width(5.dp));Text(if(isHindi)"फोटो जोड़ें" else "Attach photos")}
    }
    if(photoNames(photoPathsRaw).isNotEmpty())Column(verticalArrangement=Arrangement.spacedBy(3.dp)){
     photoNames(photoPathsRaw).forEach{name->Row(verticalAlignment=Alignment.CenterVertically){
      DiaryPhotoThumbnail(context,name,isHindi)
      Text(name,modifier=Modifier.weight(1f),fontSize=11.sp,color=DiaryInk)
      TextButton(onClick={
       photoPathsRaw=photoNames(photoPathsRaw).filterNot{it==name}.joinToString("\n")
       if(name in photoNames(newPhotoPathsRaw)){DiaryPhotoStore.delete(context,name);newPhotoPathsRaw=photoNames(newPhotoPathsRaw).filterNot{it==name}.joinToString("\n")}
       else removedPhotoPathsRaw=(photoNames(removedPhotoPathsRaw)+name).distinct().joinToString("\n")
      }){Text(if(isHindi)"हटाएँ" else "Remove")}
     }}
    }
    Text(if(photoImporting){if(isHindi)"फोटो सुरक्षित की जा रही हैं…" else "Importing photos…"}else{if(isHindi)"फोटो केवल फोन में रहती हैं और आपके चुने हुए बैकअप में ही शामिल होंगी। रिमाइंडर लगभग सुबह 9 बजे; Android देरी कर सकता है।" else "Photos stay on this phone and are included only in a backup you choose. Reminders are attempted around 9 AM; Android may delay delivery."},fontSize=11.sp,color=Color(0xFF66766B))
    if(error.isNotBlank())Text(error,color=MaterialTheme.colorScheme.error,fontSize=12.sp)
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
     Button(onClick={save()},modifier=Modifier.weight(1f),enabled=!photoImporting){Text(if(editing==null){if(isHindi)"सुरक्षित करें" else "Save"}else{if(isHindi)"अपडेट करें" else "Update"})}
     TextButton(onClick={clearForm()},enabled=!photoImporting){Text(if(isHindi)"रद्द" else "Cancel")}
    }
   }
  }
  val visible=records.filter{(filter=="ALL"||it.recordType==filter) && (query.isBlank() || listOf(it.title,it.subject,it.notes,it.recordDate).any{value->value.contains(query.trim(),ignoreCase=true)})}
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(if(isHindi)"डायरी रिकॉर्ड" else "Diary entries",fontWeight=FontWeight.Bold,color=DiaryInk);Text(if(isHindi)"कुल ${visible.size}" else "${visible.size} total",color=DiaryGreen,fontSize=12.sp)}
  if(visible.isEmpty())Text(if(isHindi)"अभी कोई रिकॉर्ड नहीं है। पहला रिकॉर्ड जोड़ें।" else "No entries yet. Add your first record.",color=Color(0xFF66766B),fontSize=13.sp)
  visible.forEach{r->Card(colors=CardDefaults.cardColors(containerColor=Color.White),shape=RoundedCornerShape(14.dp)){
   Column(Modifier.fillMaxWidth().padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
    Row(verticalAlignment=Alignment.CenterVertically){
     Column(Modifier.weight(1f)){Text(r.title,fontWeight=FontWeight.Bold,color=DiaryInk);Text("${diaryLabel(r.recordType,isHindi)} • ${r.recordDate}",fontSize=11.sp,color=DiaryGreen)}
     IconButton(onClick={editing=r;type=r.recordType;title=r.title;date=r.recordDate;subject=r.subject;quantity=if(r.quantity==0.0)"" else r.quantity.toString();unit=r.unit;amount=if(r.amount==0.0)"" else r.amount.toString();notes=r.notes;reminder=r.reminderDate;photoPathsRaw=r.photoPaths;newPhotoPathsRaw="";removedPhotoPathsRaw="";showForm=true;error=""}){Icon(Icons.Default.Edit,contentDescription=if(isHindi)"बदलें" else "Edit",tint=DiaryGreen)}
     IconButton(onClick={deleting=r}){Icon(Icons.Default.DeleteOutline,contentDescription=if(isHindi)"हटाएँ" else "Delete",tint=Color(0xFF9B3D32))}
    }
    if(r.subject.isNotBlank())Text((if(isHindi)"संबंधित: " else "Related: ")+r.subject,fontSize=12.sp,color=DiaryInk)
    val details=listOf(if(r.quantity!=0.0)"${r.quantity} ${r.unit}".trim() else "",if(r.amount!=0.0)"₹${r.amount}" else "").filter{it.isNotBlank()}.joinToString(" • ")
    if(details.isNotBlank())Text(details,fontSize=12.sp,color=DiaryInk)
    if(r.notes.isNotBlank())Text(r.notes,fontSize=12.sp,color=Color(0xFF66766B))
    if(r.reminderDate.isNotBlank())Text((if(isHindi)"रिमाइंडर: " else "Reminder: ")+r.reminderDate,fontSize=11.sp,color=DiaryGreen)
    if(photoNames(r.photoPaths).isNotEmpty())Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){photoNames(r.photoPaths).forEach{name->DiaryPhotoThumbnail(context,name,isHindi)}}
   }
  }}
  if(message.isNotBlank())Text(message,color=DiaryGreen,fontSize=12.sp)
 }
 deleting?.let{r->AlertDialog(onDismissRequest={deleting=null},title={Text(if(isHindi)"रिकॉर्ड हटाएँ?" else "Delete entry?")},text={Text(if(isHindi)"यह रिकॉर्ड और इसकी फोटो इस फोन से हट जाएँगी।" else "This entry and its photos will be removed from this phone.")},confirmButton={TextButton(onClick={viewModel.delete(r);deleting=null}){Text(if(isHindi)"हटाएँ" else "Delete",color=MaterialTheme.colorScheme.error)}},dismissButton={TextButton(onClick={deleting=null}){Text(if(isHindi)"रद्द" else "Cancel")}})}
 pendingBackup?.let{uri->AlertDialog(onDismissRequest={pendingBackup=null},title={Text(if(isHindi)"बैकअप बहाल करें?" else "Restore backup?")},text={Text(if(isHindi)"यह मौजूदा किसान प्रोफाइल, पशु, स्वास्थ्य रिकॉर्ड और डायरी को चुने गए बैकअप से बदल देगा। पहले वर्तमान डेटा का बैकअप बना लें।" else "This replaces the current farmer profile, animals, health records and diary with the selected backup. Back up current data first if needed.")},confirmButton={TextButton(onClick={viewModel.restoreBackup(uri,{summary->message=if(isHindi)"बहाली पूरी: ${summary.diaryEntries} डायरी, ${summary.animals} पशु, ${summary.photos} फोटो।" else "Restore complete: ${summary.diaryEntries} entries, ${summary.animals} animals, ${summary.photos} photos."},{reason->message=(if(isHindi)"बहाली विफल: " else "Restore failed: ")+reason});pendingBackup=null}){Text(if(isHindi)"बहाल करें" else "Restore")}},dismissButton={TextButton(onClick={pendingBackup=null}){Text(if(isHindi)"रद्द करें" else "Cancel")}})}
}
