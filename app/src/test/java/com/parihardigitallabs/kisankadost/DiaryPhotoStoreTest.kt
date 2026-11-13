package com.parihardigitallabs.kisankadost

import org.junit.Assert.*
import org.junit.Test

class DiaryPhotoStoreTest {
    @Test fun largeLandscapeImageIsDownsampledWithinLimit() {
        val sample = DiaryPhotoStore.calculateSampleSize(4000, 3000, 1600)
        assertTrue(sample > 1)
        assertTrue(4000 / sample <= 1600)
        assertTrue(3000 / sample <= 1600)
    }

    @Test fun smallImageKeepsFullResolution() {
        assertEquals(1, DiaryPhotoStore.calculateSampleSize(1200, 900, 1600))
    }

    @Test fun photoNamesRejectTraversalAndPathSeparators() {
        assertTrue(DiaryPhotoStore.isSafeName("diary_abc-123.jpg"))
        assertFalse(DiaryPhotoStore.isSafeName("../private.jpg"))
        assertFalse(DiaryPhotoStore.isSafeName("photos/private.jpg"))
        assertFalse(DiaryPhotoStore.isSafeName(""))
    }
}
