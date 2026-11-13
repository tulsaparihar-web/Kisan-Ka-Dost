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
    private fun directory(context: Context) = File(context.filesDir, "diary_photos").apply { mkdirs() }

    fun copyFromUri(context: Context, uri: Uri): String? = runCatching {
        val resolver = context.contentResolver
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        (resolver.openInputStream(uri) ?: error("Cannot read selected image")).use {
            BitmapFactory.decodeStream(it, null, bounds)
        }
        require(bounds.outWidth > 0 && bounds.outHeight > 0) { "Unsupported image" }
        val sample = calculateSampleSize(bounds.outWidth, bounds.outHeight, 1600)
        val bitmap = (resolver.openInputStream(uri) ?: error("Cannot decode selected image")).use {
            BitmapFactory.decodeStream(it, null, BitmapFactory.Options().apply { inSampleSize = sample })
        } ?: error("Unsupported image")
        try {
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
            val corrected = if (orientation == ExifInterface.ORIENTATION_NORMAL || orientation == ExifInterface.ORIENTATION_UNDEFINED) bitmap
                else Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            try {
                val name = "diary_${UUID.randomUUID()}.jpg"
                File(directory(context), name).outputStream().use { output ->
                    check(corrected.compress(Bitmap.CompressFormat.JPEG, 82, output)) { "Could not save photo" }
                }
                name
            } finally {
                if (corrected !== bitmap) corrected.recycle()
            }
        } finally {
            bitmap.recycle()
        }
    }.getOrNull()

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
        var sample = 1L
        while (width.toLong() / sample > maxDimension || height.toLong() / sample > maxDimension) {
            sample *= 2L
        }
        return sample.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
    }
}
