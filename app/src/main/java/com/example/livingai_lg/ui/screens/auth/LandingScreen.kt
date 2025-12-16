package com.example.livingai_lg.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.components.backgrounds.DecorativeBackground
import com.example.livingai_lg.ui.components.IconCircle
import com.example.livingai_lg.ui.theme.FarmButtonPrimary
import com.example.livingai_lg.ui.theme.FarmButtonSecondary
import com.example.livingai_lg.ui.theme.FarmButtonText

import com.example.livingai_lg.ui.theme.FarmSproutBg
import com.example.livingai_lg.ui.theme.FarmSproutIcon
import com.example.livingai_lg.ui.theme.FarmSunBg
import com.example.livingai_lg.ui.theme.FarmSunIcon
import com.example.livingai_lg.ui.theme.FarmTextDark
import com.example.livingai_lg.ui.theme.FarmTextLight
import com.example.livingai_lg.ui.theme.FarmTextNormal
import com.example.livingai_lg.ui.theme.FarmWheatBg
import com.example.livingai_lg.ui.theme.FarmWheatIcon
import com.example.livingai_lg.R

@Composable
fun LandingScreen(
    onSignUpClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onGuestClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmTextLight)
    ) {
        // Decorative background elements
        DecorativeBackground()

        // Main content
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp)
        ) {
            val horizontalPadding = maxWidth * 0.06f  // proportional padding

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding)
                    .padding( top = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //Spacer(modifier = Modifier.height(24.dp))

                // Icon row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    IconCircle(
                        backgroundColor = FarmWheatBg,
                        size = 56.dp
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_wheat),
                            contentDescription = "Wheat Icon",
                            tint = FarmWheatIcon    // keeps original vector colors
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    IconCircle(
                        backgroundColor = FarmSproutBg,
                        size = 64.dp
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sprout),
                            contentDescription = "Wheat Icon",
                            tint = FarmSproutIcon    // keeps original vector colors
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    IconCircle(
                        backgroundColor = FarmSunBg,
                        size = 56.dp
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sun),
                            contentDescription = "Wheat Icon",
                            tint = FarmSunIcon    // keeps original vector colors
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Welcome text
                Text(
                    text = "Welcome!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = FarmTextDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Join the farm marketplace community",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = FarmTextNormal
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Sign up button
                Button(
                    onClick = onSignUpClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FarmButtonPrimary,
                        contentColor = FarmButtonText
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "New user? Sign up",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sign in button
                Button(
                    onClick = onSignInClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FarmButtonSecondary,
                        contentColor = FarmButtonText
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Already a user? Sign in",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Continue as guest
                TextButton(
                    onClick = onGuestClick
                ) {
                    Text(
                        text = "Continue as Guest",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmTextNormal,
                        textDecoration = TextDecoration.Underline
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
