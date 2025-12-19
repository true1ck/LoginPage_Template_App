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
import com.example.livingai_lg.ui.screens.SaleArchiveScreen
import com.example.livingai_lg.ui.screens.auth.LandingScreen
import com.example.livingai_lg.ui.screens.auth.OtpScreen
import com.example.livingai_lg.ui.screens.auth.SignInScreen
import com.example.livingai_lg.ui.screens.auth.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
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
                onSignUpClick = { phone, name ->
                    navController.navigate(AppScreen.otp(phone,name))
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
                onCreateProfile = {name -> navController.navigate(Graph.main(AppScreen.createProfile(name)))},
                onSuccess = { navController.navigate(Graph.auth(AppScreen.chooseService("1")))}
            )
        }
    }
}
