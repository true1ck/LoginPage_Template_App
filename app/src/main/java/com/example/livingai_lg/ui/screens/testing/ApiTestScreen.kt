package com.example.livingai_lg.ui.screens.testing


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiTestScreen(
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    // Default API endpoint
    var apiEndpoint by remember {
        mutableStateOf("https://jsonplaceholder.typicode.com/posts/1")
    }

    var isLoading by remember { mutableStateOf(false) }
    var responseText by remember { mutableStateOf<String?>(null) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val client = remember { OkHttpClient() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "API Test",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF7F4EE)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // API endpoint input
            OutlinedTextField(
                value = apiEndpoint,
                onValueChange = { apiEndpoint = it },
                label = { Text("API Endpoint") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Call API button
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        responseText = null
                        errorText = null

                        try {
                            val result = withContext(Dispatchers.IO) {
                                val request = Request.Builder()
                                    .url(apiEndpoint)
                                    .get()
                                    .build()

                                client.newCall(request).execute().use { response ->
                                    val body = response.body?.string()
                                    Pair(response.code, body)
                                }
                            }

                            val (code, body) = result
                            responseText = "Status: $code\n\n$body"
                        } catch (e: Exception) {
                            e.printStackTrace()
                            errorText = e.toString()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Calling API..." else "Call API")
            }

            // Result display
            when {
                responseText != null -> {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = responseText ?: "",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp
                        )
                    }
                }

                errorText != null -> {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFEBEE),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorText ?: "",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp,
                            color = Color(0xFFE53935)
                        )
                    }
                }
            }
        }
    }
}
