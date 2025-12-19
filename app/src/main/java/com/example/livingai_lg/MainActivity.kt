package com.example.livingai_lg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.livingai_lg.ui.MainViewModel
import com.example.livingai_lg.ui.MainViewModelFactory
import com.example.livingai_lg.ui.login_legacy.*
import com.example.livingai_lg.ui.navigation.AppNavigation
import com.example.livingai_lg.ui.theme.FarmMarketplaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            FarmMarketplaceTheme {
                val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(LocalContext.current))
                val authState by mainViewModel.authState.collectAsState()

                AppNavigation(authState)
//                when (authState) {
//                    is AuthState.Unknown -> {
//                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                            CircularProgressIndicator()
//                        }
//                    }
//                    is AuthState.Authenticated -> {
//                        SuccessScreen(mainViewModel)
//                    }
//                    is AuthState.Unauthenticated -> {
//                        AuthNavigation()
//                    }
//                }
            }
        }
    }
}


// TODO: remove the old code after testing new stuff
@Composable
fun AuthNavigation() {
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
                navArgument("name") { type = NavType.StringType }
            )
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
