package com.example.livingai_lg.ui.screens.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.livingai_lg.api.UserNotFoundException
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay


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
    val otpLength = 6
    val otp = remember { mutableStateOf(List<String>(otpLength) { "" }) }
    var focusedIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val authManager = remember { AuthManager(context, AuthApiClient(context), TokenManager(context)) }

    fun updateOtpAt(index: Int, value: String) {
        otp.value = otp.value.mapIndexed { i, old ->
            if (i == index) value else old
        }
    }

    // Countdown timer state (1 minutes = 60 seconds)
    var countdownSeconds by remember { mutableStateOf(10) }
    var countdownKey by remember { mutableStateOf(0) }

    LaunchedEffect(countdownKey) {
        while (countdownSeconds > 0) {
            delay(1000) // Wait 1 second
            countdownSeconds--
        }
    }
    
    // Format countdown as MM:SS
    val minutes = countdownSeconds / 60
    val seconds = countdownSeconds % 60
    val countdownText = String.format("%02d:%02d", minutes, seconds)

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
        OTPInputTextFields(
            otpLength = otpLength,
            otpValues = otp.value,
            onUpdateOtpValuesByIndex = { index, value ->
                updateOtpAt(index,value)
            },
            onOtpInputComplete = {}
        )
    }
    
    // ---------------------------
    // Countdown Timer
    // ---------------------------
    if (countdownSeconds > 0) {
        Text(
            text = countdownText,
            color = Color(0xFF927B5E),
            fontSize = fs(16f),
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.15f),
                    offset = Offset(0f, s(2f).value),
                    blurRadius = s(2f).value
                )
            )
        )
    } else {
        Text(
            text = "Resend OTP",
            color = Color(0xFFE17100),
            fontSize = fs(16f),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clickable {
                    // 🔹 Stub for now
                    scope.launch {

                        authManager.requestOtp(phoneNumber)
                            .onSuccess {
                                // OTP sent successfully, navigate to OTP screen with signup data
                                // Pass signup form data through the callback
                                Toast.makeText(context, "Resent OTP",Toast.LENGTH_LONG).show()
                                countdownSeconds = 10
                                countdownKey++
                            }
                            .onFailure {
                                Toast.makeText(context, "Failed to send OTP: ${it.message}", Toast.LENGTH_LONG).show()
                                Log.e("OtpScreen", "Failed to send OTP ${it.message}", it)

                            }
                    }
                },
            textAlign = TextAlign.Center
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
                        val otpString = otp.value.joinToString("") { it?.toString() ?: "" }

                        if (otpString.length < otpLength) {
                            Toast.makeText(context, "Please enter full OTP", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        if (isSignupFlow) {
                            // For signup flow: Verify OTP first, then call signup API to update user with name/location
                            android.util.Log.d("OTPScreen", "Signup flow: Verifying OTP...")
                            authManager.login(phoneNumber, otpString)
                                .onSuccess { verifyResponse ->
                                    android.util.Log.d("OTPScreen", "OTP verified successfully. Calling signup API...")
                                    // OTP verified successfully - user is now logged in
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
                                                    // Refresh auth status - this will trigger navigation via AppNavigation's LaunchedEffect
//                                                    mainViewModel.refreshAuthStatus()
                                                    try {
                                                        if (needsProfile) {
                                                            android.util.Log.d("OTPScreen", "Navigating to create profile screen with name: $name")
                                                            onCreateProfile(name)
                                                        } else {
                                                            android.util.Log.d("OTPScreen", "Signup successful - auth state updated")
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
                                                    // Refresh auth status
                                                    mainViewModel.refreshAuthStatus()
                                                    try {
                                                        if (needsProfile) {
                                                            android.util.Log.d("OTPScreen", "Navigating to create profile screen with name: $name")
                                                            onCreateProfile(name)
                                                        } else {
                                                            // Don't manually navigate - let AppNavigation handle it
                                                            android.util.Log.d("OTPScreen", "Signup successful - auth state updated")
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
                                            // Refresh auth status
                                            mainViewModel.refreshAuthStatus()
                                            try {
                                                // If this is a signup flow and signup failed, treat as new user
                                                if (isSignupFlow) {
                                                    android.util.Log.d("OTPScreen", "Signup flow failed - navigating to landing page")
                                                    onLanding()
                                                } else if (needsProfile) {
                                                    android.util.Log.d("OTPScreen", "Navigating to create profile screen with name: $name")
                                                    onCreateProfile(name)
                                                } else {
                                                     android.util.Log.d("OTPScreen", "Signup successful - auth state updated")
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
                            authManager.login(phoneNumber, otpString)
                                .onSuccess { response ->
                                    android.util.Log.d("OTPScreen", "Sign-in OTP verified. needsProfile=${response.needsProfile}")
                                    // Tokens are now saved (synchronously via commit())
                                    // Refresh auth status - this will optimistically set authState to Authenticated
                                    // The LaunchedEffect in AppNavigation will automatically navigate to ChooseServiceScreen
                                    //mainViewModel.refreshAuthStatus()
                                    if(response.needsProfile) {
                                        onCreateProfile(name)
                                    } else {
                                        onSuccess()
                                    }
                                    android.util.Log.d("OTPScreen", "Auth status refreshed - navigation will happen automatically via LaunchedEffect")
                                }
                                .onFailure { error ->
                                    android.util.Log.e("OTPScreen", "OTP verification failed: ${error.message}", error)
                                    
                                    // Check if user not found - redirect to signup
                                    if (error is UserNotFoundException && error.errorCode == "USER_NOT_FOUND") {
                                        android.util.Log.d("OTPScreen", "User not found - redirecting to signup")
                                        Toast.makeText(
                                            context,
                                            error.message ?: "Account not found. Please sign up to create a new account.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        // Navigate back to landing page so user can choose signup
                                        try {
                                            onLanding()
                                        } catch (e: Exception) {
                                            android.util.Log.e("OTPScreen", "Navigation error: ${e.message}", e)
                                        }
                                    } else {
                                        // Other errors (invalid OTP, expired, etc.)
                                        Toast.makeText(
                                            context,
                                            error.message ?: "Invalid or expired OTP",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
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
fun OTPInputTextFields(
    otpLength: Int,
    onUpdateOtpValuesByIndex: (Int, String) -> Unit,
    onOtpInputComplete: () -> Unit,
    modifier: Modifier = Modifier,
    otpValues: List<String> = List(otpLength) { "" }, // Pass this as default for future reference
    isError: Boolean = false,
) {
    val focusRequesters = List(otpLength) { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
//        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
    ) {
        otpValues.forEachIndexed { index, value ->
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
//                    .width(64.dp)
                    .padding(6.dp)
                    .focusRequester(focusRequesters[index])
                    .background(Color.White)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Backspace) {
                            if (otpValues[index].isEmpty() && index > 0) {
                                onUpdateOtpValuesByIndex(index, "")
                                focusRequesters[index - 1].requestFocus()
                            } else {
                                onUpdateOtpValuesByIndex(index, "")
                            }
                            true
                        } else {
                            false
                        }
                    },
                value = value,
                onValueChange = { newValue ->
                    // To use OTP code copied from keyboard
                    if (newValue.length == otpLength) {
                        for (i in otpValues.indices) {
                            onUpdateOtpValuesByIndex(
                                i,
                                if (i < newValue.length && newValue[i].isDigit()) newValue[i].toString() else ""
                            )
                        }

                        keyboardController?.hide()
                        onOtpInputComplete() // you should validate the otp values first for, if it is only digits or isNotEmpty
                    } else if (newValue.length <= 1) {
                        onUpdateOtpValuesByIndex(index, newValue)
                        if (newValue.isNotEmpty()) {
                            if (index < otpLength - 1) {
                                focusRequesters[index + 1].requestFocus()
                            } else {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                onOtpInputComplete()
                            }
                        }
                    } else {
                        if (index < otpLength - 1) focusRequesters[index + 1].requestFocus()
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    },
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onOtpInputComplete()
                    }
                ),
                shape = MaterialTheme.shapes.small,
                isError = isError,
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            )

            LaunchedEffect(value) {
                if (otpValues.all { it.isNotEmpty() }) {
                    focusManager.clearFocus()
                    onOtpInputComplete()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }
}



