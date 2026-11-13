package com.parihardigitallabs.kisankadost.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [FarmerProfileEntity::class, AnimalRecordEntity::class, AnimalHealthRecordEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun farmerProfileDao(): FarmerProfileDao
    abstract fun animalRecordDao(): AnimalRecordDao
    abstract fun animalHealthRecordDao(): AnimalHealthRecordDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS animal_records (
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
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS animal_health_records (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        animalId INTEGER NOT NULL,
                        eventTitle TEXT NOT NULL,
                        eventDate TEXT NOT NULL,
                        nextReminderDate TEXT NOT NULL,
                        veterinarianConfirmed INTEGER NOT NULL,
                        notes TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        FOREIGN KEY(animalId) REFERENCES animal_records(id) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                database.execSQL("CREATE INDEX IF NOT EXISTS index_animal_health_records_animalId ON animal_health_records(animalId)")
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kisan_ka_dost.db"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build().also { INSTANCE = it }
            }
    }
}
