pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Coding"
include(":app")
include(":core:core_ui")
include(":core:core_data")
include(":core:core_testing")
include(":features:default:default_presentation")
include(":features:default:default_domain")
include(":features:default:default_data")
