plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(Versions.SDK)
    defaultConfig {
        applicationId = "com.transferwise.sequencelayout.sample"
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.SDK)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":sequencelayout"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}")
    implementation("androidx.appcompat:appcompat:${Versions.ANDROIDX_APPCOMPAT}")
}
