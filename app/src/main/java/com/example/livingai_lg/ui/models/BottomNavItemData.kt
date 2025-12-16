package com.example.livingai_lg.ui.models

import com.example.livingai_lg.R
import com.example.livingai_lg.ui.navigation.AppScreen

data class BottomNavItemData(
    val label: String,
    val iconRes: Int,
    val route: String,
)

val mainBottomNavItems = listOf(
    BottomNavItemData("Home", R.drawable.ic_home , AppScreen.BUY_ANIMALS),
    BottomNavItemData("Sell", R.drawable.ic_tag, AppScreen.CREATE_ANIMAL_LISTING),
    // TODO:
    BottomNavItemData("Chats", R.drawable.ic_chat, AppScreen.CREATE_PROFILE),
    BottomNavItemData("Services", R.drawable.ic_config, AppScreen.CREATE_PROFILE),
    BottomNavItemData("Mandi", R.drawable.ic_market, AppScreen.CREATE_PROFILE)
)

val chatBottomNavItems = listOf(
    BottomNavItemData("Home", R.drawable.ic_home ,"home"),
    BottomNavItemData("Sell", R.drawable.ic_tag, "sell"),
    BottomNavItemData("Chats", R.drawable.ic_chat, "chats"),
    BottomNavItemData("Services", R.drawable.ic_config, "services"),
    BottomNavItemData("Mandi", R.drawable.ic_market, "mandi")
)
