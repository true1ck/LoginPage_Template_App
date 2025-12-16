package com.example.livingai_lg.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.livingai_lg.ui.models.MediaType
import com.example.livingai_lg.ui.models.MediaUpload
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.example.livingai_lg.ui.utils.createMediaUri
import com.example.livingai_lg.ui.utils.getVideoThumbnail

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MediaPickerCard(
    upload: MediaUpload,
    modifier: Modifier = Modifier,
    onUriSelected: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    var showVideoPlayer by remember { mutableStateOf(false) }

    // Gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) onUriSelected(uri)
    }

    // Camera (photo)
    val imageCameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) onUriSelected(cameraUri) else cameraUri = null
    }

    // Camera (video)
    val videoCameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success) onUriSelected(cameraUri) else cameraUri = null
    }

    /* ---------- Picker Sheet (NO delete here anymore) ---------- */

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Media",
                    style = MaterialTheme.typography.titleMedium
                )

                ListItem(
                    headlineContent = { Text("Camera") },
                    leadingContent = { Icon(Icons.Outlined.CameraAlt, null) },
                    modifier = Modifier.clickable(
                        indication = LocalIndication.current,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        showSheet = false
                        cameraUri = createMediaUri(context, upload.type)

                        if (upload.type == MediaType.PHOTO) {
                            imageCameraLauncher.launch(cameraUri!!)
                        } else {
                            videoCameraLauncher.launch(cameraUri!!)
                        }
                    }
                )

                ListItem(
                    headlineContent = { Text("Gallery") },
                    leadingContent = {
                        Icon(
                            if (upload.type == MediaType.PHOTO)
                                Icons.Outlined.CameraAlt
                            else
                                Icons.Outlined.Videocam,
                            null
                        )
                    },
                    modifier = Modifier.clickable(
                        indication = LocalIndication.current,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        showSheet = false
                        galleryLauncher.launch(
                            if (upload.type == MediaType.PHOTO) "image/*" else "video/*"
                        )
                    }
                )
            }
        }
    }

    /* ---------- Card ---------- */

    Box(
        modifier = modifier
            .height(204.dp)
            .shadow(0.5.dp, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0x1A000000), RoundedCornerShape(8.dp))
            .combinedClickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    if (upload.uri != null && upload.type == MediaType.VIDEO) {
                        showVideoPlayer = true
                    } else {
                        showSheet = true
                    }
                },
                onLongClick = {
                    if (upload.uri != null) {
                        showSheet = true
                    }
                }
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {

        /* ---------- Media Preview ---------- */

        if (upload.uri != null) {

            Box(modifier = Modifier.fillMaxSize()) {

                if (upload.type == MediaType.VIDEO) {

                    val thumbnail = remember(upload.uri) {
                        getVideoThumbnail(context, upload.uri!!)
                    }

                    if (thumbnail != null) {
                        Image(
                            bitmap = thumbnail.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // ▶ Play overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(56.dp)
                            .background(
                                Color.Black.copy(alpha = 0.6f),
                                RoundedCornerShape(28.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Video",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                } else {
                    // Photo
                    AsyncImage(
                        model = upload.uri,
                        contentDescription = upload.label,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                /* ---------- ❌ Remove Button ---------- */

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove media",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(24.dp)
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onUriSelected(null)
                        }
                )
            }

        } else {
            /* ---------- Empty State ---------- */

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (upload.type == MediaType.PHOTO)
                        Icons.Outlined.CameraAlt
                    else
                        Icons.Outlined.Videocam,
                    contentDescription = null,
                    tint = Color(0xFF717182),
                    modifier = Modifier.size(32.dp)
                )

                Text(
                    text = if (upload.type == MediaType.PHOTO) "Add Photo" else "Add Video",
                    color = Color(0xFF717182)
                )

                if (upload.label.isNotEmpty()) {
                    Text(upload.label, color = Color(0xFF717182))
                }
            }
        }

        /* ---------- Video Player ---------- */

        if (showVideoPlayer && upload.uri != null) {
            VideoPlayerDialog(
                uri = upload.uri!!,
                onDismiss = { showVideoPlayer = false }
            )
        }
    }
}

