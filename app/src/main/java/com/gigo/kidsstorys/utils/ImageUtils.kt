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
    private const val MAX_WIDTH = 1920
    private const val MAX_HEIGHT = 1080
    private const val JPEG_QUALITY = 85
    private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024 // 5MB

    fun processAndSaveImage(context: Context, uri: Uri): Boolean {
        return try {
            // Bild laden
            val bitmap = loadBitmapFromUri(context, uri)
            
            // Erste Skalierung basierend auf Dimensionen
            var optimizedBitmap = scaleBitmap(bitmap, MAX_WIDTH, MAX_HEIGHT)
            
            // Komprimierung mit Qualitätsanpassung
            var quality = JPEG_QUALITY
            var fileSize: Long
            val file = File(context.filesDir, "background_image.jpg")
            
            do {
                FileOutputStream(file).use { out ->
                    optimizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
                }
                fileSize = file.length()
                quality -= 5
            } while (fileSize > MAX_FILE_SIZE_BYTES && quality > 30)

            // Ursprüngliche Bitmaps freigeben
            if (bitmap != optimizedBitmap) {
                bitmap.recycle()
            }
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri)) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                decoder.isMutableRequired = true
            }
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
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