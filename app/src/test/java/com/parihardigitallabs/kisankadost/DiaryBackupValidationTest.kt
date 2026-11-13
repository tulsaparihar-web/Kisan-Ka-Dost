package com.parihardigitallabs.kisankadost

import com.parihardigitallabs.kisankadost.data.AnimalHealthRecordEntity
import com.parihardigitallabs.kisankadost.data.AnimalRecordEntity
import com.parihardigitallabs.kisankadost.data.DiaryRecordEntity
import com.parihardigitallabs.kisankadost.data.FarmerProfileEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DiaryBackupValidationTest {
    @Test fun acceptsCurrentAndPreviousBackupVersions() {
        org.junit.Assert.assertTrue(isSupportedDiaryBackupVersion(1))
        org.junit.Assert.assertTrue(isSupportedDiaryBackupVersion(2))
        org.junit.Assert.assertFalse(isSupportedDiaryBackupVersion(0))
        org.junit.Assert.assertFalse(isSupportedDiaryBackupVersion(3))
    }

    @Test fun acceptsExactPhotoManifest() {
        val entry = DiaryRecordEntity(
            id = 1, recordType = "FIELD", title = "Irrigation", recordDate = "2026-11-13",
            photoPaths = "one.jpg\ntwo.jpg"
        )
        assertEquals(setOf("one.jpg", "two.jpg"), DiaryBackupManager.validateBackupPhotoReferences(listOf(entry), setOf("one.jpg", "two.jpg")))
    }

    @Test fun rejectsMissingOrUnreferencedPhotos() {
        val entry = DiaryRecordEntity(
            id = 1, recordType = "FIELD", title = "Irrigation", recordDate = "2026-11-13",
            photoPaths = "one.jpg"
        )
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupManager.validateBackupPhotoReferences(listOf(entry), emptySet())
        }
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupManager.validateBackupPhotoReferences(listOf(entry), setOf("one.jpg", "unused.jpg"))
        }
    }

    @Test fun rejectsTooManyOrDuplicatePhotosPerEntry() {
        val tooMany = DiaryRecordEntity(
            id = 1, recordType = "FIELD", title = "Field", recordDate = "2026-11-13",
            photoPaths = (1..6).joinToString("\n") { "photo$it.jpg" }
        )
        val duplicate = DiaryRecordEntity(
            id = 2, recordType = "FIELD", title = "Field", recordDate = "2026-11-13",
            photoPaths = "same.jpg\nsame.jpg"
        )
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupManager.validateBackupPhotoReferences(listOf(tooMany), (1..6).map { "photo$it.jpg" }.toSet())
        }
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupManager.validateBackupPhotoReferences(listOf(duplicate), setOf("same.jpg"))
        }
    }

    @Test fun rejectsInvalidAmountsAndDuplicateDiaryIds() {
        val invalidAmount = DiaryRecordEntity(
            id = 1, recordType = "EXPENSE", title = "Feed", recordDate = "2026-11-13", amount = Double.NaN
        )
        val first = DiaryRecordEntity(id = 2, recordType = "FIELD", title = "Sowing", recordDate = "2026-11-13")
        val duplicate = first.copy(title = "Irrigation")
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupManager.validateBackupRecords(emptyList(), emptyList(), emptyList(), listOf(invalidAmount))
        }
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupManager.validateBackupRecords(emptyList(), emptyList(), emptyList(), listOf(first, duplicate))
        }
    }

    @Test fun rejectsOrphanAnimalHealthRecords() {
        val animal = AnimalRecordEntity(id = 1, name = "Gauri", species = "Cattle")
        val health = AnimalHealthRecordEntity(
            id = 1, animalId = 2, eventTitle = "Check-up", eventDate = "2026-11-13"
        )
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupManager.validateBackupRecords(emptyList(), listOf(animal), listOf(health), emptyList())
        }
    }

    @Test fun validatesDiaryDateAndFarmerSingleton() {
        val invalidDate = DiaryRecordEntity(
            id = 1, recordType = "FIELD", title = "Sowing", recordDate = "not-a-date"
        )
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupManager.validateBackupRecords(emptyList(), emptyList(), emptyList(), listOf(invalidDate))
        }
        assertThrows(IllegalArgumentException::class.java) {
            DiaryBackupManager.validateBackupRecords(
                listOf(FarmerProfileEntity(id = 1), FarmerProfileEntity(id = 1)),
                emptyList(), emptyList(), emptyList()
            )
        }
    }
}
