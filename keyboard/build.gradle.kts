plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    androidTarget()
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
}

android {
    namespace = "com.seiko.platenumberkeyboard"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
}