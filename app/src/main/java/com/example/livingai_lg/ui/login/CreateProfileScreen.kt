package com.example.livingai_lg.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.livingai_lg.R
import com.example.livingai_lg.api.AuthApiClient
import com.example.livingai_lg.api.AuthManager
import com.example.livingai_lg.api.TokenManager
import com.example.livingai_lg.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun CreateProfileScreen(navController: NavController, name: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val authManager = remember { AuthManager(context, AuthApiClient("http://10.0.2.2:3000"), TokenManager(context)) }

    fun updateProfile(userType: String) {
        scope.launch {
            authManager.updateProfile(name, userType)
                .onSuccess {
                    navController.navigate("success") { popUpTo("login") { inclusive = true } }
                }
                .onFailure {
                    Toast.makeText(context, "Failed to update profile: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

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

            Text("Create Profile", fontSize = 32.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            Text("Choose Profile Type", fontSize = 16.sp, color = Color(0xFF4A5565))

            Spacer(modifier = Modifier.height(64.dp))

            ProfileTypeItem(text = "I'm a Seller", icon = R.drawable.ic_seller) { updateProfile("seller") }
            Spacer(modifier = Modifier.height(16.dp))
            ProfileTypeItem(text = "I'm a Buyer", icon = R.drawable.ic_buyer) { updateProfile("buyer") }
            Spacer(modifier = Modifier.height(16.dp))
            ProfileTypeItem(text = "I'm a Service Provider", icon = R.drawable.ic_service_provider) { updateProfile("service_provider") }
            Spacer(modifier = Modifier.height(16.dp))
            ProfileTypeItem(text = "I'm a Mandi Host", icon = R.drawable.ic_mandi_host) { /* TODO: Add user_type for Mandi Host */ }
        }
    }
}

@Composable
fun ProfileTypeItem(text: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = null, tint = Color.Unspecified)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Spacer(modifier = Modifier.weight(1f))
        Icon(painter = painterResource(id = R.drawable.ic_dot), contentDescription = null, tint = Color.Unspecified)
    }
}

@Preview(showBackground = true)
@Composable
fun CreateProfileScreenPreview() {
    LivingAi_LgTheme {
        CreateProfileScreen(rememberNavController(), "John Doe")
    }
}
