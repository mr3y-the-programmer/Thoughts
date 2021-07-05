import com.mr3y.thoughts.MainCoordinates
import com.mr3y.thoughts.Versions
import com.mr3y.thoughts.Dependencies as Deps

plugins {
    id("com.android.application")
    id("shot")
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

        testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
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

    sourceSets {
        getByName("androidTest") {
            java.srcDirs("src/shared/java")
        }
        getByName("test") {
            java.srcDirs("src/shared/java")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xinline-classes"
    }

    buildFeatures {
        compose = true
    }

    packagingOptions {
        resources {
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
        }
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
    testImplementation(Deps.Testing.Truth)
    testImplementation(Deps.Testing.Coroutine_Testing)
    androidTestImplementation(Deps.Compose.Test)
    androidTestImplementation(Deps.Testing.AndroidX.JUNIT)
    androidTestImplementation(Deps.Testing.AndroidX.Espresso)
}
