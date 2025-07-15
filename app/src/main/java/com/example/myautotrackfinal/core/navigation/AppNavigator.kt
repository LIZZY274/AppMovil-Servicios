package com.example.myautotrackfinal.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myautotrackfinal.features.home.presentation.HomeScreen
import com.example.myautotrackfinal.features.login.presentation.LoginScreen
import com.example.myautotrackfinal.features.register.presentation.RegisterScreen
import com.example.myautotrackfinal.features.service.presentation.AddServiceScreen
import com.example.myautotrackfinal.features.service.presentation.UpdateServiceScreen
import com.example.myautotrackfinal.features.service.presentation.ViewServicesScreen
import com.example.myautotrackfinal.features.service.presentation.ServiceViewMode
// NUEVA IMPORTACIÓN PARA SERVICIOS CERCANOS
import com.example.myautotrackfinal.features.nearby.presentation.NearbyServicesScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        //  AUTENTICACIÓN

        composable(Screens.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screens.Register.route) {
            RegisterScreen(navController = navController)
        }

        composable(Screens.Home.route) {
            HomeScreen(navController = navController)
        }

        //  GESTIÓN DE SERVICIOS

        // Agregar nuevo servicio
        composable(Screens.AddService.route) {
            AddServiceScreen(navController = navController)
        }

        composable(Screens.ViewServices.route) {
            ViewServicesScreen(
                navController = navController,
                viewMode = ServiceViewMode.VIEW_ONLY
            )
        }

        // Ver servicios
        composable(
            route = Screens.ViewServicesWithMode.route,
            arguments = listOf(
                navArgument("viewMode") {
                    type = NavType.StringType
                    defaultValue = "VIEW_ONLY"
                }
            )
        ) { backStackEntry ->
            val viewModeString = backStackEntry.arguments?.getString("viewMode") ?: "VIEW_ONLY"
            val viewMode = when (viewModeString) {
                "VIEW_ONLY" -> ServiceViewMode.VIEW_ONLY
                "EDIT_ONLY" -> ServiceViewMode.EDIT_ONLY
                "DELETE_ONLY" -> ServiceViewMode.DELETE_ONLY
                else -> ServiceViewMode.VIEW_ONLY
            }

            ViewServicesScreen(
                navController = navController,
                viewMode = viewMode
            )
        }

        // Actualizar servicio específico
        composable(
            route = Screens.UpdateService.route,
            arguments = listOf(
                navArgument("serviceId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")
            if (serviceId != null) {
                UpdateServiceScreen(
                    navController = navController,
                    serviceId = serviceId
                )
            } else {
                navController.popBackStack()
            }
        }

        // Servicios cercanos
        composable(Screens.NearbyServices.route) {
            NearbyServicesScreen(navController = navController)
        }
    }
}