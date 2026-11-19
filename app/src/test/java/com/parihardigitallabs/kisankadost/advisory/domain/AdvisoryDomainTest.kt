package com.parihardigitallabs.kisankadost.advisory.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class AdvisoryDomainTest {
    @Test
    fun parsesDistrictWarningCodesAndIgnoresInvalidTokens() {
        assertEquals(listOf(2, 4, 9), parseImdWarningCodes("2, 4,invalid,9,99,2"))
        assertEquals(emptyList<Int>(), parseImdWarningCodes(null))
        assertEquals(emptyList<Int>(), parseImdWarningCodes(" , "))
    }

    @Test
    fun mapsImdDistrictWarningColorsCorrectly() {
        assertEquals(WarningSeverity.RED, districtWarningSeverityFromImdColor(1))
        assertEquals(WarningSeverity.ORANGE, districtWarningSeverityFromImdColor(2))
        assertEquals(WarningSeverity.YELLOW, districtWarningSeverityFromImdColor(3))
        assertEquals(WarningSeverity.GREEN, districtWarningSeverityFromImdColor(4))
        assertEquals(WarningSeverity.UNKNOWN, districtWarningSeverityFromImdColor(null))
    }

    @Test
    fun normalizesDistrictNamesWithoutChangingMeaning() {
        assertEquals("sri ganganagar", normalizeDistrictName("  Sri   Ganganagar "))
    }

    @Test
    fun classifiesCurrentCachedExpiredAndUnavailableData() {
        val now = 10_000L
        assertEquals(DataFreshness.CURRENT, dataFreshness(now - 1_000, now + 5_000, now, 3_600_000))
        assertEquals(DataFreshness.CACHED, dataFreshness(now - 5_000_000, now + 5_000, now, 3_600_000))
        assertEquals(DataFreshness.EXPIRED, dataFreshness(now - 1_000, now - 1, now, 3_600_000))
        assertEquals(DataFreshness.UNAVAILABLE, dataFreshness(null, null, now, 3_600_000))
    }

    @Test
    fun futureFetchedTimestampIsNotMarkedCurrent() {
        assertEquals(DataFreshness.CACHED, dataFreshness(20_000, null, 10_000, 3_600_000))
    }
}
