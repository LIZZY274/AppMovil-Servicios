package com.example.myautotrackfinal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.myautotrackfinal.core.navigation.AppNavHost
import com.example.myautotrackfinal.core.navigation.Screens
import com.example.myautotrackfinal.core.datastore.TokenManager
import com.example.myautotrackfinal.ui.theme.MyAutoTrackFinalTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager(this)
        val startDestination = if (tokenManager.getToken() != null) {
            Screens.Home.route
        } else {
            Screens.Login.route
        }

        setContent {
            MyAutoTrackFinalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    AppNavHost(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}