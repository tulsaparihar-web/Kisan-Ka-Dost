package com.parihardigitallabs.kisankadost

import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class AnimalReminderSchedulerTest {
    private val zone = ZoneId.of("Asia/Kolkata")

    @Test
    fun reminderForTodaySchedulesAtNineInTheMorning() {
        val now = ZonedDateTime.of(2026, 11, 13, 8, 0, 0, 0, zone)
        assertEquals(
            60L * 60L * 1000L,
            calculateReminderDelayMillis("2026-11-13", now)
        )
    }

    @Test
    fun reminderForTomorrowSchedulesTwentyFourHoursAhead() {
        val now = ZonedDateTime.of(2026, 11, 13, 9, 0, 0, 0, zone)
        assertEquals(
            24L * 60L * 60L * 1000L,
            calculateReminderDelayMillis("2026-11-14", now)
        )
    }

    @Test
    fun reminderDateThatHasPassedRunsWithoutNegativeDelay() {
        val now = ZonedDateTime.of(2026, 11, 13, 10, 0, 0, 0, zone)
        assertEquals(0L, calculateReminderDelayMillis("2026-11-13", now))
    }
}
