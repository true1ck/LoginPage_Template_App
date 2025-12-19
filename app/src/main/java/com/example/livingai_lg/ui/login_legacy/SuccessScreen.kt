package com.example.livingai_lg.ui.login_legacy

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.livingai_lg.ui.MainViewModel
import com.example.livingai_lg.ui.UserState
import com.example.livingai_lg.ui.MainViewModelFactory // <-- This was the missing import

@Composable
fun SuccessScreen(mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(LocalContext.current))) {

    val userState by mainViewModel.userState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(
//                brush = Brush.linearGradient(
//                    colors = listOf(LightCream, LighterCream, LightestGreen)
//                )
//            )
        ,
        contentAlignment = Alignment.Center
    ) {
        when (val state = userState) {
            is UserState.Loading -> {
                CircularProgressIndicator()
            }
            is UserState.Success -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Welcome, ${state.userDetails.name}!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Phone: ${state.userDetails.phoneNumber}")
                    Text("Profile Type: ${state.userDetails.userType}")
                    Text("Last Login: ${state.userDetails.lastLoginAt}")
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { showLogoutDialog = true }) {
                        Text("Logout")
                    }
                }
            }
            is UserState.Error -> {
                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
            }
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Confirm Logout") },
                text = { Text("Are you sure you want to log out?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            mainViewModel.logout()
                        }
                    ) {
                        Text("Logout")
                    }
                },
                dismissButton = {
                    Button(onClick = { showLogoutDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
