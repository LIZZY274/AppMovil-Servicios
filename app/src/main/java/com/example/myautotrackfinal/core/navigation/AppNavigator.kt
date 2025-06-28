package com.example.myautotrackfinal.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myautotrackfinal.features.home.presentation.HomeScreen
import com.example.myautotrackfinal.features.login.presentation.LoginScreen
import com.example.myautotrackfinal.features.register.presentation.RegisterScreen
import com.example.myautotrackfinal.features.service.presentation.AddServiceScreen
import com.example.myautotrackfinal.features.service.presentation.UpdateServiceScreen
import com.example.myautotrackfinal.features.service.presentation.ViewServicesScreen

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
        composable(Screens.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screens.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Screens.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.AddService.route) {
            AddServiceScreen(navController = navController)
        }
        composable(Screens.ViewServices.route) {
            ViewServicesScreen(navController = navController)
        }
        composable(Screens.UpdateService.route) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")
            if (serviceId != null) {
                UpdateServiceScreen(navController = navController, serviceId = serviceId)
            } else {

                navController.popBackStack()
            }
        }
    }
}