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
    private val openedDatabases = mutableListOf<AppDatabase>()
    private val databaseNames = mutableListOf<String>()

    @After
    fun tearDown() {
        openedDatabases.forEach { it.close() }
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        databaseNames.forEach(context::deleteDatabase)
    }

    @Test
    fun populatedVersionOneDatabaseUpgradesToSevenWithoutLosingFarmerData() = runBlocking {
        val db = openMigratedDatabase(1)
        assertFarmerPreserved(db)
        assertTrue(db.animalRecordDao().getAllForBackup().isEmpty())
        assertTrue(db.animalHealthRecordDao().getAllForBackup().isEmpty())
        assertTrue(db.diaryRecordDao().getAllForBackup().isEmpty())
        assertDiaryTableWritable(db)
    }

    @Test
    fun populatedVersionTwoDatabaseUpgradesToSevenWithoutLosingFarmerOrAnimalData() = runBlocking {
        val db = openMigratedDatabase(2)
        assertFarmerPreserved(db)
        val animal = db.animalRecordDao().getById(7L)
        assertEquals("Gauri", animal?.name)
        assertEquals("", animal?.breed)
        assertTrue(db.animalHealthRecordDao().getAllForBackup().isEmpty())
        assertTrue(db.diaryRecordDao().getAllForBackup().isEmpty())
        assertDiaryTableWritable(db)
    }

    @Test
    fun populatedVersionThreeDatabaseUpgradesToSevenWithoutLosingHealthHistory() = runBlocking {
        val db = openMigratedDatabase(3)
        assertFarmerPreserved(db)
        val animal = db.animalRecordDao().getById(7L)
        assertEquals("Gauri", animal?.name)
        assertEquals("", animal?.breed)

        val health = db.animalHealthRecordDao().getById(11L)
        assertEquals("Vaccination", health?.eventTitle)
        assertFalse(health?.reminderNotified ?: true)
        assertTrue(db.diaryRecordDao().getAllForBackup().isEmpty())
        assertDiaryTableWritable(db)

        // Historical health records must remain linked to their animal after all migrations.
        db.animalRecordDao().delete(animal!!)
        assertTrue(db.animalHealthRecordDao().getAllForBackup().isEmpty())
    }

    @Test
    fun populatedVersionFourDatabaseUpgradesToSevenWithoutLosingFarmerAnimalOrHealthData() = runBlocking {
        val db = openMigratedDatabase(4)
        assertFarmerPreserved(db)

        val animal = db.animalRecordDao().getById(7L)
        assertEquals("Gauri", animal?.name)
        assertEquals("Gir", animal?.breed)

        val health = db.animalHealthRecordDao().getById(11L)
        assertEquals("Vaccination", health?.eventTitle)
        assertFalse(health?.reminderNotified ?: true)

        assertTrue(db.diaryRecordDao().getAllForBackup().isEmpty())
        assertDiaryTableWritable(db)

        // Historical health records must remain linked to their animal after migration.
        db.animalRecordDao().delete(animal!!)
        assertTrue(db.animalHealthRecordDao().getAllForBackup().isEmpty())
    }

    private fun openMigratedDatabase(version: Int): AppDatabase {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val name = "migration_v${version}_to_v7_test.db"
        databaseNames += name
        val file = context.getDatabasePath(name)
        file.parentFile?.mkdirs()
        file.delete()

        createHistoricalDatabase(file, version)

        val db = androidx.room.Room.databaseBuilder(context, AppDatabase::class.java, name)
            .addMigrations(*AppDatabase.ALL_MIGRATIONS)
            .allowMainThreadQueries()
            .build()
        openedDatabases += db
        db.openHelper.writableDatabase
        return db
    }

    private suspend fun assertFarmerPreserved(db: AppDatabase) {
        val farmer = db.farmerProfileDao().getAllForBackup().single()
        assertEquals("Test Farmer", farmer.farmerName)
        assertEquals("Jaipur", farmer.district)
        assertEquals("Test Village", farmer.village)
    }

    private suspend fun assertDiaryTableWritable(db: AppDatabase) {
        db.diaryRecordDao().insert(
            DiaryRecordEntity(
                recordType = "FIELD",
                title = "Migration check",
                recordDate = "2026-11-23"
            )
        )
        assertEquals(1, db.diaryRecordDao().getAllForBackup().size)
    }

    /**
     * Builds historical schemas from the actual Room entity and migration definitions:
     * v1 = farmer profile; v2 = farmer + animals; v3 = adds animal health history;
     * v4 = adds the animal breed column.
     */
    private fun createHistoricalDatabase(file: File, version: Int) {
        val sqlite = SQLiteDatabase.openOrCreateDatabase(file, null)
        try {
            createFarmerTable(sqlite)
            insertFarmer(sqlite)

            if (version >= 2) {
                createAnimalTable(sqlite, includeBreed = version >= 4)
                insertAnimal(sqlite, includeBreed = version >= 4)
            }

            if (version >= 3) {
                createAnimalHealthTable(sqlite)
                insertAnimalHealth(sqlite)
            }

            sqlite.version = version
        } finally {
            sqlite.close()
        }
    }

    private fun createFarmerTable(sqlite: SQLiteDatabase) {
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
    }

    private fun insertFarmer(sqlite: SQLiteDatabase) {
        sqlite.execSQL(
            """INSERT INTO farmer_profile VALUES
                (1,'Test Farmer','9999999999','Test Village','Sanganer','Jaipur',
                '2','बीघा','Borewell','Wheat','Cow',1000)"""
        )
    }

    private fun createAnimalTable(sqlite: SQLiteDatabase, includeBreed: Boolean) {
        val breedColumn = if (includeBreed) ", breed TEXT NOT NULL DEFAULT ''" else ""
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
                updatedAt INTEGER NOT NULL
                $breedColumn
            )"""
        )
    }

    private fun insertAnimal(sqlite: SQLiteDatabase, includeBreed: Boolean) {
        val columns = if (includeBreed) {
            "id,name,species,tagNumber,sex,ageOrDob,purpose,notes,createdAt,updatedAt,breed"
        } else {
            "id,name,species,tagNumber,sex,ageOrDob,purpose,notes,createdAt,updatedAt"
        }
        val values = if (includeBreed) {
            "(7,'Gauri','Cow','TAG-7','Female','2022','Dairy','Healthy',1000,1000,'Gir')"
        } else {
            "(7,'Gauri','Cow','TAG-7','Female','2022','Dairy','Healthy',1000,1000)"
        }
        sqlite.execSQL("INSERT INTO animal_records ($columns) VALUES $values")
    }

    private fun createAnimalHealthTable(sqlite: SQLiteDatabase) {
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
    }

    private fun insertAnimalHealth(sqlite: SQLiteDatabase) {
        sqlite.execSQL(
            """INSERT INTO animal_health_records
                (id,animalId,eventTitle,eventDate,nextReminderDate,veterinarianConfirmed,notes,createdAt)
                VALUES (11,7,'Vaccination','2026-10-01','',0,'Historical note',1000)"""
        )
    }
}
