package com.example.livingai_lg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
// Note: CoroutineScope, Dispatchers, delay, launch still used in onCreateProfile callbacks
import com.example.livingai_lg.ui.screens.SaleArchiveScreen
import com.example.livingai_lg.ui.screens.auth.LandingScreen
import com.example.livingai_lg.ui.screens.auth.OtpScreen
import com.example.livingai_lg.ui.screens.auth.SignInScreen
import com.example.livingai_lg.ui.screens.auth.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavController, mainViewModel: com.example.livingai_lg.ui.MainViewModel) {
    navigation(
        route = Graph.AUTH,
        startDestination = AppScreen.LANDING
    ) {
        composable(AppScreen.LANDING) {
            LandingScreen(
                onSignUpClick = { navController.navigate(AppScreen.SIGN_UP) },
                onSignInClick = { navController.navigate(AppScreen.SIGN_IN) },
                onGuestClick = { navController.navigate(Graph.main(AppScreen.createProfile("guest"))) }
            )
        }

        composable(AppScreen.SIGN_IN) {
            SignInScreen(
                onSignUpClick = { navController.navigate(AppScreen.SIGN_UP){
                    popUpTo(AppScreen.SIGN_IN) { inclusive = true }
                } },
                onSignInClick = { phone, name ->
                    navController.navigate(AppScreen.otp(phone,name))
                }
            )
        }

        composable(AppScreen.SIGN_UP) {
            SignUpScreen(
                onSignUpClick = { phone, name, state, district, village ->
                    navController.navigate(AppScreen.otpWithSignup(phone, name, state, district, village))
                },
                onSignInClick = {
                    navController.navigate(AppScreen.SIGN_IN) {
                        popUpTo(AppScreen.SIGN_UP) { inclusive = true }
                    }
                }
            )
        }

        composable(
            "${AppScreen.OTP}/{phoneNumber}/{name}",
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            OtpScreen(
                phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: "",
                name = backStackEntry.arguments?.getString("name") ?: "",
                mainViewModel = mainViewModel,
                onCreateProfile = {name -> 
                    android.util.Log.d("AuthNavGraph", "Navigating to create profile with name: $name")
                    // Navigate to main graph first without popping, then navigate to specific route
                    try {
                        // Navigate to main graph (this will use its start destination)
                        navController.navigate(Graph.MAIN) {
                            // Don't pop the AUTH graph yet - keep the graph structure
                            launchSingleTop = true
                        }
                        // Then navigate to the specific route after a short delay
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(200)
                            try {
                                navController.navigate(AppScreen.createProfile(name)) {
                                    // Now pop the AUTH graph after we're in the MAIN graph
                                    popUpTo(Graph.AUTH) { inclusive = true }
                                }
                            } catch (e: Exception) {
                                android.util.Log.e("AuthNavGraph", "Secondary navigation error: ${e.message}", e)
                            }
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AuthNavGraph", "Navigation error: ${e.message}", e)
                    }
                },
                onLanding = {
                    android.util.Log.d("AuthNavGraph", "Navigating to landing page for new user")
                    // Navigate to landing page within AUTH graph
                    try {
                        navController.navigate(AppScreen.LANDING) {
                            // Pop all screens up to and including the current OTP screen
                            popUpTo(Graph.AUTH) { inclusive = false }
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AuthNavGraph", "Navigation to landing error: ${e.message}", e)
                    }
                },
                onSuccess = { 
                    android.util.Log.d("AuthNavGraph", "Sign-in successful - navigating to ChooseServiceScreen")
                    // Navigate to MAIN graph which starts at ChooseServiceScreen (startDestination)
                    try {
                        navController.navigate(Graph.MAIN) {
                            // Clear back stack to prevent going back to auth screens
                            popUpTo(Graph.AUTH) { inclusive = true }
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AuthNavGraph", "Navigation error: ${e.message}", e)
                    }
                }
            )
        }

        composable(
            "${AppScreen.OTP}/{phoneNumber}/{name}/{state}/{district}/{village}",
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("state") { type = NavType.StringType },
                navArgument("district") { type = NavType.StringType },
                navArgument("village") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            OtpScreen(
                phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: "",
                name = backStackEntry.arguments?.getString("name") ?: "",
                signupState = backStackEntry.arguments?.getString("state"),
                signupDistrict = backStackEntry.arguments?.getString("district"),
                signupVillage = backStackEntry.arguments?.getString("village"),
                mainViewModel = mainViewModel,
                onCreateProfile = {name -> 
                    android.util.Log.d("AuthNavGraph", "Navigating to create profile with name: $name")
                    // Navigate to main graph first without popping, then navigate to specific route
                    try {
                        // Navigate to main graph (this will use its start destination)
                        navController.navigate(Graph.MAIN) {
                            // Don't pop the AUTH graph yet - keep the graph structure
                            launchSingleTop = true
                        }
                        // Then navigate to the specific route after a short delay
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(200)
                            try {
                                navController.navigate(AppScreen.createProfile(name)) {
                                    // Now pop the AUTH graph after we're in the MAIN graph
                                    popUpTo(Graph.AUTH) { inclusive = true }
                                }
                            } catch (e: Exception) {
                                android.util.Log.e("AuthNavGraph", "Secondary navigation error: ${e.message}", e)
                            }
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AuthNavGraph", "Navigation error: ${e.message}", e)
                    }
                },
                onLanding = {
                    android.util.Log.d("AuthNavGraph", "Navigating to landing page for new user")
                    // Navigate to landing page within AUTH graph
                    try {
                        navController.navigate(AppScreen.LANDING) {
                            // Pop all screens up to and including the current OTP screen
                            popUpTo(Graph.AUTH) { inclusive = false }
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AuthNavGraph", "Navigation to landing error: ${e.message}", e)
                    }
                },
                onSuccess = { 
                    android.util.Log.d("AuthNavGraph", "Sign-in successful - navigating to ChooseServiceScreen")
                    // Navigate to MAIN graph which starts at ChooseServiceScreen (startDestination)
                    try {
                        navController.navigate(Graph.MAIN) {
                            // Clear back stack to prevent going back to auth screens
                            popUpTo(Graph.AUTH) { inclusive = true }
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AuthNavGraph", "Navigation error: ${e.message}", e)
                    }
                }
            )
        }
    }
}
