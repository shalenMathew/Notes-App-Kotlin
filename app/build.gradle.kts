plugins {

    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")

}

android {

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            // Add any other files that need to be excluded
        }
    }
    namespace = "com.example.notesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.notesapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }



    buildFeatures{
        viewBinding=true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // responsive
    implementation ("com.github.MrNouri:DynamicSizes:1.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:ksp:4.16.0")

    // room
    val room_version = "2.6.0"

    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    val lifecycle_version = "2.6.2"

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")


    implementation ("com.google.dagger:dagger:2.48.1")
    kapt ("com.google.dagger:dagger-compiler:2.48.1")

    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    implementation ("com.google.android.gms:play-services-auth:20.7.0")


    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.http-client:google-http-client-gson:1.42.3")
    implementation ("com.google.api-client:google-api-client-android:1.34.0")
    implementation ("com.google.apis:google-api-services-drive:v3-rev136-1.25.0")

}