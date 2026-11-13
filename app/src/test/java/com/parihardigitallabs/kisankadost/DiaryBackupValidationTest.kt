package com.parihardigitallabs.kisankadost

import com.parihardigitallabs.kisankadost.data.AnimalHealthRecordEntity
import com.parihardigitallabs.kisankadost.data.AnimalRecordEntity
import com.parihardigitallabs.kisankadost.data.DiaryRecordEntity
import com.parihardigitallabs.kisankadost.data.FarmerProfileEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DiaryBackupValidationTest {
    private val animal = AnimalRecordEntity(id = 7, name = "Gauri", species = "Cattle")
    private val diary = DiaryRecordEntity(
        id = 11, recordType = "FIELD", title = "Irrigation", recordDate = "2026-11-13",
        photoPaths = "diary_photo-1.jpg"
    )
    private val photoFiles = setOf("diary_photo-1.jpg")

    @Test fun acceptsValidLinkedFarmBackup() {
        val result = DiaryBackupValidation.validate(
            listOf(FarmerProfileEntity()), listOf(animal),
            listOf(AnimalHealthRecordEntity(id=4, animalId=7, eventTitle="Vet visit", eventDate="2026-11-10")),
            listOf(diary), photoFiles
        )
        assertEquals(photoFiles, result)
    }

    @Test fun rejectsHealthRecordForMissingAnimal() {
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupValidation.validate(emptyList(), listOf(animal),
                listOf(AnimalHealthRecordEntity(id=4, animalId=99, eventTitle="Visit", eventDate="2026-11-10")),
                emptyList(), emptySet())
        }
    }

    @Test fun rejectsPhotoMissingFromArchive() {
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupValidation.validate(emptyList(), emptyList(), emptyList(), listOf(diary), emptySet())
        }
    }

    @Test fun rejectsUnconfirmedVeterinaryReminder() {
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupValidation.validate(emptyList(), listOf(animal),
                listOf(AnimalHealthRecordEntity(id=4, animalId=7, eventTitle="Visit", eventDate="2026-11-10",
                    nextReminderDate="2026-11-20", veterinarianConfirmed=false)), emptyList(), emptySet())
        }
    }

    @Test fun rejectsInvalidDateAndUnsupportedCategory() {
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupValidation.validate(emptyList(), emptyList(), emptyList(),
                listOf(diary.copy(recordDate="13-11-2026")), photoFiles)
        }
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupValidation.validate(emptyList(), emptyList(), emptyList(),
                listOf(diary.copy(recordType="UNKNOWN")), photoFiles)
        }
    }

    @Test fun acceptsSalesRecords() {
        val sale = diary.copy(recordType="SALES", title="Milk sale", photoPaths="")
        assertEquals(emptySet<String>(), DiaryBackupValidation.validate(emptyList(), emptyList(), emptyList(), listOf(sale), emptySet()))
    }
}
