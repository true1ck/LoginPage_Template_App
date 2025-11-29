package com.example.livingai_lg.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.livingai_lg.api.AuthApiClient
import com.example.livingai_lg.api.AuthManager
import com.example.livingai_lg.api.TokenManager
import com.example.livingai_lg.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun OtpScreen(navController: NavController, phoneNumber: String, name: String) {
    val otp = remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val authManager = remember { AuthManager(context, AuthApiClient("http://10.0.2.2:3000"), TokenManager(context)) }

    // Flag to determine if this is a sign-in flow for an existing user.
    val isSignInFlow = name == "existing_user"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(LightCream, LighterCream, LightestGreen)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(200.dp))

            Text("Enter OTP", fontSize = 24.sp, fontWeight = FontWeight.Medium, color = Color(0xFF364153))

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = otp.value,
                onValueChange = { if (it.length <= 6) otp.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                    disabledContainerColor = Color.White.copy(alpha = 0.9f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 24.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    scope.launch {
                        authManager.login(phoneNumber, otp.value)
                            .onSuccess { response ->
                                if (isSignInFlow) {
                                    // For existing users, always go to the success screen.
                                    navController.navigate("success") { popUpTo("login") { inclusive = true } }
                                } else {
                                    // For new users, check if a profile needs to be created.
                                    if (response.needs_profile) {
                                        navController.navigate("create_profile/$name")
                                    } else {
                                        navController.navigate("success") { popUpTo("login") { inclusive = true } }
                                    }
                                }
                            }
                            .onFailure {
                                Toast.makeText(context, "Invalid or expired OTP", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE9A00)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text("Continue", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OtpScreenPreview() {
    LivingAi_LgTheme {
        OtpScreen(rememberNavController(), "+919876543210", "John Doe")
    }
}
