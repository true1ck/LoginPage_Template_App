package com.example.livingai_lg.ui.screens.auth

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.api.AuthApiClient
import com.example.livingai_lg.api.AuthManager
import com.example.livingai_lg.api.TokenManager
import com.example.livingai_lg.ui.components.backgrounds.DecorativeBackground
import com.example.livingai_lg.ui.components.PhoneNumberInput

import com.example.livingai_lg.ui.theme.FarmTextLight
import com.example.livingai_lg.ui.theme.FarmTextLink
import com.example.livingai_lg.ui.theme.FarmTextNormal

import com.example.livingai_lg.ui.components.FarmHeader
import com.example.livingai_lg.ui.utils.isKeyboardOpen
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    onSignUpClick: () -> Unit = {},
    onSignInClick: (phone: String, name: String) -> Unit = {},
) {
    val phoneNumber = remember { mutableStateOf("") }
    val isValid = phoneNumber.value.length == 10
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val authManager = remember { AuthManager(context, AuthApiClient(context), TokenManager(context)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmTextLight)
    ) {
        DecorativeBackground()

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp)
        ) {
            val horizontalPadding = maxWidth * 0.06f  // proportional padding
            val keyboardOpen = isKeyboardOpen()
            val topPadding by animateDpAsState(
                targetValue = if (keyboardOpen) 40.dp else 140.dp,
                animationSpec = tween(280, easing = FastOutSlowInEasing)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding)
                    .padding( top = 40.dp),//topPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (keyboardOpen) Arrangement.Top else Arrangement.Center
            ) {
                //Spacer(Modifier.height(if (keyboardOpen) 32.dp else 56.dp))
                // Header
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    FarmHeader()
                }


                Spacer(modifier = Modifier.height(48.dp))

                PhoneNumberInput(
                    phone = phoneNumber.value,
                    onChange = { phoneNumber.value = it }
                )

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = {
                        val fullPhoneNumber = "+91${phoneNumber.value}"
                        scope.launch {
                            authManager.requestOtp(fullPhoneNumber)
                                .onSuccess {
                                    onSignInClick(fullPhoneNumber,"existing_user")
                                }
                                .onFailure {
                                    Toast.makeText(context, "Failed to send OTP: ${it.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                              },
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isValid) Color(0xFFE17100) else Color(0xFFE17100).copy(
                            alpha = 0.4f
                        ),
                        contentColor = Color.White
                    )
                ) {
                    Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Don't have an account? ", fontSize = 16.sp, color = FarmTextNormal)
                    TextButton(onClick = onSignUpClick) {
                        Text("Sign up", fontSize = 16.sp, color = FarmTextLink)
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
