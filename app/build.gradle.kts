import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

val localProps = Properties().apply {
    load(File(rootProject.rootDir, "local.properties").inputStream())
}

val FB_APP_ID = localProps.getProperty("FACEBOOK_APP_ID") ?: ""
val FB_CLIENT_TOKEN = localProps.getProperty("FACEBOOK_CLIENT_TOKEN") ?: ""
val GOOGLE_WEB_CLIENT_ID = localProps.getProperty("GOOGLE_WEB_CLIENT_ID") ?: ""

android {
    namespace = "course.examples.cinepople"
    compileSdk = 36

    defaultConfig {
        applicationId = "course.examples.cinepople"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["FACEBOOK_APP_ID"] = FB_APP_ID
        manifestPlaceholders["FACEBOOK_CLIENT_TOKEN"] = FB_CLIENT_TOKEN
        manifestPlaceholders["GOOGLE_WEB_CLIENT_ID"] = GOOGLE_WEB_CLIENT_ID

        buildConfigField("String", "FACEBOOK_APP_ID", "\"$FB_APP_ID\"")
        buildConfigField("String", "FACEBOOK_CLIENT_TOKEN", "\"$FB_CLIENT_TOKEN\"")
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$GOOGLE_WEB_CLIENT_ID\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

    buildFeatures{
        viewBinding = true
        buildConfig = true
    }

}

dependencies {

    // Thư viện từ Version Catalog (libs)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)

    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation ("androidx.core:core-splashscreen:1.0.1")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar", "*.jar"))))
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.facebook.android:facebook-login:latest.release")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}