package com.parihardigitallabs.kisankadost

import android.graphics.Bitmap
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.UUID

/** Optional online species identification through the user's own Pl@ntNet API account. */
object PlantNetApi {
    internal fun parseResults(payload: String): List<PlantPrediction> {
        val root = JSONObject(payload)
        val results = root.optJSONArray("results") ?: return emptyList()
        return (0 until results.length()).mapNotNull { index ->
            val item = results.optJSONObject(index) ?: return@mapNotNull null
            val species = item.optJSONObject("species") ?: return@mapNotNull null
            val scientific = species.optString("scientificNameWithoutAuthor")
                .ifBlank { species.optString("scientificName") }
                .trim()
            val names = species.optJSONArray("commonNames")
            val common = names?.let { array ->
                (0 until array.length()).asSequence().map { array.optString(it).trim() }
                    .firstOrNull { it.isNotBlank() }
            }.orEmpty()
            val label = when {
                common.isNotBlank() && scientific.isNotBlank() -> "$common ($scientific)"
                scientific.isNotBlank() -> scientific
                common.isNotBlank() -> common
                else -> return@mapNotNull null
            }
            val score = item.optDouble("score", Double.NaN).toFloat()
            if (!score.isFinite() || score !in 0f..1f) null
            else PlantPrediction(label, score, "Pl@ntNet")
        }.sortedByDescending { it.confidence }.take(3)
    }

    fun identify(bitmap: Bitmap, apiKey: String): List<PlantPrediction> {
        require(apiKey.isNotBlank()) { "A Pl@ntNet API key is required." }
        val photo = File.createTempFile("plantnet_", ".jpg")
        try {
            photo.outputStream().use { output ->
                check(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)) { "Could not prepare the plant photo." }
            }
            val boundary = "KisanKaDost-${UUID.randomUUID()}"
            val encodedKey = URLEncoder.encode(apiKey.trim(), "UTF-8")
            val connection = URL("https://my-api.plantnet.org/v2/identify/all?api-key=$encodedKey").openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.connectTimeout = 15000
            connection.readTimeout = 30000
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            connection.setRequestProperty("Accept", "application/json")
            try {
                connection.outputStream.use { output ->
                    fun write(value: String) = output.write(value.toByteArray(Charsets.UTF_8))
                    write("--$boundary\r\n")
                    write("Content-Disposition: form-data; name=\"organs\"\r\n\r\nleaf\r\n")
                    write("--$boundary\r\n")
                    write("Content-Disposition: form-data; name=\"images\"; filename=\"leaf.jpg\"\r\n")
                    write("Content-Type: image/jpeg\r\n\r\n")
                    photo.inputStream().use { it.copyTo(output) }
                    write("\r\n--$boundary--\r\n")
                }
                val status = connection.responseCode
                val stream = if (status in 200..299) connection.inputStream else connection.errorStream
                val body = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
                if (status !in 200..299) throw IllegalStateException("Pl@ntNet service returned HTTP $status.")
                return parseResults(body)
            } finally {
                connection.disconnect()
            }
        } finally {
            photo.delete()
        }
    }
}
