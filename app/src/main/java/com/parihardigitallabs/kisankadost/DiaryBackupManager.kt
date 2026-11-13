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
    private const val BACKUP_VERSION = 1
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
                diaries = db.diaryRecordDao().getAllForBackup()
            )
        }
        validateBackupRecords(snapshot.farmers, snapshot.animals, snapshot.health, snapshot.diaries)
        val photoNames = validateBackupPhotoReferences(
            snapshot.diaries,
            snapshot.diaries.flatMap { it.photoPaths.toPhotoNames() }.toSet()
        )
        val json = encode(snapshot).toByteArray(Charsets.UTF_8)
        require(json.size <= MAX_JSON_BYTES) { "Backup data is too large" }
        require(photoNames.size + 1 <= MAX_ENTRIES) { "Backup contains too many photo files" }
        val photos = photoNames.map { name ->
            val photo = DiaryPhotoStore.file(context, name) ?: throw IOException("An attached diary photo is missing")
            require(photo.length() in 1..MAX_PHOTO_BYTES) { "An attached diary photo is too large or empty" }
            name to photo
        }
        val uncompressedBytes = json.size.toLong() + photos.sumOf { it.second.length() }
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
                            stagedPhotos[name] = file
                        }
                        else -> throw IOException("Backup contains an unsupported file")
                    }
                    require(totalBytes <= MAX_ARCHIVE_BYTES) { "Backup archive is too large" }
                    zip.closeEntry()
                }
            }
            val root = JSONObject(String(jsonBytes ?: throw IOException("Backup data is missing"), Charsets.UTF_8))
            require(root.optInt("backupVersion", -1) == BACKUP_VERSION) { "Unsupported backup version" }
            val farmers = root.getJSONArray("farmers").toFarmers()
            val animals = root.getJSONArray("animals").toAnimals()
            val health = root.getJSONArray("animalHealth").toHealth()
            val diaries = root.getJSONArray("diaries").toDiaries()
            validateBackupRecords(farmers, animals, health, diaries)
            val requiredPhotos = validateBackupPhotoReferences(
                diaries,
                stagedPhotos.keys
            )

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
            val previousHealth = db.animalHealthRecordDao().getAllForBackup()
            previousDiaries.forEach { DiaryReminderScheduler.cancel(context, it.id) }
            previousHealth.forEach { AnimalReminderScheduler.cancel(context, it.id) }
            try {
                db.withTransaction {
                    db.animalHealthRecordDao().clearAll()
                    db.diaryRecordDao().clearAll()
                    db.animalRecordDao().clearAll()
                    db.farmerProfileDao().clearAll()
                    if (farmers.isNotEmpty()) db.farmerProfileDao().insertAll(farmers)
                    if (animals.isNotEmpty()) db.animalRecordDao().insertAll(animals)
                    if (health.isNotEmpty()) db.animalHealthRecordDao().insertAll(health)
                    if (restoredDiaries.isNotEmpty()) db.diaryRecordDao().insertAll(restoredDiaries)
                }
            } catch (failure: Throwable) {
                copiedPhotos.forEach { it.delete() }
                rescheduleReminders(context, previousDiaries, previousHealth)
                throw failure
            }
            restoreCommitted = true
            photoDirectory.listFiles()?.filter { it !in copiedPhotos }?.forEach { it.delete() }
            rescheduleReminders(context, db)
            RestoreSummary(farmers.size, animals.size, health.size, diaries.size, requiredPhotos.size)
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
        diaries.filter { it.reminderDate.isNotBlank() }.forEach { record ->
            runCatching { DiaryReminderScheduler.schedule(context, record.id, record.reminderDate) }
        }
        health.filter { it.veterinarianConfirmed && it.nextReminderDate.isNotBlank() }.forEach { record ->
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
        require(animals.all { it.id > 0 && it.name.isNotBlank() && it.species.isNotBlank() }) { "Backup contains an invalid animal profile" }
        require(animals.map { it.id }.distinct().size == animals.size) { "Backup contains duplicate animal IDs" }
        val animalIds = animals.map { it.id }.toSet()
        require(health.all { it.id > 0 && it.animalId in animalIds }) { "Backup has animal health records without a matching animal" }
        require(health.map { it.id }.distinct().size == health.size) { "Backup contains duplicate animal health IDs" }
        health.forEach {
            require(runCatching { LocalDate.parse(it.eventDate) }.isSuccess) { "Backup contains an invalid animal health date" }
            require(it.nextReminderDate.isBlank() || runCatching { LocalDate.parse(it.nextReminderDate) }.isSuccess) { "Backup contains an invalid animal reminder date" }
        }
        require(diaries.all { it.id > 0 && it.title.isNotBlank() && it.recordType in setOf("FIELD", "GARDEN", "ANIMAL", "EXPENSE", "LABOUR", "INPUT", "PRODUCTION") }) {
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
            .put("veterinarianConfirmed", it.veterinarianConfirmed).put("notes", it.notes).put("createdAt", it.createdAt)) } })
        root.put("diaries", JSONArray().apply { data.diaries.forEach { put(JSONObject()
            .put("id", it.id).put("recordType", it.recordType).put("title", it.title).put("recordDate", it.recordDate)
            .put("subject", it.subject).put("quantity", it.quantity).put("unit", it.unit).put("amount", it.amount)
            .put("notes", it.notes).put("reminderDate", it.reminderDate).put("photoPaths", it.photoPaths)
            .put("createdAt", it.createdAt).put("updatedAt", it.updatedAt)) } })
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
            it.optString("notes"), it.optLong("createdAt"))
    } }
    private fun JSONArray.toDiaries() = (0 until length()).map { i -> getJSONObject(i).let {
        DiaryRecordEntity(it.getLong("id"), it.getString("recordType"), it.getString("title"), it.getString("recordDate"),
            it.optString("subject"), it.optDouble("quantity"), it.optString("unit"), it.optDouble("amount"),
            it.optString("notes"), it.optString("reminderDate"), it.optString("photoPaths"),
            it.optLong("createdAt"), it.optLong("updatedAt"))
    } }
    private fun String.toPhotoNames(): List<String> = lineSequence().filter { it.isNotBlank() }.map {
        require(DiaryPhotoStore.isSafeName(it)) { "Invalid photo reference in backup" }
        it
    }.toList()

    private data class BackupSnapshot(
        val farmers: List<FarmerProfileEntity>, val animals: List<AnimalRecordEntity>,
        val health: List<AnimalHealthRecordEntity>, val diaries: List<DiaryRecordEntity>
    )
    data class RestoreSummary(val farmers: Int, val animals: Int, val healthRecords: Int, val diaryEntries: Int, val photos: Int)
}
