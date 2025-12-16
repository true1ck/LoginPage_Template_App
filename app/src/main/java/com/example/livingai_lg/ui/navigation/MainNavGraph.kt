package com.example.livingai_lg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.livingai_lg.ui.login.SuccessScreen
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

@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val onNavClick: (String) -> Unit = { route ->
        val currentRoute =
            navController.currentBackStackEntry?.destination?.route

        if (currentRoute != route) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppScreen.BUY_ANIMALS
    ) {
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


        composable(AppScreen.CHOOSE_SERVICE) {
            ChooseServiceScreen (
                onServiceSelected = { navController.navigate(AppScreen.LANDING) },
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
                onFilterClick = {navController.navigate(AppScreen.BUY_ANIMALS_FILTERS)},
                onSortClick = {navController.navigate(AppScreen.BUY_ANIMALS_SORT)},
                onSellerClick = { sellerId ->
                    navController.navigate(
                        AppScreen.sellerProfile(sellerId)
                    )
                },
            )
        }

        composable(AppScreen.BUY_ANIMALS_FILTERS) {
            FilterScreen(
                onSubmitClick = {navController.navigate(AppScreen.BUY_ANIMALS)},
                onBackClick = {
                    navController.popBackStack()
                },
                onSkipClick = {
                    navController.popBackStack()
                },
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
                onSkipClick = {
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                },

                )
        }

        composable(AppScreen.CREATE_ANIMAL_LISTING) {
            NewListingScreen (
                onSaveClick = {navController.navigate(
                    AppScreen.postSaleSurvey("2")
                )},
                onBackClick = {
                    navController.popBackStack()
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
                    navController.popBackStack()
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
        }}
}
