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
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://storage.googleapis.com/download.flutter.io")
    }
}
plugins { id("com.facebook.react.settings") }
extensions.configure<com.facebook.react.ReactSettingsExtension> {
    autolinkLibrariesFromCommand(
        workingDirectory = settings.layout.rootDirectory.dir("./react").asFile,
        lockFiles = settings.layout.rootDirectory.dir("./react").files(
            "yarn.lock", "package-lock.json", "package.json", "react-native.config.js"
        )
    )
}
rootProject.name = "EbKit"
include(":app")
apply(from = File("${rootProject.projectDir}/flutter/.android/include_flutter.groovy"))
includeBuild("${rootProject.projectDir}/react/node_modules/@react-native/gradle-plugin")