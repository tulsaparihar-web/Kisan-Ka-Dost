package com.parihardigitallabs.kisankadost
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.*
class DiaryReminderSchedulerTest {
 @Test fun todayReminderUsesNineAm() {
  val now=ZonedDateTime.of(LocalDate.of(2026,11,13),LocalTime.of(8,0),ZoneId.of("Asia/Kolkata"))
  assertEquals(3600000L,calculateDiaryReminderDelayMillis("2026-11-13",now))
 }
 @Test fun tomorrowReminderUsesLocalNineAm() {
  val now=ZonedDateTime.of(LocalDate.of(2026,11,13),LocalTime.of(10,0),ZoneId.of("Asia/Kolkata"))
  assertEquals(23L*60*60*1000,calculateDiaryReminderDelayMillis("2026-11-14",now))
 }
 @Test fun passedReminderDateSchedulesImmediately() {
  val now=ZonedDateTime.of(LocalDate.of(2026,11,13),LocalTime.of(10,0),ZoneId.of("Asia/Kolkata"))
  assertEquals(0L,calculateDiaryReminderDelayMillis("2026-11-12",now))
 }
}
