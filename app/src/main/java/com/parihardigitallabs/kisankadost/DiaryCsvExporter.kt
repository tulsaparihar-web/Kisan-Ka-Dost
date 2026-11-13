package com.parihardigitallabs.kisankadost
import com.parihardigitallabs.kisankadost.data.DiaryRecordEntity
object DiaryCsvExporter {
 fun export(records:List<DiaryRecordEntity>):String {
  val rows=listOf(listOf("Date","Type","Title","Related item","Quantity","Unit","Amount INR","Notes","Reminder date"))+records.map { r ->
   listOf(r.recordDate,r.recordType,r.title,r.subject,if(r.quantity==0.0)"" else r.quantity.toString(),r.unit,if(r.amount==0.0)"" else r.amount.toString(),r.notes,r.reminderDate)
  }
  return rows.joinToString("\r\n") { row -> row.joinToString(",") { v -> "\"" + v.replace("\"", "\"\"") + "\"" } }
 }
}
