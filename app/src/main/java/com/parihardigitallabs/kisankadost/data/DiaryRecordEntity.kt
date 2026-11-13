package com.parihardigitallabs.kisankadost.data
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "diary_records")
data class DiaryRecordEntity(
 @PrimaryKey(autoGenerate = true) val id: Long = 0,
 val recordType: String, val title: String, val recordDate: String,
 val subject: String = "", val quantity: Double = 0.0, val unit: String = "",
 val amount: Double = 0.0, val notes: String = "", val reminderDate: String = "",
 val createdAt: Long = System.currentTimeMillis(), val updatedAt: Long = System.currentTimeMillis()
)
