package com.example.livingai_lg.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import com.example.livingai_lg.ui.AuthState
import com.example.livingai_lg.ui.screens.AnimalProfileScreen
import com.example.livingai_lg.ui.screens.BuyScreen
import com.example.livingai_lg.ui.screens.ChooseServiceScreen
import com.example.livingai_lg.ui.screens.CreateProfileScreen
import com.example.livingai_lg.ui.screens.FilterScreen
import com.example.livingai_lg.ui.screens.NewListingScreen
import com.example.livingai_lg.ui.screens.PostSaleSurveyScreen
import com.example.livingai_lg.ui.screens.SaleArchiveScreen
import com.example.livingai_lg.ui.screens.SellerProfileScreen
import com.example.livingai_lg.ui.screens.SortScreen
import com.example.livingai_lg.ui.screens.auth.LandingScreen
import com.example.livingai_lg.ui.screens.auth.OtpScreen
import com.example.livingai_lg.ui.screens.auth.SignInScreen
import com.example.livingai_lg.ui.screens.auth.SignUpScreen
import com.example.livingai_lg.ui.navigation.authNavGraph
import com.example.livingai_lg.ui.navigation.mainNavGraph

object AppScreen {
    const val LANDING = "landing"
    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"

    const val OTP = "otp"

    const val CHOOSE_SERVICE = "choose_service"

    const val CREATE_PROFILE = "create_profile"

    const val BUY_ANIMALS = "buy_animals"
    const val ANIMAL_PROFILE = "animal_profile"

    const val CREATE_ANIMAL_LISTING = "create_animal_listing"

    const val BUY_ANIMALS_FILTERS = "buy_animals_filters"
    const val BUY_ANIMALS_SORT = "buy_animals_sort"

    const val SELLER_PROFILE = "seller_profile"

    const val SALE_ARCHIVE = "sale_archive"

    const val POST_SALE_SURVEY = "post_sale_survey"

    const val SAVED_LISTINGS = "saved_listings"

    const val CONTACTS = "contacts"

    const val CALLS = "calls"

    const val CHAT = "chat"

    const val CHATS = "chats"

    const val ACCOUNTS = "accounts"

    fun chats(contact: String) =
        "$CHAT/$contact"

    fun otp(phone: String, name: String) =
        "$OTP/$phone/$name"
    
    fun otpWithSignup(phone: String, name: String, state: String, district: String, village: String) =
        "$OTP/$phone/$name/$state/$district/$village"

    fun createProfile(name: String) =
        "$CREATE_PROFILE/$name"

    fun chooseService(profileId: String) =
        "$CHOOSE_SERVICE/$profileId"
    fun postSaleSurvey(animalId: String) =
        "$POST_SALE_SURVEY/$animalId"

    fun animalProfile(animalId: String) =
        "$ANIMAL_PROFILE/$animalId"

    fun sellerProfile(sellerId: String) =
        "$SELLER_PROFILE/$sellerId"

    fun saleArchive(saleId: String) =
        "$SALE_ARCHIVE/$saleId"

}

object Graph {
    const val AUTH = "auth"
    const val MAIN = "main"

    fun auth(route: String)=
        "$AUTH/$route"

