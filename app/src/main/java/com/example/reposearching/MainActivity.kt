package com.example.reposearching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reposearching.ui.navigation.AppBottomNavigation
import com.example.reposearching.ui.navigation.Screen
import com.example.reposearching.ui.screens.BookmarkScreen
import com.example.reposearching.ui.screens.SearchScreen
import com.example.reposearching.ui.theme.RepoSearchingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepoSearchingTheme {
                val navController = rememberNavController()
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { AppBottomNavigation(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Search.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Search.route) {
                            SearchScreen()
                        }
                        composable(Screen.Bookmark.route) {
                            BookmarkScreen()
                        }
                    }
                }
            }
        }
    }
}
