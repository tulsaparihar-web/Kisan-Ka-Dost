package com.parihardigitallabs.kisankadost

import com.parihardigitallabs.kisankadost.data.DiaryRecordEntity
import org.junit.Assert.*
import org.junit.Test

class DiaryCsvExporterTest {
    @Test fun quotesCommasAndEmbeddedQuotes() {
        assertEquals("\"a,b\"", DiaryCsvExporter.csvCell("a,b"))
        assertEquals("\"say \"\"hi\"\"\"", DiaryCsvExporter.csvCell("say \"hi\""))
    }

    @Test fun protectsFormulaLikeUserText() {
        assertEquals("\"'=HYPERLINK(1)\"", DiaryCsvExporter.csvCell("=HYPERLINK(1)"))
    }

    @Test fun exportsPhotoCount() {
        val record = DiaryRecordEntity(recordType="FIELD", title="Irrigation", recordDate="2026-11-13", photoPaths="one.jpg\ntwo.jpg")
        assertTrue(DiaryCsvExporter.export(listOf(record)).endsWith(",\"2\""))
    }
}
