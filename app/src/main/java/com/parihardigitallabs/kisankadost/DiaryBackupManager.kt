package com.parihardigitallabs.kisankadost

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.room.withTransaction
import com.parihardigitallabs.kisankadost.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.UUID
import java.time.LocalDate
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object DiaryBackupManager {
    private const val BACKUP_VERSION = 3
    private const val MAX_ARCHIVE_BYTES = 100L * 1024L * 1024L
    private const val MAX_JSON_BYTES = 8L * 1024L * 1024L
    private const val MAX_PHOTO_BYTES = 12L * 1024L * 1024L
    private const val MAX_ENTRIES = 600

    suspend fun exportBackup(context: Context, uri: Uri, db: AppDatabase) = withContext(Dispatchers.IO) {
        val snapshot = db.withTransaction {
            BackupSnapshot(
                farmers = db.farmerProfileDao().getAllForBackup(),
                animals = db.animalRecordDao().getAllForBackup(),
                health = db.animalHealthRecordDao().getAllForBackup(),
                diaries = db.diaryRecordDao().getAllForBackup(),
                plantDiagnoses = db.plantDiagnosisDao().getAllForBackup()
            )
        }
        validateBackupRecords(snapshot.farmers, snapshot.animals, snapshot.health, snapshot.diaries)
        val photoNames = validateBackupPhotoReferences(
            snapshot.diaries,
            snapshot.diaries.flatMap { it.photoPaths.toPhotoNames() }.toSet()
        )
        val diagnosisPhotoNames = validatePlantDiagnosisRecords(snapshot.plantDiagnoses)
        val json = encode(snapshot).toByteArray(Charsets.UTF_8)
        require(json.size <= MAX_JSON_BYTES) { "Backup data is too large" }
        require(photoNames.size + diagnosisPhotoNames.size + 1 <= MAX_ENTRIES) { "Backup contains too many photo files" }
        val photos = photoNames.map { name ->
            val photo = DiaryPhotoStore.file(context, name) ?: throw IOException("An attached diary photo is missing")
            require(photo.length() in 1..MAX_PHOTO_BYTES) { "An attached diary photo is too large or empty" }
            name to photo
        }
        val diagnosisPhotos = snapshot.plantDiagnoses.map { record ->
            val file = File(record.imagePath).canonicalFile
            val expectedDirectory = File(context.filesDir, "plant_diagnoses").canonicalFile
            require(file.parentFile == expectedDirectory && file.isFile) { "A saved Plant Doctor photo is missing or outside app storage" }
            require(file.length() in 1..MAX_PHOTO_BYTES && DiaryPhotoStore.isValidStoredPhoto(file)) { "A Plant Doctor photo is invalid or too large" }
            file.name to file
        }
        val uncompressedBytes = json.size.toLong() + photos.sumOf { it.second.length() } + diagnosisPhotos.sumOf { it.second.length() }
        require(uncompressedBytes <= MAX_ARCHIVE_BYTES) { "Backup is larger than the supported 100 MB limit" }
        val output = context.contentResolver.openOutputStream(uri) ?: throw IOException("Cannot open backup destination")
        ZipOutputStream(BufferedOutputStream(output)).use { zip ->
            zip.putNextEntry(ZipEntry("backup.json"))
            zip.write(json)
            zip.closeEntry()
            photos.forEach { (name, photo) ->
                zip.putNextEntry(ZipEntry("photos/$name"))
                photo.inputStream().buffered().use { it.copyTo(zip) }
                zip.closeEntry()
            }
            diagnosisPhotos.forEach { (name, photo) ->
                zip.putNextEntry(ZipEntry("plant_photos/$name"))
                photo.inputStream().buffered().use { it.copyTo(zip) }
                zip.closeEntry()
            }
        }
        true
    }

    suspend fun restoreBackup(context: Context, uri: Uri, db: AppDatabase) = withContext(Dispatchers.IO) {
        val staging = File(context.cacheDir, "diary_restore_${UUID.randomUUID()}").apply { mkdirs() }
        val copiedPhotos = mutableListOf<File>()
        var restoreCommitted = false
        try {
            var totalBytes = 0L
            var entries = 0
            var jsonBytes: ByteArray? = null
            var backupJsonSeen = false
            val stagedPhotos = linkedMapOf<String, File>()
            val stagedPlantPhotos = linkedMapOf<String, File>()
            val input = context.contentResolver.openInputStream(uri) ?: throw IOException("Cannot open selected backup")
            ZipInputStream(BufferedInputStream(input)).use { zip ->
                while (true) {
                    val entry = zip.nextEntry ?: break
                    entries++
                    require(entries <= MAX_ENTRIES) { "Backup contains too many files" }
                    require(!entry.isDirectory && !entry.name.startsWith("/") && !entry.name.contains("..")) { "Invalid backup entry" }
                    when {
                        entry.name == "backup.json" -> {
                            require(!backupJsonSeen) { "Backup contains duplicate metadata files" }
                            backupJsonSeen = true
                            val bytes = readLimited(zip, MAX_JSON_BYTES)
                            totalBytes += bytes.size
                            jsonBytes = bytes
                        }
                        entry.name.startsWith("plant_photos/") -> {
                            require(entry.name.count { it == '/' } == 1) { "Invalid Plant Doctor photo archive path" }
                            val name = entry.name.removePrefix("plant_photos/")
                            require(isSafePlantPhotoName(name)) { "Invalid Plant Doctor photo filename" }
                            require(!stagedPlantPhotos.containsKey(name)) { "Duplicate Plant Doctor photo filename" }
                            val bytes = readLimited(zip, MAX_PHOTO_BYTES)
                            require(bytes.isNotEmpty()) { "Backup contains an empty Plant Doctor photo" }
                            totalBytes += bytes.size
                            val file = File(staging, "plant_" + name)
                            file.writeBytes(bytes)
                            require(DiaryPhotoStore.isValidStoredPhoto(file)) { "Backup contains an invalid Plant Doctor photo" }
                            stagedPlantPhotos[name] = file
                        }
                        entry.name.startsWith("photos/") -> {
                            require(entry.name.count { it == '/' } == 1) { "Invalid photo archive path" }
                            val name = entry.name.removePrefix("photos/")
                            require(DiaryPhotoStore.isSafeName(name)) { "Invalid photo filename" }
                            require(!stagedPhotos.containsKey(name)) { "Duplicate photo filename" }
                            val file = File(staging, name)
                            val bytes = readLimited(zip, MAX_PHOTO_BYTES)
                            require(bytes.isNotEmpty()) { "Backup contains an empty photo" }
                            totalBytes += bytes.size
                            file.writeBytes(bytes)
                            require(DiaryPhotoStore.isValidStoredPhoto(file)) { "Backup contains an invalid or oversized photo" }
                            stagedPhotos[name] = file
                        }
                        else -> throw IOException("Backup contains an unsupported file")
                    }
                    require(totalBytes <= MAX_ARCHIVE_BYTES) { "Backup archive is too large" }
                    zip.closeEntry()
                }
            }
            val root = JSONObject(String(jsonBytes ?: throw IOException("Backup data is missing"), Charsets.UTF_8))
            require(isSupportedDiaryBackupVersion(root.optInt("backupVersion", -1))) { "Unsupported backup version" }
            val farmers = root.getJSONArray("farmers").toFarmers()
            val animals = root.getJSONArray("animals").toAnimals()
            val health = root.getJSONArray("animalHealth").toHealth()
            val diaries = root.getJSONArray("diaries").toDiaries()
            val plantDiagnoses = root.optJSONArray("plantDiagnoses")?.toPlantDiagnoses() ?: emptyList()
            validateBackupRecords(farmers, animals, health, diaries)
            validatePlantDiagnosisRecords(plantDiagnoses)
            val requiredPhotos = validateBackupPhotoReferences(
                diaries,
                stagedPhotos.keys
            )

            val requiredPlantPhotos = validatePlantDiagnosisPhotoReferences(plantDiagnoses, stagedPlantPhotos.keys)
            val plantPhotoMap = mutableMapOf<String, String>()
            val plantPhotoDirectory = File(context.filesDir, "plant_diagnoses").apply { mkdirs() }
            requiredPlantPhotos.forEach { original ->
                val destination = File(plantPhotoDirectory, "restored_" + UUID.randomUUID() + ".jpg")
                stagedPlantPhotos.getValue(original).copyTo(destination, overwrite = false)
                copiedPhotos += destination
                plantPhotoMap[original] = destination.absolutePath
            }
            val restoredDiagnoses = plantDiagnoses.map { record ->
                record.copy(imagePath = plantPhotoMap.getValue(File(record.imagePath).name))
            }
            val photoMap = mutableMapOf<String, String>()
            val photoDirectory = File(context.filesDir, "diary_photos").apply { mkdirs() }
            requiredPhotos.forEach { original ->
                val extension = original.substringAfterLast('.', "jpg").take(8).let { if (it.matches(Regex("[A-Za-z0-9]+"))) it else "jpg" }
                val newName = "restored_${UUID.randomUUID()}.$extension"
                val destination = File(photoDirectory, newName)
                stagedPhotos.getValue(original).copyTo(destination, overwrite = false)
                copiedPhotos += destination
                photoMap[original] = newName
            }
            val restoredDiaries = diaries.map { record ->
                record.copy(photoPaths = record.photoPaths.toPhotoNames().mapNotNull { photoMap[it] }.joinToString("\n"))
            }
            val previousDiaries = db.diaryRecordDao().getAllForBackup()
            val previousDiagnoses = db.plantDiagnosisDao().getAllForBackup()
            val previousHealth = db.animalHealthRecordDao().getAllForBackup()
            previousDiaries.forEach { DiaryReminderScheduler.cancel(context, it.id) }
            previousHealth.forEach { AnimalReminderScheduler.cancel(context, it.id) }
            try {
                db.withTransaction {
                    db.plantDiagnosisDao().clearAll()
                    db.animalHealthRecordDao().clearAll()
                    db.diaryRecordDao().clearAll()
                    db.animalRecordDao().clearAll()
                    db.farmerProfileDao().clearAll()
                    if (farmers.isNotEmpty()) db.farmerProfileDao().insertAll(farmers)
                    if (animals.isNotEmpty()) db.animalRecordDao().insertAll(animals)
                    if (health.isNotEmpty()) db.animalHealthRecordDao().insertAll(health)
                    if (restoredDiaries.isNotEmpty()) db.diaryRecordDao().insertAll(restoredDiaries)
                    if (restoredDiagnoses.isNotEmpty()) db.plantDiagnosisDao().insertAll(restoredDiagnoses)
                }
            } catch (failure: Throwable) {
                copiedPhotos.forEach { it.delete() }
                rescheduleReminders(context, previousDiaries, previousHealth)
                throw failure
            }
            restoreCommitted = true
            photoDirectory.listFiles()?.filter { it !in copiedPhotos }?.forEach { it.delete() }
            val copiedDiagnosisPaths = copiedPhotos.map { it.canonicalFile }.filter { it.parentFile == plantPhotoDirectory.canonicalFile }.toSet()
            plantPhotoDirectory.listFiles()?.filter { it !in copiedDiagnosisPaths }?.forEach { it.delete() }
            rescheduleReminders(context, db)
            RestoreSummary(farmers.size, animals.size, health.size, diaries.size, requiredPhotos.size, restoredDiagnoses.size)
        } finally {
            if (!restoreCommitted) copiedPhotos.forEach { it.delete() }
            staging.deleteRecursively()
        }
    }

    private suspend fun rescheduleReminders(context: Context, db: AppDatabase) {
        rescheduleReminders(context, db.diaryRecordDao().getAllForBackup(), db.animalHealthRecordDao().getAllForBackup())
    }

    private fun rescheduleReminders(
        context: Context,
        diaries: List<DiaryRecordEntity>,
        health: List<AnimalHealthRecordEntity>
    ) {
        val allowed = Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        if (!allowed) return
        diaries.filter { it.reminderDate.isNotBlank() && !it.reminderNotified }.forEach { record ->
            runCatching { DiaryReminderScheduler.schedule(context, record.id, record.reminderDate) }
        }
        health.filter { it.veterinarianConfirmed && it.nextReminderDate.isNotBlank() && !it.reminderNotified }.forEach { record ->
            runCatching { AnimalReminderScheduler.schedule(context, record.id, record.nextReminderDate) }
        }
    }

    internal fun validateBackupRecords(
        farmers: List<FarmerProfileEntity>,
        animals: List<AnimalRecordEntity>,
        health: List<AnimalHealthRecordEntity>,
        diaries: List<DiaryRecordEntity>
    ) {
        require(farmers.size <= 1 && farmers.all { it.id == 1 }) { "Backup must contain at most one farmer profile" }
        require(animals.size <= 5_000 && health.size <= 10_000 && diaries.size <= 10_000) {
            "Backup contains too many records"
        }
        require(animals.all { it.id > 0 && it.name.isNotBlank() && it.species.isNotBlank() && it.name.length <= 120 && it.species.length <= 80 }) { "Backup contains an invalid animal profile" }
        require(animals.map { it.id }.distinct().size == animals.size) { "Backup contains duplicate animal IDs" }
        val animalIds = animals.map { it.id }.toSet()
        require(health.all { it.id > 0 && it.animalId in animalIds && it.eventTitle.isNotBlank() }) { "Backup has an invalid animal health record or a record without a matching animal" }
        require(health.map { it.id }.distinct().size == health.size) { "Backup contains duplicate animal health IDs" }
        health.forEach {
            require(it.eventTitle.length <= 160 && it.notes.length <= 3000) { "Backup contains an oversized animal health record" }
            require(runCatching { LocalDate.parse(it.eventDate) }.isSuccess) { "Backup contains an invalid animal health date" }
            require(it.nextReminderDate.isBlank() || runCatching { LocalDate.parse(it.nextReminderDate) }.isSuccess) { "Backup contains an invalid animal reminder date" }
            require(it.nextReminderDate.isBlank() || it.veterinarianConfirmed) { "Backup contains an animal reminder without veterinary confirmation" }
        }
        require(diaries.all { it.id > 0 && it.title.isNotBlank() && it.recordType in setOf("FIELD", "GARDEN", "ANIMAL", "EXPENSE", "LABOUR", "INPUT", "PRODUCTION", "SALES") }) {
            "Backup contains an invalid diary entry"
        }
        require(diaries.map { it.id }.distinct().size == diaries.size) { "Backup contains duplicate diary IDs" }
        diaries.forEach {
            require(runCatching { LocalDate.parse(it.recordDate) }.isSuccess) { "Backup contains an invalid diary date" }
            require(it.reminderDate.isBlank() || runCatching { LocalDate.parse(it.reminderDate) }.isSuccess) { "Backup contains an invalid diary reminder date" }
            require(it.quantity.isFinite() && it.quantity >= 0.0 && it.amount.isFinite() && it.amount >= 0.0) { "Backup contains an invalid quantity or amount" }
            require(it.title.length <= 160 && it.subject.length <= 120 && it.unit.length <= 40 && it.notes.length <= 3000) {
                "Backup contains a diary field that is too long"
            }
        }
    }

    internal fun validatePlantDiagnosisRecords(records: List<PlantDiagnosisEntity>): Set<String> {
        require(records.size <= 50) { "Backup contains too many Plant Doctor history records" }
        require(records.all { it.id > 0 && it.predictedLabel.isNotBlank() && it.predictedLabel.length <= 160 && it.confidence.isFinite() && it.confidence in 0f..1f && it.createdAt > 0 }) {
            "Backup contains an invalid Plant Doctor diagnosis"
        }
        require(records.map { it.id }.distinct().size == records.size) { "Backup contains duplicate Plant Doctor diagnosis IDs" }
        val names = records.map { record ->
            val path = record.imagePath
            val file = File(path)
            val name = file.name
            val hasTraversal = path.split(File.separatorChar).any { it == ".." || it == "." }
            require(isSafePlantPhotoName(name) && !hasTraversal) { "Backup contains an invalid Plant Doctor photo reference" }
            require(path == name || file.isAbsolute) { "Plant Doctor photo reference must be a filename or app-private absolute path" }
            name
        }
        require(names.distinct().size == names.size) { "Backup contains duplicate Plant Doctor photo references" }
        return names.toSet()
    }

    internal fun validatePlantDiagnosisPhotoReferences(records: List<PlantDiagnosisEntity>, stagedNames: Set<String>): Set<String> {
        val required = validatePlantDiagnosisRecords(records)
        require(required == stagedNames) { "Plant Doctor photos do not match the photos referenced by diagnosis history" }
        return required
    }

    private fun isSafePlantPhotoName(name: String): Boolean =
        name.isNotBlank() && name.length <= 120 && name.matches(Regex("[A-Za-z0-9._-]+")) && !name.contains("..")

    internal fun validateBackupPhotoReferences(
        diaries: List<DiaryRecordEntity>,
        stagedPhotoNames: Set<String>
    ): Set<String> {
        val allNames = mutableListOf<String>()
        diaries.forEach { record ->
            val names = record.photoPaths.lineSequence().filter { it.isNotBlank() }.toList()
            require(names.size <= 5) { "A diary entry contains more than five photos" }
            require(names.distinct().size == names.size) { "A diary entry contains duplicate photo references" }
            names.forEach { name ->
                require(DiaryPhotoStore.isSafeName(name)) { "Invalid photo reference in backup" }
                allNames += name
            }
        }
        val required = allNames.toSet()
        require(required == stagedPhotoNames) { "Backup photos do not match the photos referenced by diary entries" }
        return required
    }

    private fun readLimited(input: java.io.InputStream, max: Long): ByteArray {
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(8192)
        var count = 0L
        while (true) {
            val read = input.read(buffer)
            if (read < 0) break
            count += read
            require(count <= max) { "Backup entry is too large" }
            output.write(buffer, 0, read)
        }
        return output.toByteArray()
    }

    private fun encode(data: BackupSnapshot): String {
        val root = JSONObject().put("backupVersion", BACKUP_VERSION).put("createdAt", System.currentTimeMillis())
        root.put("farmers", JSONArray().apply { data.farmers.forEach { put(JSONObject()
            .put("id", it.id).put("farmerName", it.farmerName).put("mobileNumber", it.mobileNumber)
            .put("village", it.village).put("tehsil", it.tehsil).put("district", it.district)
            .put("landArea", it.landArea).put("landUnit", it.landUnit).put("irrigationSource", it.irrigationSource)
            .put("mainCrops", it.mainCrops).put("livestock", it.livestock).put("updatedAt", it.updatedAt)) } })
        root.put("animals", JSONArray().apply { data.animals.forEach { put(JSONObject()
            .put("id", it.id).put("name", it.name).put("species", it.species).put("breed", it.breed)
            .put("tagNumber", it.tagNumber).put("sex", it.sex).put("ageOrDob", it.ageOrDob)
            .put("purpose", it.purpose).put("notes", it.notes).put("createdAt", it.createdAt).put("updatedAt", it.updatedAt)) } })
        root.put("animalHealth", JSONArray().apply { data.health.forEach { put(JSONObject()
            .put("id", it.id).put("animalId", it.animalId).put("eventTitle", it.eventTitle)
            .put("eventDate", it.eventDate).put("nextReminderDate", it.nextReminderDate)
            .put("veterinarianConfirmed", it.veterinarianConfirmed).put("notes", it.notes).put("createdAt", it.createdAt)
            .put("reminderNotified", it.reminderNotified)) } })
        root.put("plantDiagnoses", JSONArray().apply { data.plantDiagnoses.forEach { put(JSONObject()
            .put("id", it.id).put("predictedLabel", it.predictedLabel).put("confidence", it.confidence)
            .put("createdAt", it.createdAt).put("photoName", File(it.imagePath).name)) } })
        root.put("diaries", JSONArray().apply { data.diaries.forEach { put(JSONObject()
            .put("id", it.id).put("recordType", it.recordType).put("title", it.title).put("recordDate", it.recordDate)
            .put("subject", it.subject).put("quantity", it.quantity).put("unit", it.unit).put("amount", it.amount)
            .put("notes", it.notes).put("reminderDate", it.reminderDate).put("photoPaths", it.photoPaths)
            .put("createdAt", it.createdAt).put("updatedAt", it.updatedAt).put("reminderNotified", it.reminderNotified)) } })
        return root.toString()
    }

    private fun JSONArray.toFarmers() = (0 until length()).map { i -> getJSONObject(i).let {
        FarmerProfileEntity(it.optInt("id", 1), it.optString("farmerName"), it.optString("mobileNumber"),
            it.optString("village"), it.optString("tehsil"), it.optString("district"), it.optString("landArea"),
            it.optString("landUnit", "बीघा"), it.optString("irrigationSource"), it.optString("mainCrops"),
            it.optString("livestock"), it.optLong("updatedAt"))
    } }
    private fun JSONArray.toAnimals() = (0 until length()).map { i -> getJSONObject(i).let {
        AnimalRecordEntity(it.getLong("id"), it.getString("name"), it.getString("species"), it.optString("breed"),
            it.optString("tagNumber"), it.optString("sex"), it.optString("ageOrDob"), it.optString("purpose"),
            it.optString("notes"), it.optLong("createdAt"), it.optLong("updatedAt"))
    } }
    private fun JSONArray.toHealth() = (0 until length()).map { i -> getJSONObject(i).let {
        AnimalHealthRecordEntity(it.getLong("id"), it.getLong("animalId"), it.getString("eventTitle"),
            it.getString("eventDate"), it.optString("nextReminderDate"), it.optBoolean("veterinarianConfirmed"),
            it.optString("notes"), it.optLong("createdAt"), it.optBoolean("reminderNotified", false))
    } }
    private fun JSONArray.toDiaries() = (0 until length()).map { i -> getJSONObject(i).let {
        DiaryRecordEntity(it.getLong("id"), it.getString("recordType"), it.getString("title"), it.getString("recordDate"),
            it.optString("subject"), it.optDouble("quantity"), it.optString("unit"), it.optDouble("amount"),
            it.optString("notes"), it.optString("reminderDate"), it.optString("photoPaths"),
            it.optLong("createdAt"), it.optLong("updatedAt"), it.optBoolean("reminderNotified", false))
    } }
    private fun JSONArray.toPlantDiagnoses() = (0 until length()).map { i -> getJSONObject(i).let { json ->
        PlantDiagnosisEntity(
            id = json.getLong("id"), imagePath = json.getString("photoName"),
            predictedLabel = json.getString("predictedLabel"), confidence = json.getDouble("confidence").toFloat(),
            createdAt = json.getLong("createdAt")
        )
    } }
    private fun String.toPhotoNames(): List<String> = lineSequence().filter { it.isNotBlank() }.map {
        require(DiaryPhotoStore.isSafeName(it)) { "Invalid photo reference in backup" }
        it
    }.toList()

    private data class BackupSnapshot(
        val farmers: List<FarmerProfileEntity>, val animals: List<AnimalRecordEntity>,
        val health: List<AnimalHealthRecordEntity>, val diaries: List<DiaryRecordEntity>,
        val plantDiagnoses: List<PlantDiagnosisEntity>
    )
    data class RestoreSummary(val farmers: Int, val animals: Int, val healthRecords: Int, val diaryEntries: Int, val photos: Int, val plantDiagnoses: Int)
}

internal fun isSupportedDiaryBackupVersion(version: Int): Boolean = version in 1..3
