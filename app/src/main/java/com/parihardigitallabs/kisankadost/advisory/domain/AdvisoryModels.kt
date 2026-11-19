package com.parihardigitallabs.kisankadost.advisory.domain

/**
 * Normalized, source-attributed advisory data. These models contain no fabricated
 * weather values and can be populated only from a verified provider response.
 */
enum class DataFreshness { CURRENT, CACHED, EXPIRED, UNAVAILABLE }

enum class WarningSeverity { GREEN, YELLOW, ORANGE, RED, UNKNOWN }

data class SourceMetadata(
    val sourceName: String,
    val sourceUrl: String,
    val issuedAtEpochMillis: Long?,
    val validUntilEpochMillis: Long?,
    val fetchedAtEpochMillis: Long
)

data class DistrictWarning(
    val districtName: String,
    val issueDate: String,
    val issueTimeUtc: String?,
    val dayCodes: List<List<Int>>,
    val daySeverities: List<WarningSeverity>,
    val source: SourceMetadata
)

data class WeatherObservation(
    val stationName: String,
    val observedAtUtc: String,
    val temperatureCelsius: Double?,
    val humidityPercent: Int?,
    val windSpeedKmph: Double?,
    val rainfallLast24HoursMm: Double?,
    val source: SourceMetadata
)

data class AgrometAdvisory(
    val districtName: String,
    val bulletinTitle: String,
    val bulletinText: String,
    val issuedAtLabel: String,
    val validUntilLabel: String?,
    val language: String,
    val source: SourceMetadata
)

fun normalizeDistrictName(value: String): String =
    value.trim().lowercase().replace(Regex("\\s+"), " ")

fun parseImdWarningCodes(raw: String?): List<Int> {
    if (raw.isNullOrBlank()) return emptyList()
    return raw.split(",")
        .mapNotNull { token -> token.trim().toIntOrNull() }
        .filter { it in 1..17 }
        .distinct()
}

/** IMD district-warning color codes: 1 red, 2 orange, 3 yellow, 4 green. */
fun districtWarningSeverityFromImdColor(colorCode: Int?): WarningSeverity = when (colorCode) {
    1 -> WarningSeverity.RED
    2 -> WarningSeverity.ORANGE
    3 -> WarningSeverity.YELLOW
    4 -> WarningSeverity.GREEN
    else -> WarningSeverity.UNKNOWN
}

fun dataFreshness(
    fetchedAtEpochMillis: Long?,
    validUntilEpochMillis: Long?,
    nowEpochMillis: Long,
    maxAgeMillis: Long
): DataFreshness {
    if (fetchedAtEpochMillis == null) return DataFreshness.UNAVAILABLE
    if (validUntilEpochMillis != null && nowEpochMillis > validUntilEpochMillis) {
        return DataFreshness.EXPIRED
    }
    if (nowEpochMillis < fetchedAtEpochMillis) return DataFreshness.CACHED
    return if (nowEpochMillis - fetchedAtEpochMillis <= maxAgeMillis) {
        DataFreshness.CURRENT
    } else {
        DataFreshness.CACHED
    }
}
