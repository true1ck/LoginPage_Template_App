
package com.example.livingai_lg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.livingai_lg.ui.login.LoginScreen
import com.example.livingai_lg.ui.login.SignUpScreen
import com.example.livingai_lg.ui.login.OtpScreen
import com.example.livingai_lg.ui.login.CreateProfileScreen
import com.example.livingai_lg.ui.theme.LivingAi_LgTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LivingAi_LgTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    composable("signup") {
                        SignUpScreen(navController = navController)
                    }
                    composable("otp") {
                        OtpScreen(navController = navController)
                    }
                    composable("create_profile") {
                        CreateProfileScreen(navController = navController)
                    }
                }
            }
        }
    }
}
