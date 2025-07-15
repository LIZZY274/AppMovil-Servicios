package com.example.myautotrackfinal.core.di

import android.content.Context
import com.example.myautotrackfinal.core.database.AppDatabase
import com.example.myautotrackfinal.core.network.AuthApi
import com.example.myautotrackfinal.core.network.ServiceApi
import com.example.myautotrackfinal.core.network.RetrofitClient
import com.example.myautotrackfinal.core.datastore.TokenManager
import com.example.myautotrackfinal.core.di.module.HardwareModule
import com.example.myautotrackfinal.core.hardware.domain.CameraRepository
import com.example.myautotrackfinal.core.hardware.data.VibrationManager
import com.example.myautotrackfinal.core.hardware.data.VibrationRepositoryImpl
import com.example.myautotrackfinal.core.utils.ConnectivityUtil
import com.example.myautotrackfinal.data.repository.OfflineAuthRepositoryImpl
import com.example.myautotrackfinal.data.repository.OfflineServiceRepositoryImpl
import com.example.myautotrackfinal.domain.usecase.*
import com.example.myautotrackfinal.features.login.data.LoginRepository
import com.example.myautotrackfinal.features.login.domain.LoginUseCase
import com.example.myautotrackfinal.features.login.domain.repository.LoginRepositoryInterface
import com.example.myautotrackfinal.features.register.data.RegisterRepository
import com.example.myautotrackfinal.features.register.domain.RegisterUseCase
import com.example.myautotrackfinal.features.register.domain.repository.RegisterRepositoryInterface
import com.example.myautotrackfinal.features.service.data.ServiceRepository
import com.example.myautotrackfinal.features.service.domain.ServiceUseCase
import com.example.myautotrackfinal.features.service.domain.repository.ServiceRepositoryInterface

object AppModule {

    // ============================================
    // CORE DEPENDENCIES
    // ============================================
    fun provideTokenManager(context: Context): TokenManager {
        return TokenManager(context)
    }

    fun provideAuthApi(context: Context): AuthApi {
        return RetrofitClient.createWithContext(context).create(AuthApi::class.java)
    }

    fun provideServiceApi(context: Context): ServiceApi {
        return RetrofitClient.createWithContext(context).create(ServiceApi::class.java)
    }

    // ============================================
    // HARDWARE DEPENDENCIES
    // ============================================
    fun provideCameraRepository(context: Context): CameraRepository {
        return HardwareModule.provideCameraRepository(context)
    }

    fun provideVibrationManager(context: Context): VibrationManager {
        return VibrationManager(context)
    }

    fun provideVibrationRepository(context: Context): VibrationRepositoryImpl {
        return VibrationRepositoryImpl(provideVibrationManager(context))
    }

    // ============================================
    // REPOSITORIES ORIGINALES
    // ============================================
    fun provideLoginRepository(context: Context): LoginRepositoryInterface {
        return LoginRepository(provideAuthApi(context))
    }

    fun provideRegisterRepository(context: Context): RegisterRepositoryInterface {
        return RegisterRepository(provideAuthApi(context))
    }

    fun provideServiceRepository(context: Context): ServiceRepositoryInterface {
        return ServiceRepository(provideServiceApi(context))
    }

    // ============================================
    // USE CASES ORIGINALES
    // ============================================
    fun provideLoginUseCase(context: Context): LoginUseCase {
        return LoginUseCase(provideLoginRepository(context))
    }

    fun provideRegisterUseCase(context: Context): RegisterUseCase {
        return RegisterUseCase(provideRegisterRepository(context))
    }

    fun provideServiceUseCase(context: Context): ServiceUseCase {
        return ServiceUseCase(provideServiceRepository(context))
    }

    // ============================================
    // ROOM DEPENDENCIES
    // ============================================
    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    fun provideUserDao(context: Context) = provideAppDatabase(context).userDao()

    fun provideServiceDao(context: Context) = provideAppDatabase(context).serviceDao()

    fun provideConnectivityUtil(context: Context) = ConnectivityUtil(context)

    // ============================================
    // OFFLINE REPOSITORIES
    // ============================================
    fun provideOfflineAuthRepository(context: Context) =
        OfflineAuthRepositoryImpl(provideUserDao(context))

    fun provideOfflineServiceRepository(context: Context) =
        OfflineServiceRepositoryImpl(provideServiceDao(context))

    // ============================================
    // OFFLINE USE CASES
    // ============================================
    fun provideSaveServiceOfflineUseCase(context: Context) =
        SaveServiceOfflineUseCase(
            provideOfflineServiceRepository(context),
            provideConnectivityUtil(context)
        )

    fun provideGetOfflineServicesUseCase(context: Context) =
        GetOfflineServicesUseCase(provideOfflineServiceRepository(context))

    fun provideSyncServicesUseCase(context: Context) =
        SyncServicesUseCase(
            provideOfflineServiceRepository(context),
            provideServiceUseCase(context),
            provideConnectivityUtil(context)
        )
}