package com.example.myautotrackfinal.core.navigation

sealed class Screens(val route: String) {

    object Login : Screens("login_screen")
    object Register : Screens("register_screen")

    object Home : Screens("home_screen")


    object AddService : Screens("add_service_screen")

    object ViewServices : Screens("view_services_screen")

    object ViewServicesWithMode : Screens("view_services_screen/{viewMode}") {
        fun createRoute(viewMode: String) = "view_services_screen/$viewMode"

        fun viewOnly() = "view_services_screen/VIEW_ONLY"
        fun editOnly() = "view_services_screen/EDIT_ONLY"
        fun deleteOnly() = "view_services_screen/DELETE_ONLY"
    }


    object UpdateService : Screens("update_service_screen/{serviceId}") {
        fun createRoute(serviceId: String) = "update_service_screen/$serviceId"
    }

    object NearbyServices : Screens("nearby_services_screen")
}