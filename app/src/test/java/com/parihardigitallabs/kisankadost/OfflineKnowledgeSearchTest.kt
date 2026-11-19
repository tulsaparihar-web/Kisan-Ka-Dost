package com.parihardigitallabs.kisankadost

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OfflineKnowledgeSearchTest {
    @Test
    fun blankQueryReturnsNoResults() {
        assertTrue(searchOfflineKnowledge("   ").isEmpty())
    }

    @Test
    fun englishQueryFindsRelevantCropEntry() {
        val results = searchOfflineKnowledge("yellow leaves")
        assertTrue(results.isNotEmpty())
        assertEquals("yellow-leaves", results.first().id)
    }

    @Test
    fun hindiQueryFindsRelevantEntry() {
        val results = searchOfflineKnowledge("पत्तियां पीली")
        assertTrue(results.any { it.id == "yellow-leaves" })
    }

    @Test
    fun pesticideSafetyQueryFindsSafetyGuidance() {
        val results = searchOfflineKnowledge("कीटनाशक सुरक्षा")
        assertTrue(results.any { it.id == "pesticide-safety" })
    }

    @Test
    fun queryCanSearchKeywordsAndAliases() {
        val results = searchOfflineKnowledge("KVK")
        assertTrue(results.any { it.id == "kvk" })
    }
}
