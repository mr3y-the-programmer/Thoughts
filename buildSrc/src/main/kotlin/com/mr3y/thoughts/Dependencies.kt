package com.mr3y.thoughts

object Versions {
    const val Compose = "1.0.0-beta01"
    const val Kotlin = "1.4.30"
}

object Dependencies {

    object Kotlin {
        const val Reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.Kotlin}"
    }

    object AndroidX {
        const val Core_Ktx = "androidx.core:core-ktx:1.3.2"
        const val Appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val Material = "com.google.android.material:material:1.3.0"
        const val Lifecycle_Ktx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.0"
    }

    object Compose {
        const val UI = "androidx.compose.ui:ui:${Versions.Compose}"
        const val Material = "androidx.compose.material:material:${Versions.Compose}"
        const val Tooling = "androidx.compose.ui:ui-tooling:${Versions.Compose}"
        const val Activity_Compose = "androidx.activity:activity-compose:${Versions.Compose}"
        const val ConstraintLayout_Compose = "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha02"
    }

    object Testing {
        const val JUNIT = "junit:junit:4.13.2"

        object AndroidX {
            const val JUNIT = "androidx.test.ext:junit:1.1.2"
            const val Espresso = "androidx.test.espresso:espresso-core:3.3.0"
        }
    }
}