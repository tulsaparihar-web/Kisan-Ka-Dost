package com.parihardigitallabs.kisankadost.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal_records")
data class AnimalRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val species: String,
    @ColumnInfo(defaultValue = "''") val breed: String = "",
    val tagNumber: String = "",
    val sex: String = "",
    val ageOrDob: String = "",
    val purpose: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
