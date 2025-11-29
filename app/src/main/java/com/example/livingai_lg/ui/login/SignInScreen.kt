package com.example.livingai_lg.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
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
fun SignInScreen(navController: NavController) {
    val phoneNumber = remember { mutableStateOf("") }
    val isPhoneNumberValid = remember(phoneNumber.value) { phoneNumber.value.length == 10 && phoneNumber.value.all { it.isDigit() } }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val authManager = remember { AuthManager(context, AuthApiClient("http://10.0.2.2:3000"), TokenManager(context)) }

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
            Spacer(modifier = Modifier.height(140.dp))

            Row {
                Text("Farm", fontSize = 32.sp, fontWeight = FontWeight.Medium, color = Color(0xFFE17100))
                Text("Market", fontSize = 32.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            }
            Text("Welcome back!", fontSize = 16.sp, color = Color(0xFF4A5565))

            Spacer(modifier = Modifier.height(128.dp))

            Text(
                text = "Enter Phone Number",
                fontSize = 16.sp,
                color = Color(0xFF364153),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start).padding(start = 21.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .width(65.dp)
                        .height(52.dp)
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
                        .background(color = Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(16.dp))
                        .border(width = 1.dp, color = Color.Black.copy(alpha = 0.07f), shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+91", fontSize = 16.sp, color = Color(0xFF0A0A0A))
                }
                Spacer(modifier = Modifier.width(6.dp))
                TextField(
                    value = phoneNumber.value,
                    onValueChange = { phoneNumber.value = it },
                    placeholder = { Text("Enter your Phone Number", color = Color(0xFF99A1AF)) },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF99A1AF)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                        disabledContainerColor = Color.White.copy(alpha = 0.9f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    isError = phoneNumber.value.isNotEmpty() && !isPhoneNumberValid,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(52.dp).shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp)),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }
            if (phoneNumber.value.isNotEmpty() && !isPhoneNumberValid) {
                Text(
                    text = "Please enter a valid 10-digit phone number",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val fullPhoneNumber = "+91${phoneNumber.value}"
                    scope.launch {
                        authManager.requestOtp(fullPhoneNumber)
                            .onSuccess {
                                // For existing user, name is not needed, so we pass a placeholder
                                navController.navigate("otp/$fullPhoneNumber/existing_user")
                            }
                            .onFailure {
                                Toast.makeText(context, "Failed to send OTP: ${it.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                },
                enabled = isPhoneNumberValid,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFE9A00),
                    disabledContainerColor = Color(0xFFF8DDA7),
                    contentColor = Color.White,
                    disabledContentColor = Color.White.copy(alpha = 0.7f)
                ),
                modifier = Modifier.fillMaxWidth().height(56.dp).shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    LivingAi_LgTheme {
        SignInScreen(rememberNavController())
    }
}
