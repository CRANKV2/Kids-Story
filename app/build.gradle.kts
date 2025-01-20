plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.gigo.kidsstorys"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gigo.kidsstorys"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 5
        versionName = "1.2.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    val roomVersion = "2.6.1"
    
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    
    // Neue Dependencies
    //noinspection UseTomlInstead,GradleDependency
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    //noinspection UseTomlInstead,GradleDependency
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation(libs.androidx.navigation.compose.v277)

    // Implement Firebase for Login
    //noinspection GradleDependency
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.android.gms:play-services-auth:20.7.0' // Für Google Sign-In

}