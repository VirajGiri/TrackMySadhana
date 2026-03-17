import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

// ── Load keystore properties ──────────────────────────────────────────────
val keystorePropsFile = rootProject.file("keystore.properties")
val keystoreProps = Properties().also { props ->
    if (keystorePropsFile.exists()) props.load(keystorePropsFile.inputStream())
}

android {
    namespace = "com.virajgiri.trackmysadhana"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.virajgiri.trackmysadhana"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // ── Signing ───────────────────────────────────────────────────────────
    signingConfigs {
        create("release") {
            val ksFile = keystoreProps["storeFile"]?.toString()
            if (keystorePropsFile.exists() && ksFile != null &&
                !keystoreProps["storePassword"].toString().contains("CHANGE_ME")) {
                storeFile      = file(ksFile)
                storePassword  = keystoreProps["storePassword"].toString()
                keyAlias       = keystoreProps["keyAlias"].toString()
                keyPassword    = keystoreProps["keyPassword"].toString()
            }
        }
    }

    buildTypes {
        release {
            // ── R8 / ProGuard ─────────────────────────────────────────────
            isMinifyEnabled    = true
            isShrinkResources  = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val releaseSigning = signingConfigs.getByName("release")
            if (releaseSigning.storeFile != null) {
                signingConfig = releaseSigning
            }
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix   = "-debug"
        }
    }

    // ── App Bundle splits (for Play Store) ───────────────────────────────
    bundle {
        language { enableSplit = true }
        density  { enableSplit = true }
        abi      { enableSplit = true }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig  = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    // Splash Screen
    implementation(libs.androidx.splashscreen)
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    // Lifecycle ViewModel + LiveData
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    // Chart
    implementation(libs.mpandroidchart)
    // Fragment KTX
    implementation(libs.androidx.fragment.ktx)
    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
