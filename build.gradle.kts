// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("com.github.dcendents:android-maven-gradle-plugin:${Versions.ANDROID_MAVEN_GRADLE_PLUGIN}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}