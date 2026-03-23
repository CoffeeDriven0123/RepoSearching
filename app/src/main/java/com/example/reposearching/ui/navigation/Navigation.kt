package com.example.reposearching.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.reposearching.R

/** 定義應用程式中的導航路線 */
sealed class Screen(val route: String, val titleResId: Int, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Search : Screen("search", R.string.nav_search, Icons.Filled.Search)
    object Bookmark : Screen("bookmark", R.string.nav_bookmarks, Icons.Filled.Favorite)
}

val bottomNavItems = listOf(
    Screen.Search,
    Screen.Bookmark
)

/**
 * 底部導航列元件
 */
@Composable
fun AppBottomNavigation(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavItems.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = stringResource(screen.titleResId)) },
                label = { Text(stringResource(screen.titleResId)) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up 到 Start Destination 避免返回堆疊過深
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // 避免連續點擊產生多個實例
                        launchSingleTop = true
                        // 恢復之前的狀態
                        restoreState = true
                    }
                }
            )
        }
    }
}
