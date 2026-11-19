package com.parihardigitallabs.kisankadost.data

import android.database.sqlite.SQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {
    private var migratedDatabase: AppDatabase? = null
    private var databaseName: String? = null

    @After
    fun tearDown() {
        migratedDatabase?.close()
        databaseName?.let {
            InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase(it)
        }
    }

    @Test
    fun versionFourDatabaseUpgradesToSevenWithoutLosingFarmerAnimalOrHealthData() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val name = "migration_v4_to_v7_test.db"
        databaseName = name
        val file = context.getDatabasePath(name)
        file.parentFile?.mkdirs()
        file.delete()

        createVersionFourDatabase(file)

        val db = androidx.room.Room.databaseBuilder(context, AppDatabase::class.java, name)
            .addMigrations(*AppDatabase.ALL_MIGRATIONS)
            .allowMainThreadQueries()
            .build()
        migratedDatabase = db
        db.openHelper.writableDatabase

        val farmer = db.farmerProfileDao().getAllForBackup().single()
        assertEquals("Test Farmer", farmer.farmerName)
        assertEquals("Jaipur", farmer.district)

        val animal = db.animalRecordDao().getById(7L)
        assertEquals("Gauri", animal?.name)
        assertEquals("Gir", animal?.breed)

        val health = db.animalHealthRecordDao().getById(11L)
        assertEquals("Vaccination", health?.eventTitle)
        assertFalse(health?.reminderNotified ?: true)

        val diary = db.diaryRecordDao().getAllForBackup()
        assertTrue(diary.isEmpty())

        db.diaryRecordDao().insert(
            DiaryRecordEntity(
                recordType = "FIELD",
                title = "Migration check",
                recordDate = "2026-11-19"
            )
        )
        assertEquals(1, db.diaryRecordDao().getAllForBackup().size)

        // The historical health record must still be linked to its animal after migration.
        db.animalRecordDao().delete(animal!!)
        assertTrue(db.animalHealthRecordDao().getAllForBackup().isEmpty())
    }

    private fun createVersionFourDatabase(file: File) {
        val sqlite = SQLiteDatabase.openOrCreateDatabase(file, null)
        try {
            sqlite.execSQL(
                """CREATE TABLE farmer_profile (
                    id INTEGER NOT NULL PRIMARY KEY,
                    farmerName TEXT NOT NULL,
                    mobileNumber TEXT NOT NULL,
                    village TEXT NOT NULL,
                    tehsil TEXT NOT NULL,
                    district TEXT NOT NULL,
                    landArea TEXT NOT NULL,
                    landUnit TEXT NOT NULL,
                    irrigationSource TEXT NOT NULL,
                    mainCrops TEXT NOT NULL,
                    livestock TEXT NOT NULL,
                    updatedAt INTEGER NOT NULL
                )"""
            )
            sqlite.execSQL(
                """CREATE TABLE animal_records (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    species TEXT NOT NULL,
                    tagNumber TEXT NOT NULL,
                    sex TEXT NOT NULL,
                    ageOrDob TEXT NOT NULL,
                    purpose TEXT NOT NULL,
                    notes TEXT NOT NULL,
                    createdAt INTEGER NOT NULL,
                    updatedAt INTEGER NOT NULL,
                    breed TEXT NOT NULL DEFAULT ''
                )"""
            )
            sqlite.execSQL(
                """CREATE TABLE animal_health_records (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    animalId INTEGER NOT NULL,
                    eventTitle TEXT NOT NULL,
                    eventDate TEXT NOT NULL,
                    nextReminderDate TEXT NOT NULL,
                    veterinarianConfirmed INTEGER NOT NULL,
                    notes TEXT NOT NULL,
                    createdAt INTEGER NOT NULL,
                    FOREIGN KEY(animalId) REFERENCES animal_records(id)
                        ON UPDATE NO ACTION ON DELETE CASCADE
                )"""
            )
            sqlite.execSQL(
                "CREATE INDEX index_animal_health_records_animalId ON animal_health_records(animalId)"
            )
            sqlite.execSQL(
                """INSERT INTO farmer_profile VALUES
                    (1,'Test Farmer','9999999999','Test Village','Sanganer','Jaipur',
                    '2','बीघा','Borewell','Wheat','Cow',1000)"""
            )
            sqlite.execSQL(
                """INSERT INTO animal_records
                    (id,name,species,tagNumber,sex,ageOrDob,purpose,notes,createdAt,updatedAt,breed)
                    VALUES (7,'Gauri','Cow','TAG-7','Female','2022','Dairy','Healthy',1000,1000,'Gir')"""
            )
            sqlite.execSQL(
                """INSERT INTO animal_health_records
                    (id,animalId,eventTitle,eventDate,nextReminderDate,veterinarianConfirmed,notes,createdAt)
                    VALUES (11,7,'Vaccination','2026-10-01','',0,'Historical note',1000)"""
            )
            sqlite.version = 4
        } finally {
            sqlite.close()
        }
    }
}
