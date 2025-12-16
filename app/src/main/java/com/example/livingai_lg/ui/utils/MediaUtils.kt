package com.example.livingai_lg.ui.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.example.livingai_lg.ui.models.MediaType

fun createMediaUri(
    context: Context,
    type: MediaType
): Uri {
    val directory = when (type) {
        MediaType.PHOTO -> File(context.cacheDir, "images")
        MediaType.VIDEO -> File(context.cacheDir, "videos")
    }

    if (!directory.exists()) {
        directory.mkdirs()
    }

    val fileName = when (type) {
        MediaType.PHOTO -> "${UUID.randomUUID()}.jpg"
        MediaType.VIDEO -> "${UUID.randomUUID()}.mp4"
    }

    val file = File(directory, fileName)

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}


fun getVideoThumbnail(
    context: Context,
    uri: Uri
): Bitmap? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        retriever.getFrameAtTime(0)
    } catch (e: Exception) {
        null
    }
}
