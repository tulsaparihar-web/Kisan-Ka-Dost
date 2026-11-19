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

    @Test
    fun acceptsOnlyPredictionsForTheSelectedCrop() {
        assertEquals(true, plantPredictionMatchesSelectedCrop("pepper bell healthy", "pepper bell"))
        assertEquals(false, plantPredictionMatchesSelectedCrop("pepper bell healthy", "tomato"))
        assertEquals(true, plantPredictionMatchesSelectedCrop("background", "tomato"))
        assertEquals(false, plantPredictionMatchesSelectedCrop("neem healthy", "tomato"))
    }
}
