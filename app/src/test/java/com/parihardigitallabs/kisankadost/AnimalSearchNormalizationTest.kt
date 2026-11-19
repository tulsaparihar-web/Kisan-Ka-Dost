package com.parihardigitallabs.kisankadost

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AnimalSearchNormalizationTest {
    private fun matches(key: String, query: String): Boolean = animalSearchMatches(
        key = key,
        titleHi = when (key) {
            "Cattle" -> "गाय / बैल"
            "Buffalo" -> "भैंस"
            "Goat" -> "बकरी"
            "Sheep" -> "भेड़"
            "Camel" -> "ऊँट"
            else -> "मुर्गीपालन"
        },
        titleEn = key,
        breedsHi = when (key) {
            "Cattle" -> "राठी, थारपारकर"
            "Goat" -> "सिरोही, जखराना"
            else -> ""
        },
        breedsEn = when (key) {
            "Cattle" -> "Rathi, Tharparkar"
            "Goat" -> "Sirohi, Jakhrana"
            else -> ""
        },
        normalizedQuery = normalizeAnimalSearch(query)
    )

    @Test
    fun matchesCommonRomanizedAnimalNames() {
        assertTrue(matches("Cattle", "gaay"))
        assertTrue(matches("Buffalo", "bhains"))
        assertTrue(matches("Goat", "bakri"))
        assertTrue(matches("Sheep", "bhed"))
        assertTrue(matches("Camel", "oont"))
        assertTrue(matches("Poultry", "murghi"))
    }

    @Test
    fun matchesBreedNamesAndCommonSpellings() {
        assertTrue(matches("Cattle", "Tharparker"))
        assertTrue(matches("Goat", "Jakrana"))
        assertTrue(matches("Goat", "Sirohi"))
    }

    @Test
    fun matchesHindiNamesAndIgnoresSpacesAndCase() {
        assertTrue(matches("Buffalo", "भैंस"))
        assertTrue(matches("Cattle", "  RATHI "))
    }

    @Test
    fun doesNotReturnUnrelatedAnimalGroups() {
        assertFalse(matches("Goat", "bhains"))
        assertFalse(matches("Camel", "murghi"))
    }

    @Test
    fun blankSearchMatchesAllGroups() {
        assertTrue(matches("Cattle", "   "))
    }
}
