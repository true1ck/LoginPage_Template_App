package com.example.livingai_lg.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.api.AuthApiClient
import com.example.livingai_lg.api.AuthManager
import com.example.livingai_lg.api.SignupRequest
import com.example.livingai_lg.api.TokenManager
import com.example.livingai_lg.ui.components.backgrounds.DecorativeBackground
import kotlin.math.min
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.launch
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent


@Composable
fun OtpScreen(
    phoneNumber: String,
    name: String,
    mainViewModel: com.example.livingai_lg.ui.MainViewModel,
    onSuccess: () -> Unit = {},
    onCreateProfile: (name: String) -> Unit = {},
    onLanding: () -> Unit = {},  // New callback for navigating to landing page
    // Optional signup data for signup flow
    signupState: String? = null,
    signupDistrict: String? = null,
    signupVillage: String? = null,
) {
    val otp = remember { mutableStateOf("") }
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val authManager = remember { AuthManager(context, AuthApiClient(context), TokenManager(context)) }

    // Flag to determine if this is a sign-in flow for an existing user.
    val isSignInFlow = name == "existing_user"
    // Flag to determine if this is a signup flow (has signup data)
    val isSignupFlow = !isSignInFlow && (signupState != null || signupDistrict != null || signupVillage != null)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFFFFBEA),
                        Color(0xFFFDFBE8),
                        Color(0xFFF7FEE7)
                    )
                )
            )
    ) {
        DecorativeBackground()

        val screenW = maxWidth.value
        val screenH = maxHeight.value

        // Figma design reference size from Flutter widget
        val designW = 393.39f
        val designH = 852.53f

        val scale = min(screenW / designW, screenH / designH)

        fun s(v: Float) = (v * scale).dp        // dp scaling
        fun fs(v: Float) = (v * scale).sp        // font scaling
Column(
    Modifier.fillMaxSize().padding(horizontal = 12.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    // ---------------------------
    // "Enter OTP" Title
    // ---------------------------
    Text(
        text = "Enter OTP",
        color = Color(0xFF927B5E),
        fontSize = fs(20f),
        fontWeight = FontWeight.Medium,
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        textAlign = TextAlign.Center,
        style = LocalTextStyle.current.copy(
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.25f),
                offset = Offset(0f, s(4f).value),
                blurRadius = s(4f).value
            )
        )
    )

    // ---------------------------
    // OTP 4-Box Input Row
    // ---------------------------
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(s(17f))
    ) {
        OtpInputRow(
            otpLength = 6,
            scale = scale,
            otp = otp.value,
            onOtpChange = { if (it.length <= 6) otp.value = it }
        )
    }
    // ---------------------------
    // Continue Button
    // ---------------------------
    Box(
        modifier = Modifier
            .fillMaxWidth().padding(vertical = 16.dp, horizontal = 48.dp)
            .size(s(279.25f), s(55.99f))
            .shadow(
                elevation = s(6f),
                ambientColor = Color.Black.copy(alpha = 0.10f),
                shape = RoundedCornerShape(s(16f)),
            )
            .shadow(
                elevation = s(15f),
                ambientColor = Color.Black.copy(alpha = 0.10f),
                shape = RoundedCornerShape(s(16f)),
            )
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFFD9900), Color(0xFFE17100))
                ),
                shape = RoundedCornerShape(s(16f))
            )
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    scope.launch {
                        if (isSignupFlow) {
                            // For signup flow: Verify OTP first, then call signup API to update user with name/location
                            android.util.Log.d("OTPScreen", "Signup flow: Verifying OTP...")
                            authManager.login(phoneNumber, otp.value)
                                .onSuccess { verifyResponse ->
                                    android.util.Log.d("OTPScreen", "OTP verified successfully. Calling signup API...")
                                    // OTP verified successfully - user is now logged in
                                    // Refresh auth status in MainViewModel so AppNavigation knows user is authenticated
                                    mainViewModel.refreshAuthStatus()
                                    // Now call signup API to update user with name and location
                                    val signupRequest = SignupRequest(
                                        name = name,
                                        phoneNumber = phoneNumber,
                                        state = signupState,
                                        district = signupDistrict,
                                        cityVillage = signupVillage
                                    )
                                    authManager.signup(signupRequest)
                                        .onSuccess { signupResponse ->
                                            android.util.Log.d("OTPScreen", "Signup API response: success=${signupResponse.success}, userExists=${signupResponse.userExists}, needsProfile=${signupResponse.needsProfile}, isNewAccount=${signupResponse.isNewAccount}")
                                            // Check if this is a new user account
                                            val isNewUser = signupResponse.isNewAccount == true
                                            
                                            if (isNewUser) {
                                                // New user - navigate to landing page
                                                android.util.Log.d("OTPScreen", "New user detected - navigating to landing page")
                                                try {
                                                    onLanding()
                                                } catch (e: Exception) {
                                                    android.util.Log.e("OTPScreen", "Navigation error: ${e.message}", e)
                                                    Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
                                                }
                                            } else {
                                                // Existing user or signup update - use existing logic
                                                if (signupResponse.success || signupResponse.userExists == true) {
                                                    // Success - user is created/updated and logged in
                                                    // Check if profile needs completion
                                                    val needsProfile = signupResponse.needsProfile == true || verifyResponse.needsProfile
                                                    android.util.Log.d("OTPScreen", "Signup successful. needsProfile=$needsProfile, navigating...")
                                                    try {
                                                        if (needsProfile) {
                                                            android.util.Log.d("OTPScreen", "Navigating to create profile screen with name: $name")
                                                            onCreateProfile(name)
                                                        } else {
                                                            android.util.Log.d("OTPScreen", "Navigating to success screen")
                                                            onSuccess()
                                                        }
                                                    } catch (e: Exception) {
                                                        android.util.Log.e("OTPScreen", "Navigation error: ${e.message}", e)
                                                        Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
                                                    }
                                                } else {
                                                    // Signup failed but OTP was verified - user is logged in
                                                    // Navigate to success anyway since verify-otp succeeded
                                                    android.util.Log.d("OTPScreen", "Signup API returned false, but OTP verified. Navigating anyway...")
                                                    val needsProfile = verifyResponse.needsProfile
                                                    try {
                                                        if (needsProfile) {
                                                            android.util.Log.d("OTPScreen", "Navigating to create profile screen with name: $name")
                                                            onCreateProfile(name)
                                                        } else {
                                                            android.util.Log.d("OTPScreen", "Navigating to success screen")
                                                            onSuccess()
                                                        }
                                                    } catch (e: Exception) {
                                                        android.util.Log.e("OTPScreen", "Navigation error: ${e.message}", e)
                                                        Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
                                                    }
                                                    // Show warning if signup update failed
                                                    val errorMsg = signupResponse.message
                                                    if (errorMsg != null) {
                                                        Toast.makeText(context, "Profile update: $errorMsg", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        }
                                        .onFailure { signupError ->
                                            android.util.Log.e("OTPScreen", "Signup API failed: ${signupError.message}", signupError)
                                            // Signup API failed but OTP was verified - user is logged in
                                            // For new users, navigate to landing page
                                            // For existing users, use existing logic
                                            val needsProfile = verifyResponse.needsProfile
                                            android.util.Log.d("OTPScreen", "Navigating despite signup failure. needsProfile=$needsProfile")
                                            try {
                                                // If this is a signup flow and signup failed, treat as new user
                                                if (isSignupFlow) {
                                                    android.util.Log.d("OTPScreen", "Signup flow failed - navigating to landing page")
                                                    onLanding()
                                                } else if (needsProfile) {
                                                    android.util.Log.d("OTPScreen", "Navigating to create profile screen with name: $name")
                                                    onCreateProfile(name)
                                                } else {
                                                    android.util.Log.d("OTPScreen", "Navigating to success screen")
                                                    onSuccess()
                                                }
                                            } catch (e: Exception) {
                                                android.util.Log.e("OTPScreen", "Navigation error: ${e.message}", e)
                                                Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
                                            }
                                            // Show warning about signup failure
                                            val errorMsg = signupError.message ?: "Profile update failed"
                                            Toast.makeText(context, "Warning: $errorMsg", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .onFailure { error ->
                                    android.util.Log.e("OTPScreen", "OTP verification failed: ${error.message}", error)
                                    Toast.makeText(
                                        context,
                                        "Invalid or expired OTP",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            // For sign-in flow: Just verify OTP and login
                            authManager.login(phoneNumber, otp.value)
                                .onSuccess { response ->
                                    android.util.Log.d("OTPScreen", "Sign-in OTP verified. needsProfile=${response.needsProfile}")
                                    // Refresh auth status in MainViewModel so AppNavigation knows user is authenticated
                                    mainViewModel.refreshAuthStatus()
                                    try {
                                        if (isSignInFlow) {
                                            // For existing users, always go to the success screen.
                                            android.util.Log.d("OTPScreen", "Existing user - navigating to success")
                                            onSuccess()
                                        } else {
                                            // For new users, check if a profile needs to be created.
                                            if (response.needsProfile) {
                                                android.util.Log.d("OTPScreen", "New user needs profile - navigating to create profile with name: $name")
                                                onCreateProfile(name)
                                            } else {
                                                android.util.Log.d("OTPScreen", "New user - navigating to success")
                                                onSuccess()
                                            }
                                        }
                                    } catch (e: Exception) {
                                        android.util.Log.e("OTPScreen", "Navigation error: ${e.message}", e)
                                        Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                                .onFailure { error ->
                                    android.util.Log.e("OTPScreen", "OTP verification failed: ${error.message}", error)
                                    Toast.makeText(
                                        context,
                                        "Invalid or expired OTP",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Continue",
            color = Color.White,
            fontSize = fs(16f),
            fontWeight = FontWeight.Medium
        )
    }
}
    }
}

@Composable
fun OtpInputRow(
    otpLength: Int,
    scale: Float,
    otp: String,
    onOtpChange: (String) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val maxRowWidth = maxWidth

        val spacing = (12f * scale).dp
        val totalSpacing = spacing * (otpLength - 1)

        val boxWidth = ((maxRowWidth - totalSpacing) / otpLength)
            .coerceAtMost((66f * scale).dp)

        val focusRequesters = remember {
            List(otpLength) { FocusRequester() }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(otpLength) { index ->
                OtpBox(
                    index = index,
                    otp = otp,
                    scale = scale,
                    width = boxWidth, // 👈 fixed width
                    focusRequester = focusRequesters[index],
                    onRequestFocus = {
                        val firstEmpty = otp.length.coerceAtMost(otpLength - 1)
                        focusRequesters[firstEmpty].requestFocus()
                    },
                    onNextFocus = {
                        if (index + 1 < otpLength) focusRequesters[index + 1].requestFocus()
                    },
                    onPrevFocus = {
                        if (index - 1 >= 0) focusRequesters[index - 1].requestFocus()
                    },
                    onChange = onOtpChange
                )
            }
        }
    }
}




@Composable
private fun OtpBox(
    index: Int,
    otp: String,
    scale: Float,
    width: Dp,
    focusRequester: FocusRequester,
    onRequestFocus: () -> Unit,
    onNextFocus: () -> Unit,
    onPrevFocus: () -> Unit,
    onChange: (String) -> Unit
) {
    val boxH = 52f * scale
    val radius = 16f * scale

    val char = otp.getOrNull(index)?.toString() ?: ""

    Box(
        modifier = Modifier
            .size(width, boxH.dp)
            .shadow((4f * scale).dp, RoundedCornerShape(radius.dp))
            .background(Color.White, RoundedCornerShape(radius.dp))
            .clickable { onRequestFocus() },
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = char,
            onValueChange = { new ->
                when {
                    // DIGIT ENTERED
                    new.matches(Regex("\\d")) -> {
                        val updated = otp.padEnd(index + 1, ' ').toMutableList()
                        updated[index] = new.first()
                        onChange(updated.joinToString("").trim())
                        onNextFocus()
                    }

                    // BACKSPACE WHEN CHARACTER EXISTS
                    new.isEmpty() && char.isNotEmpty() -> {
                        val updated = otp.toMutableList()
                        updated.removeAt(index)
                        onChange(updated.joinToString(""))
                    }
                }
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onPreviewKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown &&
                        event.key == Key.Backspace &&
                        char.isEmpty() &&
                        index > 0
                    ) {
                        val updated = otp.toMutableList()
                        updated.removeAt(index - 1)          // 👈 clear previous box
                        onChange(updated.joinToString(""))
                        onPrevFocus()
                        true
                    }
                    else {
                        false
                    }
                },
            textStyle = LocalTextStyle.current.copy(
                fontSize = (24f * scale).sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            singleLine = true
        )



    }
}




