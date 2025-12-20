package com.example.livingai_lg.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.farmmarketplace.ui.screens.CallsScreen
import com.example.farmmarketplace.ui.screens.ContactsScreen
import com.example.livingai_lg.ui.AuthState
import com.example.livingai_lg.ui.MainViewModel
import com.example.livingai_lg.ui.screens.AccountsScreen
import com.example.livingai_lg.ui.screens.AnimalProfileScreen
import com.example.livingai_lg.ui.screens.BuyScreen
import com.example.livingai_lg.ui.screens.ChooseServiceScreen
import com.example.livingai_lg.ui.screens.CreateProfileScreen
import com.example.livingai_lg.ui.screens.FilterScreen
import com.example.livingai_lg.ui.screens.NewListingScreen
import com.example.livingai_lg.ui.screens.PostSaleSurveyScreen
import com.example.livingai_lg.ui.screens.SaleArchiveScreen
import com.example.livingai_lg.ui.screens.SavedListingsScreen
import com.example.livingai_lg.ui.screens.SellerProfileScreen
import com.example.livingai_lg.ui.screens.SortScreen
import com.example.livingai_lg.ui.screens.auth.LandingScreen
import com.example.livingai_lg.ui.screens.auth.OtpScreen
import com.example.livingai_lg.ui.screens.auth.SignInScreen
import com.example.livingai_lg.ui.screens.auth.SignUpScreen
import com.example.livingai_lg.ui.screens.chat.ChatScreen
import com.example.livingai_lg.ui.screens.chat.ChatsScreen
import com.example.livingai_lg.ui.screens.testing.ApiTestScreen

fun NavController.navigateIfAuthenticated(
    authState: AuthState,
    targetRoute: String,
    fallbackRoute: String = AppScreen.LANDING
) {
    if (authState is AuthState.Authenticated) {
        navigate(targetRoute) {
            launchSingleTop = true
        }
    } else {
        navigate(fallbackRoute) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }
}

