package com.example.livingai_lg.ui.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import com.example.livingai_lg.R
import com.example.livingai_lg.ui.navigation.AppScreen

data class BottomNavItemData(
    val label: String,
    val iconRes: Any,
    val route: String,
)

val mainBottomNavItems = listOf(
    BottomNavItemData("Home", R.drawable.ic_home , AppScreen.BUY_ANIMALS),
    BottomNavItemData("Sell", R.drawable.ic_tag, AppScreen.CREATE_ANIMAL_LISTING),
    // TODO:
    BottomNavItemData("Chats", R.drawable.ic_chat, AppScreen.CHATS),
    BottomNavItemData("Services", R.drawable.ic_config, AppScreen.CREATE_PROFILE),
    BottomNavItemData("Mandi", R.drawable.ic_shop2, AppScreen.CREATE_PROFILE)
)

val chatBottomNavItems = listOf(
    BottomNavItemData("Contacts", R.drawable.ic_home ,AppScreen.CONTACTS),
    BottomNavItemData("Calls", R.drawable.ic_tag, AppScreen.CALLS),
    BottomNavItemData("Chats", R.drawable.ic_chat, AppScreen.CHATS),
 )
