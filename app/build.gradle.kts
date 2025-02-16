plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.mobile.app.lincride"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mobile.app.lincride"
        minSdk = 25
        //noinspection OldTargetApi
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
        viewBinding = true
    }

    kapt{
        correctErrorTypes = true
    }

    android.buildFeatures.buildConfig = true

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.dagger.hilt)
    implementation(libs.play.services.maps)
    implementation(libs.gms.play.services.location)
    implementation(libs.google.places)
    implementation(libs.google.maps.ktx)
    implementation(libs.google.maps.utility.ktx)
    implementation(libs.androidx.lifecycle.runtime.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    kapt(libs.kapt.hilt.compiler)
    ksp(libs.ksp.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.kotlin.room.runtime)
    testImplementation(libs.junit5)
    testImplementation(libs.test.coroutine)
    testImplementation(libs.test.mockito.core)
    testImplementation(libs.test.mockito.junit5)
    testImplementation(libs.test.google.truth)
    androidTestImplementation(libs.test.mockito.kotlin)
    androidTestImplementation(libs.test.coroutine)
    androidTestImplementation(libs.test.google.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

secrets {

    propertiesFileName = "secrets.properties"

    defaultPropertiesFileName = "local.defaults.properties"

}

