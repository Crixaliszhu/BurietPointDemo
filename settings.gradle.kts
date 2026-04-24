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
        maven {
            isAllowInsecureProtocol = true
            url = uri("https://repository.vrtbbs.com/repository/maven-public/")
        }
        // 火山引擎 SDK
        maven { url = uri("https://artifact.bytedance.com/repository/Volcengine/") }
        maven { url = uri("https://artifact.bytedance.com/repository/pangle") }
    }
}

rootProject.name = "recruitment_android_demo"
include(":app")