    fun main(route: String)=
        "$MAIN/$route"
}
@Composable
fun AppNavigation(
    authState: AuthState,
    mainViewModel: com.example.livingai_lg.ui.MainViewModel
) {
    val navController = rememberNavController()
    
    // Determine start destination based on initial auth state
    // This prevents showing landing screen when user is already logged in
    val startDestination = remember(authState) {
        when (authState) {
            is AuthState.Authenticated -> Graph.MAIN
            is AuthState.Unauthenticated -> Graph.AUTH
            is AuthState.Unknown -> Graph.AUTH // Show landing while checking
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authNavGraph(navController, mainViewModel)
        mainNavGraph(navController)
    }

    // Handle navigation based on auth state changes
    LaunchedEffect(authState) {
        android.util.Log.d("AppNavigation", "LaunchedEffect triggered with authState: $authState")
        when (authState) {
            is AuthState.Authenticated -> {
                // User is authenticated, navigate to ChooseServiceScreen
                // Add a small delay to ensure NavHost graphs are fully built
                delay(100)
                
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                android.util.Log.d("AppNavigation", "Authenticated - currentRoute: $currentRoute")
                // Only navigate if we're not already in the MAIN graph or ChooseServiceScreen
                if (currentRoute?.startsWith(Graph.MAIN) != true && 
                    currentRoute?.startsWith(AppScreen.CHOOSE_SERVICE) != true) {
                    android.util.Log.d("AppNavigation", "Navigating to ChooseServiceScreen (default profileId: 1)")
                    try {
                        // Navigate directly to the start destination route of MAIN graph
                        // This avoids the "Sequence is empty" error when navigating to Graph.MAIN
                        navController.navigate(AppScreen.chooseService("1")) {
                            // Clear back stack to prevent going back to auth screens
                            popUpTo(Graph.AUTH) { inclusive = true }
                            // Prevent multiple navigations
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AppNavigation", "Navigation error: ${e.message}", e)
                        // Fallback: try navigating to Graph.MAIN if direct route fails
                        try {
                            navController.navigate(Graph.MAIN) {
                                popUpTo(Graph.AUTH) { inclusive = true }
                                launchSingleTop = true
                            }
                        } catch (e2: Exception) {
                            android.util.Log.e("AppNavigation", "Fallback navigation also failed: ${e2.message}", e2)
                        }
                    }
                } else {
                    android.util.Log.d("AppNavigation", "Already in MAIN graph or ChooseServiceScreen, skipping navigation")
                }
            }
            is AuthState.Unauthenticated -> {
                // User is not authenticated, ensure we're in auth graph (landing screen)
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                if (currentRoute?.startsWith(Graph.MAIN) == true || 
                    currentRoute?.startsWith(Graph.AUTH) != true) {
                    navController.navigate(Graph.AUTH) {
                        // Clear back stack to prevent going back to main screens
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            is AuthState.Unknown -> {
                // Still checking auth status
                // If we're on landing screen, stay there
                // If we're on main screen and checking, don't navigate yet
                // This prevents flickering during token validation
            }
        }
    }
//    MainNavGraph(navController)
//    AuthNavGraph(navController)
//    when (authState) {
//        is AuthState.Unauthenticated -> {AuthNavGraph()}
//        is AuthState.Authenticated -> {MainNavGraph()}
//        is AuthState.Unknown -> {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        }
//        AuthState.Loading -> SplashScreen()
//    }

//    val onNavClick: (String) -> Unit = { route ->
//        val currentRoute =
//            navController.currentBackStackEntry?.destination?.route
//
//        if (currentRoute != route) {
//            navController.navigate(route) {
//                launchSingleTop = true
//                restoreState = true
//                popUpTo(navController.graph.startDestinationId) {
//                    saveState = true
//                }
//            }
//        }
//    }



//    NavHost(
//        navController = navController,
//        startDestination = AppScreen.LANDING,
//
//    ) {
//        composable(AppScreen.LANDING) {
//            LandingScreen(
//                onSignUpClick = { navController.navigate(AppScreen.SIGN_UP) },
//                onSignInClick = { navController.navigate(AppScreen.SIGN_IN) },
//                onGuestClick = { navController.navigate(AppScreen.CREATE_PROFILE) }
//            )
//        }
//
//        composable(AppScreen.SIGN_IN) {
//            SignInScreen(
//                onSignUpClick = { navController.navigate(AppScreen.SIGN_UP) },
//                onSignInClick = {
//                    navController.navigate(AppScreen.OTP)
//                }
//            )
//        }
//
//        composable(AppScreen.SIGN_UP) {
//            SignUpScreen(
//                onSignUpClick = {
//                    navController.navigate(AppScreen.OTP)
//                },
//                onSignInClick = {
//                    navController.navigate(AppScreen.SIGN_IN) {
//                        popUpTo(AppScreen.SIGN_UP) { inclusive = true }
//                    }
//                }
//            )
//        }
//
//        composable(AppScreen.OTP) {
//            OtpScreen(
//                onContinue = { navController.navigate(AppScreen.CREATE_PROFILE) }
//            )
//        }

//        composable(AppScreen.CREATE_PROFILE) {
//            CreateProfileScreen (
//                onProfileSelected = { profileId ->
//                    if (profileId == "buyer_seller") {
//                        navController.navigate(AppScreen.BUY_ANIMALS)
//                    } else {
//                        navController.navigate(AppScreen.CHOOSE_SERVICE)
//                    } },
//            )
//        }
//
//        composable(AppScreen.CHOOSE_SERVICE) {
//            ChooseServiceScreen (
//                onServiceSelected = { navController.navigate(AppScreen.LANDING) },
//            )
//        }
//
//        composable(AppScreen.BUY_ANIMALS) {
//            BuyScreen(
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onProductClick = { animalId ->
//                    navController.navigate(
//                        AppScreen.animalProfile(animalId)
//                    )
//                },
//                onNavClick = onNavClick,
//                onFilterClick = {navController.navigate(AppScreen.BUY_ANIMALS_FILTERS)},
//                onSortClick = {navController.navigate(AppScreen.BUY_ANIMALS_SORT)},
//                onSellerClick = { sellerId ->
//                    navController.navigate(
//                        AppScreen.sellerProfile(sellerId)
//                    )
//                },
//            )
//        }
//
//        composable(AppScreen.BUY_ANIMALS_FILTERS) {
//            FilterScreen(
//                onSubmitClick = {navController.navigate(AppScreen.BUY_ANIMALS)},
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onSkipClick = {
//                    navController.popBackStack()
//                },
//                onCancelClick = {
//                    navController.popBackStack()
//                },
//
//            )
//        }
//
//        composable(AppScreen.BUY_ANIMALS_SORT) {
//            SortScreen(
//                onApplyClick = {navController.navigate(AppScreen.BUY_ANIMALS)},
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onSkipClick = {
//                    navController.popBackStack()
//                },
//                onCancelClick = {
//                    navController.popBackStack()
//                },
//
//                )
//        }
//
//        composable(AppScreen.CREATE_ANIMAL_LISTING) {
//            NewListingScreen (
//                onSaveClick = {navController.navigate(
//                    AppScreen.postSaleSurvey("2")
//                )},
//                onBackClick = {
//                    navController.popBackStack()
//                }
//            )
//        }
//
//        composable(
//            route = "${AppScreen.SALE_ARCHIVE}/{saleId}",
//            arguments = listOf(
//                navArgument("saleId") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//
//            val saleId = backStackEntry
//                .arguments
//                ?.getString("saleId")
//                ?: return@composable
//
//            SaleArchiveScreen(
//                saleId = saleId,
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onSaleSurveyClick = { saleId ->
//                    navController.navigate(
//                        AppScreen.sellerProfile(saleId)
//                    )
//                },
//            )
//        }
//
//        composable(
//            route = "${AppScreen.POST_SALE_SURVEY}/{animalId}",
//            arguments = listOf(
//                navArgument("animalId") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//
//            val animalId = backStackEntry
//                .arguments
//                ?.getString("animalId")
//                ?: return@composable
//
//            PostSaleSurveyScreen (
//                animalId = animalId,
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onSubmit = {navController.navigate(
//                    AppScreen.saleArchive("2")
//                )}
//            )
//        }
//
//        composable(
//            route = "${AppScreen.ANIMAL_PROFILE}/{animalId}",
//            arguments = listOf(
//                navArgument("animalId") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//
//            val animalId = backStackEntry
//                .arguments
//                ?.getString("animalId")
//                ?: return@composable
//
//            AnimalProfileScreen(
//                animalId = animalId,
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onSellerClick = { sellerId ->
//                    navController.navigate(
//                        AppScreen.sellerProfile(sellerId)
//                    )
//                },
//            )
//        }
//
//        composable(
//            route = "${AppScreen.SELLER_PROFILE}/{sellerId}",
//            arguments = listOf(
//                navArgument("sellerId") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//
//            val sellerId = backStackEntry
//                .arguments
//                ?.getString("sellerId")
//                ?: return@composable
//
//            SellerProfileScreen(
//                sellerId = sellerId,
//                onBackClick = {
//                    navController.popBackStack()
//                }
//            )
//        }
//        composable(AppScreen.SELLER_PROFILE) {
//            SellerProfileScreen (
//                onBackClick = {
//                    navController.popBackStack()
//                }
//            )
//        }


//    }
}


