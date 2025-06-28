package com.example.myautotrackfinal.core.appcontext

import android.app.Application

object AppContextHolder {
    lateinit var application: Application
        private set

    fun init(app: Application) {
        application = app
    }
}