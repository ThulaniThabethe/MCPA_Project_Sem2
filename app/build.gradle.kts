plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mcpa_project_sem22"
    compileSdk = 36
    
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
        disable += setOf("Range", "UnusedResources", "IconMissingDensityFolder")
    }

    defaultConfig {
        applicationId = "com.example.mcpa_project_sem22"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.location)
    implementation(libs.osmdroid.android)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    
    // Performance monitoring
    implementation(libs.profileinstaller)
    implementation(libs.metrics.performance)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}