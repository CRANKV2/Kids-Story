@file:Suppress("SpellCheckingInspection")

package com.gigo.kidsstorys.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

object ImageUtils {
    private const val MAX_WIDTH = 1920
    private const val MAX_HEIGHT = 1080
    private const val JPEG_QUALITY = 85
    private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024 // 5MB

    fun processAndSaveImage(context: Context, uri: Uri, outputFile: File): Boolean {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val originalBitmap = BitmapFactory.decodeStream(inputStream)

                // Behalte die originale Größe bei
                val maxHeight = 1920 // Maximale Höhe, aber groß genug
                val maxWidth = 1080  // Maximale Breite, aber groß genug

                val scaleFactor = when {
                    originalBitmap.height > maxHeight || originalBitmap.width > maxWidth -> {
                        val heightScale = maxHeight.toFloat() / originalBitmap.height
                        val widthScale = maxWidth.toFloat() / originalBitmap.width
                        min(heightScale, widthScale)
                    }
                    else -> 1f
                }

                val finalBitmap = Bitmap.createScaledBitmap(
                    originalBitmap,
                    (originalBitmap.width * scaleFactor).toInt(),
                    (originalBitmap.height * scaleFactor).toInt(),
                    true
                )

                FileOutputStream(outputFile).use { out ->
                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
                true
            } ?: false
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