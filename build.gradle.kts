plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
//    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
//    alias(libs.plugins.kotlin.parcelize) apply false
//    alias(libs.plugins.kotlin.ksp) apply false
}
buildscript {
    dependencies {
        classpath("com.facebook.react:react-native-gradle-plugin")
    }
}