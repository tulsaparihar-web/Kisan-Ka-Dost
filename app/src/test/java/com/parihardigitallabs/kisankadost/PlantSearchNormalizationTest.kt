package com.parihardigitallabs.kisankadost

import org.junit.Assert.assertEquals
import org.junit.Test

class PlantSearchNormalizationTest {
    @Test
    fun removesSpacesAndPunctuation() {
        assertEquals("moneyplant", normalizePlantQuery("Money Plant"))
        assertEquals("moneyplant", normalizePlantQuery("money-plant"))
    }

    @Test
    fun ignoresCase() {
        assertEquals(normalizePlantQuery("Papaya"), normalizePlantQuery("PAPAYA"))
    }

    @Test
    fun preservesDevanagariLettersWhileRemovingSpaces() {
        assertEquals("करीपत्ता", normalizePlantQuery("करी पत्ता"))
    }

    @Test
    fun removesZeroWidthCharacters() {
        assertEquals("tulsi", normalizePlantQuery("tu\u200Bl\u200Bsi"))
    }
}
