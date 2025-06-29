package com.example.myautotrackfinal.core.di

import android.content.Context
import com.example.myautotrackfinal.core.network.AuthApi
import com.example.myautotrackfinal.core.network.ServiceApi
import com.example.myautotrackfinal.core.network.RetrofitClient
import com.example.myautotrackfinal.core.datastore.TokenManager
import com.example.myautotrackfinal.core.di.module.HardwareModule
import com.example.myautotrackfinal.core.hardware.domain.CameraRepository
import com.example.myautotrackfinal.core.hardware.data.VibrationManager
import com.example.myautotrackfinal.core.hardware.data.VibrationRepositoryImpl
import com.example.myautotrackfinal.core.hardware.domain.VibrationRepository
import com.example.myautotrackfinal.features.login.data.LoginRepository
import com.example.myautotrackfinal.features.login.domain.LoginUseCase
import com.example.myautotrackfinal.features.login.domain.repository.LoginRepositoryInterface
import com.example.myautotrackfinal.features.register.data.RegisterRepository
import com.example.myautotrackfinal.features.register.domain.RegisterUseCase
import com.example.myautotrackfinal.features.register.domain.repository.RegisterRepositoryInterface
import com.example.myautotrackfinal.features.service.data.ServiceRepository
import com.example.myautotrackfinal.features.service.domain.ServiceUseCase
import com.example.myautotrackfinal.features.service.domain.repository.ServiceRepositoryInterface
import com.example.myautotrackfinal.features.home.data.HomeRepository
import com.example.myautotrackfinal.features.home.domain.HomeUseCase

object AppModule {



    fun provideTokenManager(context: Context): TokenManager {
        return TokenManager(context)
    }

    fun provideAuthApi(context: Context): AuthApi {
        return RetrofitClient.createWithContext(context).create(AuthApi::class.java)
    }

    fun provideServiceApi(context: Context): ServiceApi {
        return RetrofitClient.createWithContext(context).create(ServiceApi::class.java)
    }



    fun provideCameraRepository(context: Context): CameraRepository {
        return HardwareModule.provideCameraRepository(context)
    }


    fun provideVibrationManager(context: Context): VibrationManager {
        return VibrationManager(context)
    }

    fun provideVibrationRepository(context: Context): VibrationRepositoryImpl {
        return VibrationRepositoryImpl(provideVibrationManager(context))
    }



    fun provideLoginRepository(context: Context): LoginRepositoryInterface {
        val authApi = provideAuthApi(context)
        return LoginRepository(authApi)
    }

    fun provideRegisterRepository(context: Context): RegisterRepositoryInterface {
        val authApi = provideAuthApi(context)
        return RegisterRepository(authApi)
    }

    fun provideServiceRepository(context: Context): ServiceRepositoryInterface {
        val serviceApi = provideServiceApi(context)
        return ServiceRepository(serviceApi)
    }

    fun provideHomeRepository(): HomeRepository {
        return HomeRepository()
    }



    fun provideLoginUseCase(context: Context): LoginUseCase {
        val repository = provideLoginRepository(context)
        return LoginUseCase(repository)
    }

    fun provideRegisterUseCase(context: Context): RegisterUseCase {
        val repository = provideRegisterRepository(context)
        return RegisterUseCase(repository)
    }

    fun provideServiceUseCase(context: Context): ServiceUseCase {
        val repository = provideServiceRepository(context)
        return ServiceUseCase(repository)
    }

    fun provideHomeUseCase(): HomeUseCase {
        val repository = provideHomeRepository()
        return HomeUseCase(repository)
    }
}