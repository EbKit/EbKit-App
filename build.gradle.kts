plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}
buildscript {
    dependencies {
        // React Native Gradle Plugin (旧版Gradle插件)
        classpath(libs.facebook.react.rngp)
    }
}