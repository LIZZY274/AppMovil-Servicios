package com.example.myautotrackfinal

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.myautotrackfinal.core.appcontext.AppContextHolder
import com.example.myautotrackfinal.core.navigation.AppNavHost
import com.example.myautotrackfinal.core.navigation.Screens
import com.example.myautotrackfinal.core.datastore.TokenManager
import com.example.myautotrackfinal.ui.theme.MyAutoTrackFinalTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar contexto de la aplicación
        AppContextHolder.init(application)

        // Configurar destino inicial basado en token
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

                    // ============================================
                    // 🔑 MANEJO DE PERMISOS DE UBICACIÓN
                    // ============================================
                    val locationPermissions = rememberMultiplePermissionsState(
                        permissions = listOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )

                    // ============================================
                    // 📞 PERMISOS ADICIONALES (OPCIONAL)
                    // ============================================
                    val additionalPermissions = rememberMultiplePermissionsState(
                        permissions = listOf(
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.CAMERA
                        )
                    )

                    // Solicitar permisos de ubicación automáticamente
                    LaunchedEffect(Unit) {
                        if (!locationPermissions.allPermissionsGranted) {
                            locationPermissions.launchMultiplePermissionRequest()
                        }
                    }

                    AppNavHost(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}