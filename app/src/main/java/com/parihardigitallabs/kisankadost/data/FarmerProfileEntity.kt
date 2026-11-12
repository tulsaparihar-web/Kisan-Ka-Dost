package com.parihardigitallabs.kisankadost.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farmer_profile")
data class FarmerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val farmerName: String = "",
    val mobileNumber: String = "",
    val village: String = "",
    val tehsil: String = "",
    val district: String = "",
    val landArea: String = "",
    val landUnit: String = "बीघा",
    val irrigationSource: String = "",
    val mainCrops: String = "",
    val livestock: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
