import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.24"
    `maven-publish`
}


group = "com.dh"
version = "1.0.0"

android {
    namespace = "com.dh.app.core"

    compileSdk = libs.versions.app.build.compileSDKVersion.get().toInt()

    defaultConfig {
        minSdk = libs.versions.app.build.minimumSDK.get().toInt()
        vectorDrawables.useSupportLibrary = true
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    publishing {
        singleVariant("release") {}
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    compileOptions {
        val currentJavaVersionFromLibs =
            JavaVersion.valueOf(libs.versions.app.build.javaVersion.get())
        sourceCompatibility = currentJavaVersionFromLibs
        targetCompatibility = currentJavaVersionFromLibs
    }

    // Compose compiler version aligned with Kotlin 1.9.24
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    tasks.withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(
            JvmTarget.fromTarget(project.libs.versions.app.build.kotlinJVMTarget.get())
        )
        compilerOptions.freeCompilerArgs.set(
            listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-opt-in=com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi",
                "-Xcontext-receivers"
            )
        )
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = true
        warningsAsErrors = false
        baseline = file("lint-baseline.xml")
        lintConfig = rootProject.file("lint.xml")
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
}

publishing.publications {
    create<MavenPublication>("release") {
        afterEvaluate {
            from(components["release"])
        }
    }
}

detekt {
    baseline = file("detekt-baseline.xml")
    config.setFrom("$rootDir/detekt.yml")
    buildUponDefaultConfig = true
    allRules = false
}

dependencies {

    // Kotlin
    implementation(libs.kotlinxSerializationJson)
    implementation(libs.kotlinxCollectionsImmutable)

    // AndroidX
    implementation(libs.androidxConstraintLayout)
    implementation(libs.androidxDocumentFile)
    implementation(libs.androidxSwipeRefreshLayout)
    implementation(libs.androidxExifInterface)
    implementation(libs.androidxBiometricKtx)
    implementation(libs.androidxLifecycleRuntimeCompose)
    implementation(libs.androidxLifecycleProcess)
    implementation(libs.androidxActivityCompose)

    // Compose
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.composePreview)
    implementation(libs.composeUiViewBinding)
    implementation(libs.composeMaterialIconsExtended)

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    // API
    api(libs.jodaTime)
    api(libs.reprint)
    api(libs.androidxCoreKtx)
    api(libs.androidxAppcompat)
    api(libs.material)
    api(libs.gson)

    // Glide
    implementation("com.github.bumptech.glide:compose:1.0.0-beta08")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Room
    api(libs.androidxRoomRuntime)
    api(libs.androidxRoomKtx)
    ksp(libs.androidxRoomCompiler)

    // Detekt
    detektPlugins(libs.composeDetekt)

    // Other
    implementation("com.googlecode.ez-vcard:ez-vcard:0.12.1")
}
