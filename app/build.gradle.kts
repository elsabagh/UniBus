plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.daggerHiltAndroid)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.unibus"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.unibus"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"

        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Lifecycle ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Firebase
    implementation(platform(libs.firebase.bom))
    // Add the dependencies for the Crashlytics and Analytics libraries
    // Add the dependency for the Firebase Authentication library
    implementation(libs.firebase.auth)
    // Declare the dependency for the Cloud Firestore library
    implementation(libs.firebase.firestore)
    // Add the dependency for the Performance Monitoring library
    implementation(libs.firebase.perf)
    // Add the dependency for the Remote Config library
    implementation(libs.firebase.config)
    //    Preferences DataStore
    implementation(libs.androidx.datastore.preferences)
    //    testing navigation
    androidTestImplementation(libs.androidx.navigation.testing)

    implementation(libs.androidx.work.runtime.ktx)

    // For location services
    implementation (libs.play.services.location)

    // Coil
    implementation(libs.coil.compose)

    // Extended Icons
    implementation(libs.androidx.material.icons.extended)


    implementation ("com.google.accompanist:accompanist-swiperefresh:0.27.0")

    implementation("androidx.datastore:datastore-preferences:1.1.3")

    implementation ("com.google.firebase:firebase-storage-ktx")

    //Google Services & Maps
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.maps.android:android-maps-utils:2.3.0")
    implementation ("com.google.maps.android:maps-compose:2.9.0")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.maps:google-maps-services:0.15.0")
    //Accompanist (Permission)
    implementation ("com.google.accompanist:accompanist-permissions:0.31.3-beta")

    implementation ("com.google.accompanist:accompanist-flowlayout:0.24.13-rc")
}