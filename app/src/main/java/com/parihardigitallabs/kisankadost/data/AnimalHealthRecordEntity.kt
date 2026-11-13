package com.parihardigitallabs.kisankadost.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "animal_health_records",
    foreignKeys = [
        ForeignKey(
            entity = AnimalRecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["animalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["animalId"])]
)
data class AnimalHealthRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val animalId: Long,
    val eventTitle: String,
    val eventDate: String,
    val nextReminderDate: String = "",
    val veterinarianConfirmed: Boolean = false,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(defaultValue = "0") val reminderNotified: Boolean = false
)
