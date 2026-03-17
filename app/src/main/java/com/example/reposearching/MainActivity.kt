package com.example.reposearching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 將 innerPadding 應用在 SearchScreen 上，避免內容被狀態列或導覽列遮住
                    SearchScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
