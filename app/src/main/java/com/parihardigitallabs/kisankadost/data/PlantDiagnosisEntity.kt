package com.parihardigitallabs.kisankadost.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "plant_diagnosis_history",
    indices = [Index(value = ["createdAt"])]
)
data class PlantDiagnosisEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imagePath: String,
    val predictedLabel: String,
    val confidence: Float,
    val createdAt: Long = System.currentTimeMillis()
)
