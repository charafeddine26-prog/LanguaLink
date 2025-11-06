plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.compose) // <-- 新增
}

android {
    namespace = "com.example.langualink"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.langualink"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // 为 Compose 添加 vector drawable 支持
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    // --- 新增的部分 ---
    buildFeatures {
        // 启用 Jetpack Compose
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    // ------------------
}

dependencies {
    // 您已有的依赖
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    // 注意：我们将使用 activity-compose 替换 activity
    // implementation(libs.androidx.activity)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.datastore.preferences)

    // --- 新增的 Compose 依赖 ---
    // Jetpack Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI 核心库
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3) // 使用最新的 Material 3 设计

    // Activity Compose 集成
    implementation("androidx.activity:activity-compose:1.9.0") // 明确指定 activity-compose 版本

    // Compose 调试工具 (只在 debug 构建时使用)
    debugImplementation(libs.androidx.compose.ui.tooling)
    // ... (在 dependencies 代码块的内部)
    implementation(libs.androidx.compose.material.icons.extended) // <-- 新增这一行
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    // -------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // iTextPDF
    implementation("com.itextpdf.android:kernel-android:8.0.4")
    implementation("com.itextpdf.android:layout-android:8.0.4")
}