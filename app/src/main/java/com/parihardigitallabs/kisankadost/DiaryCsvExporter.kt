package com.parihardigitallabs.kisankadost

import com.parihardigitallabs.kisankadost.data.DiaryRecordEntity

object DiaryCsvExporter {
    fun export(records: List<DiaryRecordEntity>): String {
        val header = listOf("Date", "Type", "Title", "Related item", "Quantity", "Unit", "Amount INR", "Notes", "Reminder date", "Photo count")
        val rows = listOf(header) + records.map { r ->
            listOf(r.recordDate, r.recordType, r.title, r.subject,
                if (r.quantity == 0.0) "" else r.quantity.toString(), r.unit,
                if (r.amount == 0.0) "" else r.amount.toString(), r.notes, r.reminderDate,
                r.photoPaths.lineSequence().count { it.isNotBlank() }.toString())
        }
        return rows.joinToString("\r\n") { row -> row.joinToString(",") { csvCell(it) } }
    }

    internal fun csvCell(value: String): String {
        val safe = if (value.firstOrNull() in listOf('=', '+', '-', '@', '\t', '\r')) "'$value" else value
        return "\"" + safe.replace("\"", "\"\"") + "\""
    }
}
