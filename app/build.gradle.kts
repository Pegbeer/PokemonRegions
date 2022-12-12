plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("kapt")
    id("com.apollographql.apollo3").version("3.7.2")
    kotlin("plugin.serialization").version("1.7.10")

}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.example.pokemonregions"
        minSdk = 22
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release"){
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    val lottieVersion = "5.2.0"
    val lifecycleVersion = "2.5.1"
    val firebaseVersion = "21.1.0"
    val retrofitVersion = "2.9.0"
    val hiltVersion = "2.44.2"
    val apolloVersion = "3.7.2"

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.activity:activity-ktx:1.5.1")
    implementation("androidx.fragment:fragment-ktx:1.5.5")

    //LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //Google Services
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.3.2")
    implementation("com.google.firebase:firebase-analytics-ktx:$firebaseVersion")
    implementation("com.google.firebase:firebase-auth-ktx:$firebaseVersion")
    implementation("com.google.firebase:firebase-database-ktx:20.1.0")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("com.facebook.android:facebook-android-sdk:15.0.2")

    //Dependency Injection
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    //CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //Lottie
    implementation("com.airbnb.android:lottie:$lottieVersion")

    //Apollo
    implementation("com.apollographql.apollo3:apollo-runtime:$apolloVersion")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

kapt{
    correctErrorTypes = true
}

apollo{
    packageName.set("com.example")
}