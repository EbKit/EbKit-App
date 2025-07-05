plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.kotlin.parcelize)
//    alias(libs.plugins.kotlin.kapt)
//    alias(libs.plugins.kotlin.ksp)
}

kotlin {
    android {
        namespace = "io.ebkit.app"
        compileSdk = 36
        defaultConfig {
            applicationId = "io.ebkit.app"
            minSdk = 24
            targetSdk = 36
            versionCode = 1
            versionName = "1.0"
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            ndk {
                abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86_64")
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
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        kotlinOptions {
            jvmTarget = "11"
        }
        buildFeatures {
            aidl = true
            compose = true
            buildConfig = true
        }
    }
    sourceSets {
        main {
            dependencies {
                implementation(dependencyNotation = project(path = ":flutter"))
                implementation(dependencyNotation = project(path = ":flutter_boost"))

                implementation(dependencyNotation = libs.utilcodex)
                implementation(dependencyNotation = libs.shizuku.api)
                implementation(dependencyNotation = libs.shizuku.provider)
                implementation(dependencyNotation = libs.hiddenapibypass)
                implementation(dependencyNotation = libs.baseframework)
            }
        }
    }
}

dependencies {
    implementation("com.google.accompanist:accompanist-coil:0.15.0")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.play.services.base)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}