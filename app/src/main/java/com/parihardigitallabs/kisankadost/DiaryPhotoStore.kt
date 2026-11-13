package com.parihardigitallabs.kisankadost

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.util.UUID

object DiaryPhotoStore {
    private const val MAX_DIMENSION = 1600
    private const val MAX_SOURCE_DIMENSION = 30000
    private const val MAX_SOURCE_PIXELS = 100_000_000L

    private fun directory(context: Context) = File(context.filesDir, "diary_photos").apply { mkdirs() }

    fun copyFromUri(context: Context, uri: Uri): String? {
        var decoded: Bitmap? = null
        var destination: File? = null
        var outputBitmap: Bitmap? = null
        return try {
            val resolver = context.contentResolver
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            val boundsStream = resolver.openInputStream(uri) ?: error("Cannot read selected image")
            boundsStream.use { BitmapFactory.decodeStream(it, null, bounds) }
            require(bounds.outWidth in 1..MAX_SOURCE_DIMENSION && bounds.outHeight in 1..MAX_SOURCE_DIMENSION) {
                "Image dimensions are not supported"
            }
            require(bounds.outWidth.toLong() * bounds.outHeight.toLong() <= MAX_SOURCE_PIXELS) {
                "Image is too large to process safely"
            }

            val sample = calculateSampleSize(bounds.outWidth, bounds.outHeight, MAX_DIMENSION)
            decoded = resolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, BitmapFactory.Options().apply { inSampleSize = sample })
            } ?: error("Cannot decode selected image")
            require(decoded != null && decoded.width > 0 && decoded.height > 0) { "Unsupported image" }

            val orientation = resolver.openInputStream(uri)?.use {
                ExifInterface(it).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            } ?: ExifInterface.ORIENTATION_NORMAL
            val matrix = Matrix().apply {
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> postRotate(90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> postRotate(180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> postRotate(270f)
                    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> postScale(-1f, 1f)
                    ExifInterface.ORIENTATION_FLIP_VERTICAL -> postScale(1f, -1f)
                    ExifInterface.ORIENTATION_TRANSPOSE -> { postScale(-1f, 1f); postRotate(90f) }
                    ExifInterface.ORIENTATION_TRANSVERSE -> { postScale(-1f, 1f); postRotate(270f) }
                }
            }
            outputBitmap = if (orientation == ExifInterface.ORIENTATION_NORMAL ||
                orientation == ExifInterface.ORIENTATION_UNDEFINED
            ) decoded else Bitmap.createBitmap(decoded!!, 0, 0, decoded!!.width, decoded!!.height, matrix, true)

            val name = "diary_${UUID.randomUUID()}.jpg"
            destination = File(directory(context), name)
            destination!!.outputStream().buffered().use { output ->
                check(outputBitmap!!.compress(Bitmap.CompressFormat.JPEG, 82, output)) { "Could not save photo" }
            }
            name
        } catch (_: Exception) {
            destination?.delete()
            null
        } finally {
            if (outputBitmap != null && outputBitmap !== decoded) outputBitmap?.recycle()
            decoded?.recycle()
        }
    }

    fun isValidStoredPhoto(file: File): Boolean {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.absolutePath, bounds)
        return bounds.outWidth in 1..MAX_DIMENSION && bounds.outHeight in 1..MAX_DIMENSION
    }

    fun pruneUnused(context: Context, referencedNames: Set<String>) {
        directory(context).listFiles()?.forEach { file ->
            if (isSafeName(file.name) && file.name !in referencedNames) file.delete()
        }
    }

    fun delete(context: Context, fileName: String) {
        if (isSafeName(fileName)) File(directory(context), fileName).delete()
    }

    fun file(context: Context, fileName: String): File? =
        if (isSafeName(fileName)) File(directory(context), fileName).takeIf { it.isFile } else null

    fun isSafeName(name: String): Boolean =
        name.isNotBlank() && name.length <= 120 && name.matches(Regex("[A-Za-z0-9._-]+")) && !name.contains("..")

    fun calculateSampleSize(width: Int, height: Int, maxDimension: Int): Int {
        require(width > 0 && height > 0 && maxDimension > 0) { "Image dimensions must be positive" }
        var sample = 1
        while (width / sample > maxDimension || height / sample > maxDimension) sample *= 2
        return sample
    }
}
