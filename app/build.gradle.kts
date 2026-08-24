plugins {
    alias(libs.plugins.android.application)
}

android {
<<<<<<< HEAD
    namespace = "com.example.exp3"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.exp3"
        minSdk = 24
        targetSdk = 37
=======
    namespace = "com.example.exp1"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.exp1"
        minSdk = 24
        targetSdk = 36
>>>>>>> c8609e24304c3219bf2274bc643369e99d486488
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
<<<<<<< HEAD
    implementation(libs.androidx.slidingpanelayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
=======
>>>>>>> c8609e24304c3219bf2274bc643369e99d486488
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}