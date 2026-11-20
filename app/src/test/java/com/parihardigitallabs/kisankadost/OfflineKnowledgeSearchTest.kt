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
    @Test
    fun titleMatchesRankAheadOfIncidentalAnswerMatches() {
        val results = searchOfflineKnowledge("soil test")
        assertEquals("soil-test", results.first().id)
    }

    @Test
    fun punctuationAndExtraWhitespaceDoNotBreakSearch() {
        val results = searchOfflineKnowledge("  yellow,   leaves! ")
        assertEquals("yellow-leaves", results.first().id)
    }

    @Test
    fun hindiTopicSearchRanksMatchingQuestionFirst() {
        val results = searchOfflineKnowledge("पशुओं को साफ पानी")
        assertEquals("animal-water", results.first().id)
    }


    @Test
    fun rajasthanRainfallQueryFindsLocalCropPlanningGuidance() {
        val results = searchOfflineKnowledge("राजस्थान खरीफ बाजरा ग्वार")
        assertTrue(results.any { it.id == "rajasthan-crop-planning" })
    }

    @Test
    fun fodderConservationQueryFindsLivestockGuidance() {
        val results = searchOfflineKnowledge("silage fodder चारा")
        assertTrue(results.any { it.id == "fodder-conservation" })
    }

    @Test
    fun calfColostrumQueryFindsNewbornCareGuidance() {
        val results = searchOfflineKnowledge("बछड़ा खीस colostrum")
        assertTrue(results.any { it.id == "calf-colostrum" })
    }

    @Test
    fun orchardAndHomeGardenTopicsAreSearchable() {
        assertTrue(searchOfflineKnowledge("फलदार बाग orchard").any { it.id == "orchard-planting" })
        assertTrue(searchOfflineKnowledge("गमला गर्मी home garden").any { it.id == "summer-garden" })
    }

    @Test
    fun cleanMilkingAndVaccinationTopicsAreSearchable() {
        assertTrue(searchOfflineKnowledge("दूध दुहना hygiene").any { it.id == "clean-milking" })
        assertTrue(searchOfflineKnowledge("पशु vaccination टीकाकरण").any { it.id == "livestock-vaccination" })
    }

    @Test
    fun cropResidueAndRainfallRiskTopicsAreSearchable() {
        assertTrue(searchOfflineKnowledge("पराली crop residue").any { it.id == "crop-residue" })
        assertTrue(searchOfflineKnowledge("तेज बारिश जलभराव").any { it.id == "rainfall-waterlogging" })
    }
}
