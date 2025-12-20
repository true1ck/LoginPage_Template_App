package com.example.livingai_lg.ui.navigation.navigation_legacy

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.farmmarketplace.ui.screens.CallsScreen
import com.example.farmmarketplace.ui.screens.ContactsScreen
import com.example.livingai_lg.ui.navigation.AppScreen
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
import com.example.livingai_lg.ui.screens.chat.ChatScreen
import com.example.livingai_lg.ui.screens.chat.ChatsScreen

fun NavGraphBuilder.mainNavGraph(navController: NavController) {
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

    navigation(
        route = Graph.MAIN,
        startDestination = AppScreen.chooseService("1") // Default to ChooseServiceScreen for authenticated users
    ){


//    NavHost(
//        navController = navController,
//        startDestination = AppScreen.createProfile("guest")
//    ) {
        composable(
            "${AppScreen.CREATE_PROFILE}/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            CreateProfileScreen(
                 name = backStackEntry.arguments?.getString("name") ?: "",
                onProfileSelected = { profileId ->
                    navController.navigate(AppScreen.chooseService(profileId))

                }
            )
        }

        composable(
            "${AppScreen.CHOOSE_SERVICE}/{profileId}",
            arguments = listOf(navArgument("profileId") { type = NavType.StringType })
        ) { backStackEntry ->
            ChooseServiceScreen (
                profileId = backStackEntry.arguments?.getString("profileId") ?: "",
                onServiceSelected = { navController.navigate(AppScreen.BUY_ANIMALS) },
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
                    navController.navigate(Graph.AUTH) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onApiTest = {navController.navigate(AppScreen.API_TEST)}
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
    }


}
