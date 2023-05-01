package com.example.plantidentificationapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File

// Encoding image file to string Base64
fun encodeImageBase64(photoFile: File): String {
    return Base64.encodeToString(photoFile.readBytes(), Base64.NO_WRAP)
}

fun downscaleBitmap(image : Bitmap, maxLength : Int) : Bitmap {
    try {
        if (image.height >= image.width) {
            if (image.height <= maxLength) { // If image is alredy good enough size
                return image
            }

            val aspectRatio = image.width.toDouble() / image.height.toDouble()
            val targetWidth = (maxLength * aspectRatio).toInt()
            val image = Bitmap.createScaledBitmap(image, targetWidth, maxLength, false)
            return image
        } else {
            if (image.width <= maxLength) {  // If image is alredy good enough size
                return image
            }

            val aspectRatio = image.height.toDouble() / image.width.toDouble()
            val targetHeight = (maxLength * aspectRatio).toInt()
            val image = Bitmap.createScaledBitmap(image, maxLength, targetHeight, false)
            return image
        }
    } catch (e: Exception) {
        return image
    }
}

fun convertBitmapToByteArray (bitmap : Bitmap) : ByteArray {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
    return baos.toByteArray()
}

fun convertByteArrayToBitmap (byteArray: ByteArray) : Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}


