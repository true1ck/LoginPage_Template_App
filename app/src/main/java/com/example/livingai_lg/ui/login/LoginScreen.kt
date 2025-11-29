package com.example.livingai_lg.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.livingai_lg.ui.theme.*

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(LightCream, LighterCream, LightestGreen)
                )
            )
    ) {
        // Decorative elements...

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Icon Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Gold, RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌾", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(24.dp))
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(LightOrange.copy(alpha = 0.72f), RoundedCornerShape(36.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌱", fontSize = 32.sp)
                }
                Spacer(modifier = Modifier.width(24.dp))
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(DarkOrange.copy(alpha = 0.67f), RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "☀️", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = DarkBrown
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Join the farm marketplace community",
                fontSize = 16.sp,
                color = MidBrown,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))

            // New User Button
            Button(
                onClick = { navController.navigate("signup") },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightOrange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "New user? Sign up", color = DarkerBrown, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Existing User Button
            Button(
                onClick = { navController.navigate("signin") },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TerraCotta),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Already a user? Sign in", color = DarkerBrown, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Guest Button
            Text(
                text = "Continue as Guest",
                color = MidBrown,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { Toast.makeText(context, "Guest mode is not yet available", Toast.LENGTH_SHORT).show() }
            )

            Spacer(modifier = Modifier.weight(1.5f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LivingAi_LgTheme {
        LoginScreen(rememberNavController())
    }
}
