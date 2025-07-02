package com.example.myautotrackfinal.features.home.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myautotrackfinal.core.di.AppModule

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = AppModule.provideTokenManager(application.applicationContext)

    fun logout() {
        tokenManager.deleteToken()
    }
}