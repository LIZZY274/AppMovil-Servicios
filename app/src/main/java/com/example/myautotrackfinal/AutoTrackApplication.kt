package com.example.myautotrackfinal

import android.app.Application
import com.example.myautotrackfinal.core.di.AppModule

class AutoTrackApplication : Application() {

    override fun onCreate() {
        super.onCreate()


        AppModule.provideAppDatabase(this)
    }
}