@Composable
fun AppNavigation(
    authState: AuthState,
    mainViewModel: MainViewModel
) {
    val navController = rememberNavController()

    /* ---------------------------------------------------
     * Collect one-time navigation events (OTP, login, logout)
     * --------------------------------------------------- */
    LaunchedEffect(Unit) {
        mainViewModel.navEvents.collect { event ->
            when (event) {

                is NavEvent.ToCreateProfile -> {
                    navController.navigate(
                        AppScreen.createProfile(event.name)
                    ) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                is NavEvent.ToChooseService -> {
                    navController.navigate(
                        AppScreen.chooseService(event.profileId)
                    ) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                NavEvent.ToLanding -> {
                    navController.navigate(AppScreen.LANDING) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    val onNavClick: (String) -> Unit = { route ->
        val currentRoute =
            navController.currentBackStackEntry?.destination?.route
        Log.d("Current Route:"," $currentRoute $route")

        if (currentRoute != route) {
            navController.navigate(route) {
                launchSingleTop = true
                //restoreState = true
//                popUpTo(navController.graph.startDestinationId) {
//                    saveState = true
//                }
            }
        }
    }

    /* ---------------------------------------------------
     * Single NavHost
     * --------------------------------------------------- */
    NavHost(
        navController = navController,
        startDestination = if(authState is AuthState.Authenticated) AppScreen.BUY_ANIMALS else AppScreen.LANDING
    ) {

        /* ---------------- AUTH ---------------- */

        composable(AppScreen.LANDING) {
            LandingScreen(
                onSignUpClick = {
                    navController.navigate(AppScreen.SIGN_UP)
                },
                onSignInClick = {
                    navController.navigate(AppScreen.SIGN_IN)
                },
                onGuestClick = {
                    mainViewModel.emitNavEvent(
                        NavEvent.ToCreateProfile("guest")
                    )
                }
            )
        }

        composable(AppScreen.SIGN_IN) {
            SignInScreen(
                onSignUpClick = {
                    navController.navigate(AppScreen.SIGN_UP)
                },
                onSignInClick = { phone, name ->
                    navController.navigate(
                        AppScreen.otp(phone, name)
                    )
                }
            )
        }

        composable(AppScreen.SIGN_UP) {
            SignUpScreen(
                onSignUpClick = { phone, name, state, district, village ->
                    navController.navigate(
                        AppScreen.otpWithSignup(
                            phone, name, state, district, village
                        )
                    )
                },
                onSignInClick = {
                    navController.navigate(AppScreen.SIGN_IN)
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
                mainViewModel = mainViewModel,
                phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: "",
                name = backStackEntry.arguments?.getString("name") ?: "",
                onCreateProfile = { name ->
//                    navController.navigateIfAuthenticated(
//                        authState,
//                        AppScreen.createProfile(name),
//                        AppScreen.LANDING
//                    )
                    mainViewModel.emitNavEvent(
                        NavEvent.ToCreateProfile(name)
                    )
                },
                onSuccess = {
//                    navController.navigateIfAuthenticated(
//                        authState,
//                        AppScreen.chooseService(""),
//                        AppScreen.LANDING
//                    )
                    mainViewModel.emitNavEvent(
                        NavEvent.ToChooseService()
                    )
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
                mainViewModel = mainViewModel,
                phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: "",
                name = backStackEntry.arguments?.getString("name") ?: "",
                signupState = backStackEntry.arguments?.getString("state"),
                signupDistrict = backStackEntry.arguments?.getString("district"),
                signupVillage = backStackEntry.arguments?.getString("village"),
                onCreateProfile = { name ->
                    mainViewModel.emitNavEvent(
                        NavEvent.ToCreateProfile(name)
                    )
                },
                onSuccess = {
                    mainViewModel.emitNavEvent(
                        NavEvent.ToChooseService()
                    )
                }
            )
        }

        /* ---------------- MAIN ---------------- */

        composable(
            "${AppScreen.CREATE_PROFILE}/{name}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            CreateProfileScreen(
                name = backStackEntry.arguments?.getString("name") ?: "",
                onProfileSelected = { profileId ->
                    mainViewModel.emitNavEvent(
                        NavEvent.ToChooseService(profileId)
                    )
                }
            )
        }

        composable(
            "${AppScreen.CHOOSE_SERVICE}/{profileId}",
            arguments = listOf(
                navArgument("profileId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ChooseServiceScreen(
                profileId = backStackEntry.arguments?.getString("profileId") ?: "",
                onServiceSelected = {
                    navController.navigate(AppScreen.BUY_ANIMALS)
//                    navController.navigateIfAuthenticated(
//                        authState,
//                        AppScreen.BUY_ANIMALS
//                    )
                }
            )
        }

        composable(AppScreen.BUY_ANIMALS) {
            BuyScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onProductClick = { animalId ->
                    navController.navigate(
                        AppScreen.animalProfile(animalId)
                    )
                },
                onNavClick = onNavClick,
                onSellerClick = { sellerId ->
                    navController.navigate(
                        AppScreen.sellerProfile(sellerId)
                    )
                },
            )
        }

        composable(AppScreen.BUY_ANIMALS_FILTERS) {
            FilterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSubmitClick = {navController.navigate(AppScreen.BUY_ANIMALS)},
                onCancelClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(AppScreen.BUY_ANIMALS_SORT) {
            SortScreen(
                onApplyClick = {navController.navigate(AppScreen.BUY_ANIMALS)},
                onBackClick = {
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                },

                )
        }

        composable(AppScreen.SAVED_LISTINGS) {
            SavedListingsScreen(
                onNavClick = onNavClick,
                onBackClick = { navController.popBackStack() })
        }

        composable(AppScreen.ACCOUNTS) {
            AccountsScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    // Navigate to auth graph after logout
                    navController.navigate(AppScreen.LANDING) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onApiTest = {
                    navController.navigate(AppScreen.API_TEST)
                }
            )
        }


        composable(AppScreen.CREATE_ANIMAL_LISTING) {
            NewListingScreen (
                onSaveClick = {navController.navigate(
                    AppScreen.postSaleSurvey("2")
                )},
                onBackClick = {
                    navController.navigate(AppScreen.BUY_ANIMALS){
                        popUpTo(AppScreen.BUY_ANIMALS){
                            inclusive = true
                        }
                    }

                }
            )
        }

        composable(
            route = "${AppScreen.SALE_ARCHIVE}/{saleId}",
            arguments = listOf(
                navArgument("saleId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val saleId = backStackEntry
                .arguments
                ?.getString("saleId")
                ?: return@composable

            SaleArchiveScreen(
                saleId = saleId,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaleSurveyClick = { saleId ->
                    navController.navigate(
                        AppScreen.sellerProfile(saleId)
                    )
                },
            )
        }

        composable(
            route = "${AppScreen.POST_SALE_SURVEY}/{animalId}",
            arguments = listOf(
                navArgument("animalId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val animalId = backStackEntry
                .arguments
                ?.getString("animalId")
                ?: return@composable

            PostSaleSurveyScreen (
                animalId = animalId,
                onBackClick = {
                    navController.navigate(AppScreen.CREATE_ANIMAL_LISTING)
                },
                onSubmit = {navController.navigate(
                    AppScreen.saleArchive("2")
                )}
            )
        }

        composable(
            route = "${AppScreen.ANIMAL_PROFILE}/{animalId}",
            arguments = listOf(
                navArgument("animalId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val animalId = backStackEntry
                .arguments
                ?.getString("animalId")
                ?: return@composable

            AnimalProfileScreen(
                animalId = animalId,
                onBackClick = {
                    navController.popBackStack()
                },
                onNavClick = onNavClick,
                onSellerClick = { sellerId ->
                    navController.navigate(
                        AppScreen.sellerProfile(sellerId)
                    )
                },
            )
        }

        composable(
            route = "${AppScreen.SELLER_PROFILE}/{sellerId}",
            arguments = listOf(
                navArgument("sellerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val sellerId = backStackEntry
                .arguments
                ?.getString("sellerId")
                ?: return@composable

            SellerProfileScreen(
                sellerId = sellerId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppScreen.CONTACTS) {
            ContactsScreen(
                onBackClick = {navController.navigate(AppScreen.BUY_ANIMALS)},//{navController.popBackStack()},
                onTabClick  = onNavClick,
            )
        }

        composable(AppScreen.CALLS) {
            CallsScreen(
                onBackClick = {navController.navigate(AppScreen.BUY_ANIMALS)},//{navController.popBackStack()},
                onTabClick  = onNavClick,
            )
        }

        composable(AppScreen.CHATS) {
            ChatsScreen(
                onBackClick = {navController.navigate(AppScreen.BUY_ANIMALS)},//{navController.popBackStack()},
                onTabClick  = onNavClick,
                onChatItemClick = {navController.navigate(AppScreen.chats("2"))}
            )
        }

        composable(
            route = "${AppScreen.CHAT}/{contact}",
            arguments = listOf(
                navArgument("contact") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val sellerId = backStackEntry
                .arguments
                ?.getString("contact")
                ?: return@composable

            ChatScreen(
                sellerId,
                onBackClick = {
                    navController.navigate(AppScreen.CHATS)
                    //navController.popBackStack()
                }
            )
        }

        //A route for testing
        composable(AppScreen.API_TEST) {
            ApiTestScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

