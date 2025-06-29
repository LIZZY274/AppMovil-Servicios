plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.myautotrackfinal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myautotrackfinal"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    // ============================================
    // 🎯 DEPENDENCIAS PRINCIPALES DE ANDROID
    // ============================================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.material)

    // ============================================
    // 🧪 DEPENDENCIAS DE TESTING
    // ============================================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")


    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    implementation("androidx.compose.material:material-icons-extended:1.6.8")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")


    // 📷 CÁMARA

    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")


    // 🖼️ CARGA DE IMÁGENES

    implementation("io.coil-kt:coil-compose:2.5.0")

    // 🔑 PERMISOS EN TIEMPO DE EJECUCIÓN

    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

   
    // 📍 UBICACIÓN

    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Google Maps (opcional - para mapa real en el futuro)
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:maps-compose:4.3.3")

    // 📐 CÁLCULOS MATEMÁTICOS Y GEOGRÁFICOS

    // Para cálculos de distancia y coordenadas
    implementation("org.apache.commons:commons-math3:3.6.1")


    // Para formateo de fechas y horas
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")


    implementation("com.jakewharton.timber:timber:5.0.1")
}