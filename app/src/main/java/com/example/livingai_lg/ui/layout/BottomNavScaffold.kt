package com.example.livingai_lg.ui.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.zIndex
import com.example.livingai_lg.ui.components.BottomNavigationBar
import com.example.livingai_lg.ui.models.BottomNavItemData

@Composable
fun BottomNavScaffold(
    modifier: Modifier = Modifier,
    items: List<BottomNavItemData>,
    currentItem: String,
    onBottomNavItemClick: (route: String) -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier
                    .zIndex(1f)
                    .shadow(8.dp),
                items,
                currentItem,
                onItemClick = onBottomNavItemClick
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        content(paddingValues)
    }
}
