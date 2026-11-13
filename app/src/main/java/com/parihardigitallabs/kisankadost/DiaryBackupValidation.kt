package com.parihardigitallabs.kisankadost

import com.parihardigitallabs.kisankadost.data.AnimalHealthRecordEntity
import com.parihardigitallabs.kisankadost.data.AnimalRecordEntity
import com.parihardigitallabs.kisankadost.data.DiaryRecordEntity
import com.parihardigitallabs.kisankadost.data.FarmerProfileEntity
import java.time.LocalDate

object DiaryBackupValidation {
    private val allowedTypes = setOf("FIELD", "GARDEN", "ANIMAL", "EXPENSE", "LABOUR", "INPUT", "PRODUCTION", "SALES")

    fun validate(
        farmers: List<FarmerProfileEntity>,
        animals: List<AnimalRecordEntity>,
        health: List<AnimalHealthRecordEntity>,
        diaries: List<DiaryRecordEntity>,
        photoFiles: Set<String>
    ): Set<String> {
        require(farmers.size <= 1 && farmers.all { it.id == 1 }) { "Backup must contain at most one farmer profile" }
        require(animals.size <= 5_000 && health.size <= 10_000 && diaries.size <= 10_000) {
            "Backup contains too many records"
        }
        require(photoFiles.size <= 25_000) { "Backup contains too many photos" }
        require(animals.map { it.id }.all { it > 0 } && animals.map { it.id }.distinct().size == animals.size) { "Animal IDs are invalid or duplicated" }
        require(health.map { it.id }.all { it > 0 } && health.map { it.id }.distinct().size == health.size) { "Health record IDs are invalid or duplicated" }
        require(diaries.map { it.id }.all { it > 0 } && diaries.map { it.id }.distinct().size == diaries.size) { "Diary record IDs are invalid or duplicated" }

        val animalIds = animals.map { it.id }.toSet()
        require(health.all { it.animalId in animalIds }) { "Backup has animal health records without a matching animal" }
        health.forEach {
            require(it.eventTitle.isNotBlank() && isDate(it.eventDate)) { "A health record has an invalid title or date" }
            require(it.nextReminderDate.isBlank() || (it.veterinarianConfirmed && isDate(it.nextReminderDate))) {
                "A health reminder must have a valid veterinarian-confirmed date"
            }
        }

        val referencedPhotos = linkedSetOf<String>()
        diaries.forEach { record ->
            require(record.recordType in allowedTypes) { "Backup contains an unknown diary category" }
            require(record.title.isNotBlank() && record.title.length <= 160) { "A diary title is invalid" }
            require(isDate(record.recordDate)) { "A diary record has an invalid date" }
            require(record.reminderDate.isBlank() || isDate(record.reminderDate)) { "A diary reminder date is invalid" }
            require(record.subject.length <= 120 && record.unit.length <= 40 && record.notes.length <= 3000) {
                "A diary record contains an oversized field"
            }
            require(record.quantity.isFinite() && record.quantity >= 0.0 && record.amount.isFinite() && record.amount >= 0.0) {
                "A diary quantity or amount is invalid"
            }
            val names = record.photoPaths.lineSequence().filter { it.isNotBlank() }.toList()
            require(names.size <= 5 && names.distinct().size == names.size) { "A diary entry has too many or duplicate photos" }
            names.forEach { name ->
                require(DiaryPhotoStore.isSafeName(name)) { "A diary entry contains an invalid photo filename" }
                referencedPhotos += name
            }
        }
        require(referencedPhotos == photoFiles) { "Backup photo files do not match the diary photo references" }
        return referencedPhotos
    }

    private fun isDate(value: String): Boolean = runCatching { LocalDate.parse(value); true }.getOrDefault(false)
}
