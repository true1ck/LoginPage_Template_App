package com.example.livingai_lg.ui.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class NewListingFormState {

    val name = mutableStateOf("")
    val animal = mutableStateOf("")
    val breed = mutableStateOf("")
    val age = mutableStateOf("")
    val milkYield = mutableStateOf("")
    val calvingNumber = mutableStateOf("")
    val reproductiveStatus = mutableStateOf("")
    val description = mutableStateOf("")

    val animalExpanded = mutableStateOf(false)
    val breedExpanded = mutableStateOf(false)

    val mediaUploads = mutableStateListOf(
        MediaUpload("left-view", "Left View", MediaType.PHOTO),
        MediaUpload("right-view", "Right View", MediaType.PHOTO),
        MediaUpload("left-angle", "Left Angle View", MediaType.PHOTO),
        MediaUpload("right-angle", "Right Angle View", MediaType.PHOTO),
        MediaUpload("angle", "Angle View", MediaType.PHOTO),
        MediaUpload("video", "", MediaType.VIDEO)
    )
}
