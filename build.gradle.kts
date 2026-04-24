// Top-level build file
plugins {
    id("com.android.application") version "8.10.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://artifact.bytedance.com/repository/Volcengine/") }
        maven { url = uri("https://artifact.bytedance.com/repository/pangle") }
    }
    dependencies {
        // 火山引擎全埋点插桩插件
        classpath("com.bytedance.applog:RangersAppLog-All-plugin-agp8:6.16.10")
    }
}
