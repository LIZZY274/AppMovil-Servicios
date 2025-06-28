package com.example.myautotrackfinal.core.navigation

sealed class Screens(val route: String) {
    object Login : Screens("login_screen")
    object Register : Screens("register_screen")
    object Home : Screens("home_screen")
    object AddService : Screens("add_service_screen")
    object ViewServices : Screens("view_services_screen")
    object UpdateService : Screens("update_service_screen/{serviceId}") {
        fun createRoute(serviceId: String) = "update_service_screen/$serviceId"
    }
}