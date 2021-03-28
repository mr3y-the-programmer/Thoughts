import com.mr3y.thoughts.MainCoordinates
import com.mr3y.thoughts.Versions
import com.mr3y.thoughts.Dependencies as Deps

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(MainCoordinates.CompileSdkVersion)
    buildToolsVersion = MainCoordinates.BuildToolsVersion

    defaultConfig {
        applicationId = MainCoordinates.App_ID
        minSdkVersion(MainCoordinates.MinSdkVersion)
        targetSdkVersion(MainCoordinates.TargetSdkVersion)
        versionCode = MainCoordinates.VersionCode
        versionName = MainCoordinates.VersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
        useIR = true
        freeCompilerArgs += "-Xinline-classes"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose
    }
}

dependencies {
    implementation(Deps.AndroidX.Core_Ktx)
    implementation(Deps.AndroidX.Appcompat)
    implementation(Deps.AndroidX.Material)
    implementation(Deps.AndroidX.Lifecycle_Ktx)
    implementation(Deps.Compose.UI)
    implementation(Deps.Compose.Material)
    implementation(Deps.Compose.Tooling)
    implementation(Deps.Compose.Activity_Compose)
    implementation(Deps.Compose.ConstraintLayout_Compose)
    // to use Layout Inspector with jetpack compose
    debugImplementation(Deps.Kotlin.Reflect)
    testImplementation(Deps.Testing.JUNIT)
    androidTestImplementation(Deps.Testing.AndroidX.JUNIT)
    androidTestImplementation(Deps.Testing.AndroidX.Espresso)
}
