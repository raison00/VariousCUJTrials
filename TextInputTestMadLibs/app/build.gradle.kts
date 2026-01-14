//val org.gradle.accessors.dm.LibrariesForLibs.AndroidxLibraryAccessors.material3: Any

//val org.gradle.accessors.dm.LibrariesForLibs.AndroidxLibraryAccessors.navigation: Any

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.uxcompose.text_input_test_madlibs"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.uxcompose.text_input_test_madlibs"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}
// https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.material.icons.extended)
   // implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    //implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material3.window.size.class1)

}