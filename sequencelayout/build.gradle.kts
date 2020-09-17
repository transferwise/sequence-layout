plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.github.dcendents.android-maven")
}

android {
    compileSdkVersion(Versions.SDK)

    defaultConfig {
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.SDK)
        versionCode = 13
        versionName = "1.1.1"
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

group = "com.github.transferwise"
version = android.defaultConfig.versionName.orEmpty()

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}")
    implementation("androidx.appcompat:appcompat:${Versions.ANDROIDX_APPCOMPAT}")
    implementation("androidx.core:core-ktx:${Versions.ANDROIDX_CORE_KTX}")
}
