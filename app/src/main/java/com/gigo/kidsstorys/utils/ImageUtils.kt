package com.gigo.kidsstorys.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    fun processAndSaveImage(context: Context, uri: Uri): Boolean {
        return try {
            // Bild laden
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }

            // Bild auf maximale Größe skalieren (z.B. 1920x1080)
            val scaledBitmap = scaleBitmap(bitmap, 1920, 1080)

            // Bild im internen Speicher speichern
            val file = File(context.filesDir, "background_image.jpg")
            FileOutputStream(file).use { out ->
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }

            // Ursprüngliche Bitmaps freigeben
            bitmap.recycle()
            if (bitmap != scaledBitmap) {
                scaledBitmap.recycle()
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun scaleBitmap(source: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val ratio = minOf(
            maxWidth.toFloat() / source.width,
            maxHeight.toFloat() / source.height
        )

        return if (ratio < 1) {
            Bitmap.createScaledBitmap(
                source,
                (source.width * ratio).toInt(),
                (source.height * ratio).toInt(),
                true
            )
        } else {
            source
        }
    }
} 