plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.github.dcendents.android-maven")
}

group="com.github.transferwise"

android {
    compileSdkVersion(Versions.SDK)

    defaultConfig {
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.SDK)
        versionCode = 11
        versionName = "1.0.11"
        setProperty("archivesBaseName", "com.transferwise.sequencelayout-${versionName}")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}")
    implementation("androidx.appcompat:appcompat:${Versions.ANDROIDX_APPCOMPAT}")
    implementation("androidx.core:core-ktx:${Versions.ANDROIDX_CORE_KTX}")
}
