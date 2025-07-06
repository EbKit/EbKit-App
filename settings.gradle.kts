import com.facebook.react.ReactSettingsExtension

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("${rootProject.projectDir}/react/node_modules/@react-native/gradle-plugin")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // 必须使用 PREFER_SETTINGS
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io") // Jitpack
        maven(url = "https://storage.googleapis.com/download.flutter.io") // Flutter
    }
}

plugins {
    id("com.facebook.react.settings")
}

extensions.configure<ReactSettingsExtension> {
    autolinkLibrariesFromCommand(
        workingDirectory = rootProject.projectDir.resolve(relative = "react"),
        lockFiles = files(
            paths = arrayListOf(
                rootProject.projectDir.resolve("react/yarn.lock"),
                rootProject.projectDir.resolve("react/package-lock.json"),
                rootProject.projectDir.resolve("react/package.json"),
                rootProject.projectDir.resolve("react/react-native.config.js")
            ).toTypedArray()
        ),
    )
}

apply(from = File("${rootProject.projectDir}/flutter/.android/include_flutter.groovy"))
includeBuild("${rootProject.projectDir}/react/node_modules/@react-native/gradle-plugin")

rootProject.name = "EbKit"
include(":app")