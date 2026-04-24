plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// 火山引擎全埋点插桩插件（自动采集点击、页面浏览等事件）
apply(plugin = "com.bytedance.std.tracker")

android {
    namespace = "io.dcloud.H576E6CC7"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.dcloud.H576E6CC7"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        // 火山引擎 AppLog SDK 需要的 manifest placeholder
        manifestPlaceholders["APPLOG_SCHEME"] = "rangersapplog.c7ece5852a45e0db"
    }

    signingConfigs {
        create("yp") {
            storeFile = file("HBuilder.jks")
            storePassword = "123456"
            keyAlias = "hbuilder"
            keyPassword = "123456"
            isV1SigningEnabled = true
            isV2SigningEnabled = true
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("yp")
        }

        create("pre") {
            initWith(getByName("debug"))
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("yp")
            matchingFallbacks += listOf("pre", "debug", "release")
        }

        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("yp")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources {
            excludes += "META-INF/*.kotlin_module"
        }
    }
}

configurations.all {
    exclude(group = "com.android.support", module = "support-compat")
    exclude(group = "com.android.support", module = "support-annotations")
    exclude(group = "com.android.support", module = "support-core-utils")
    exclude(group = "com.android.support", module = "support-v4")
    exclude(group = "com.android.support", module = "versionedparcelable")
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // ========== 埋点库 ==========
    implementation("com.yupao.pointer:buried_point:2.4.0")
}
