
package com.example.livingai_lg.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.livingai_lg.ui.theme.*

@Composable
fun SignUpScreen(navController: NavController) {
    val name = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(LightCream, LighterCream, LightestGreen)
                )
            )
    ) {
        // Decorative elements from LoginScreen
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
           // ...
        }
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
            Text("Find your perfect livestock", fontSize = 16.sp, color = Color(0xFF4A5565))

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Enter Name",
                fontSize = 16.sp,
                color = Color(0xFF364153),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start).padding(start = 21.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                placeholder = { Text("Enter your name", color = Color(0xFF99A1AF)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF99A1AF)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                    disabledContainerColor = Color.White.copy(alpha = 0.9f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(52.dp).shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp)),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("otp") },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE9A00)),
                modifier = Modifier.fillMaxWidth().height(56.dp).shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text("Sign In", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Don\'t have an account? ", color = Color(0xFF4A5565), fontSize = 16.sp)
                Text(
                    text = "Sign up",
                    color = Color(0xFFE17100),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    LivingAi_LgTheme {
        SignUpScreen(rememberNavController())
    }
}
