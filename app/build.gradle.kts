import org.jetbrains.kotlin.config.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.dh.app.core"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dh.app.core"
        minSdk = 26
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidxCoreKtx)
    implementation(libs.androidxAppcompat)
    implementation(libs.material)
    implementation(libs.androidxActivity)
    implementation(libs.androidxConstraintLayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidxJUnit)
    androidTestImplementation(libs.androidxEspressoCore)

    implementation(project(":commons"))

}