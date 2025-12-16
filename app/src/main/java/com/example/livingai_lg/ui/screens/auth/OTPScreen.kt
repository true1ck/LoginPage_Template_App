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
import com.example.livingai_lg.api.TokenManager
import com.example.livingai_lg.ui.components.backgrounds.DecorativeBackground
import kotlin.math.min
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.coroutines.launch


@Composable
fun OtpScreen(
    phoneNumber: String,
    name: String,
    onSuccess: () -> Unit = {},
    onCreateProfile: (name: String) -> Unit = {},
) {
    val otp = remember { mutableStateOf("") }
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val authManager = remember { AuthManager(context, AuthApiClient(context), TokenManager(context)) }

    // Flag to determine if this is a sign-in flow for an existing user.
    val isSignInFlow = name == "existing_user"

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

        // ---------------------------
        // "Enter OTP" Title
        // ---------------------------
        Text(
            text = "Enter OTP",
            color = Color(0xFF927B5E),
            fontSize = fs(20f),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.offset(x = s(139f), y = s(279f)),
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
//        Row(
//            Modifier.offset(x = s(38f), y = s(319f)),
//            horizontalArrangement = Arrangement.spacedBy(s(17f))
//        ) {
//            repeat(4) { index ->
//                OtpBox(
//                    index = index,
//                    otp = otp.value,
//                    onChange = { if (it.length <= 6) otp.value = it },
//                    scale = scale
//                )
//            }
//        }
        OtpInputRow(
            otpLength = 6,
            scale = scale,
            otp = otp.value,
            onOtpChange = { if (it.length <= 6) otp.value = it }
        )
        
        // ---------------------------
        // Continue Button
        // ---------------------------
        Box(
            modifier = Modifier
                .offset(x = s(57f), y = s(411f))
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
                            authManager.login(phoneNumber, otp.value)
                                .onSuccess { response ->
                                    if (isSignInFlow) {
                                        // For existing users, always go to the success screen.
                                        onSuccess()
                                    } else {
                                        // For new users, check if a profile needs to be created.
                                        if (response.needsProfile) {
                                            onCreateProfile(name)
                                        } else {
                                            onSuccess()
                                        }
                                    }
                                }
                                .onFailure {
                                    Toast.makeText(context, "Invalid or expired OTP", Toast.LENGTH_SHORT).show()
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

@Composable
fun OtpInputRow(
    otpLength: Int = 4,
    scale: Float,
    otp: String,
    onOtpChange: (String) -> Unit
) {
    val focusRequesters = remember {
        List(otpLength) { FocusRequester() }
    }

    Row(horizontalArrangement = Arrangement.spacedBy((12 * scale).dp)) {
        repeat(otpLength) { index ->
            OtpBox(
                index = index,
                otp = otp,
                scale = scale,
                focusRequester = focusRequesters[index],
                onRequestFocus = {
                    val firstEmpty = otp.length.coerceAtMost(otpLength - 1)
                    focusRequesters[firstEmpty].requestFocus()
                },
                onNextFocus = {
                    if (index + 1 < otpLength) {
                        focusRequesters[index + 1].requestFocus()
                    }
                },
                onPrevFocus = {
                    if (index - 1 >= 0) {
                        focusRequesters[index - 1].requestFocus()
                    }
                },
                onChange = onOtpChange
            )
        }
    }
}

@Composable
private fun OtpBox(
    index: Int,
    otp: String,
    scale: Float,
    focusRequester: FocusRequester,
    onRequestFocus: () -> Unit,
    onNextFocus: () -> Unit,
    onPrevFocus: () -> Unit,
    onChange: (String) -> Unit
) {
    val boxW = 66f * scale
    val boxH = 52f * scale
    val radius = 16f * scale

    val char = otp.getOrNull(index)?.toString() ?: ""

    Box(
        modifier = Modifier
            .size(boxW.dp, boxH.dp)
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
                        val updated = otp.padEnd(index, ' ').toMutableList()
                        if (updated.size > index) updated[index] = new.first()
                        else updated.add(new.first())
                        onChange(updated.joinToString("").trim())
                        onNextFocus()
                    }

                    // BACKSPACE
                    new.isEmpty() -> {
                        if (char.isNotEmpty()) {
                            val updated = otp.toMutableList()
                            updated.removeAt(index)
                            onChange(updated.joinToString(""))
                        } else {
                            onPrevFocus()
                        }
                    }
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
            singleLine = true,
            modifier = Modifier
                .focusRequester(focusRequester)
                .align(Alignment.Center)
        )
    }
}

