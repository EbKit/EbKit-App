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

// Flutter
apply(
    from = rootProject.projectDir.resolve(
        relative = ".android/include_flutter.groovy"
    )
)

rootProject.name = "EbKit"
include(":app")