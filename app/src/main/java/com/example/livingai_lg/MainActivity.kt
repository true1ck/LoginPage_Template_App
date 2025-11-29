package com.example.livingai_lg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.livingai_lg.ui.login.*
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
                    composable("signin") {
                        SignInScreen(navController = navController)
                    }
                    composable(
                        "otp/{phoneNumber}/{name}",
                        arguments = listOf(
                            navArgument("phoneNumber") { type = NavType.StringType },
                            navArgument("name") { type = NavType.StringType })
                    ) { backStackEntry ->
                        OtpScreen(
                            navController = navController,
                            phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: "",
                            name = backStackEntry.arguments?.getString("name") ?: ""
                        )
                    }
                    composable(
                        "create_profile/{name}",
                        arguments = listOf(navArgument("name") { type = NavType.StringType })
                    ) { backStackEntry ->
                        CreateProfileScreen(
                            navController = navController,
                            name = backStackEntry.arguments?.getString("name") ?: ""
                        )
                    }
                    composable("success") {
                        SuccessScreen()
                    }
                }
            }
        }
    }
}
