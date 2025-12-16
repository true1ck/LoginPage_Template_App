package com.example.livingai_lg.ui.models

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MediaUpload(
    val id: String,
    val label: String,
    val type: MediaType,
    initialUri: Uri? = null
) {
    var uri by mutableStateOf(initialUri)
}

enum class MediaType {
    PHOTO, VIDEO
}