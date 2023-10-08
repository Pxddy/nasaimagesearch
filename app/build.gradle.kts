import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 34

    namespace = "com.ph.nasaimagesearch"

    defaultConfig {
        applicationId = "com.ph.nasaimagesearch"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()

            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=kotlin.time.ExperimentalTime",
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlin.ExperimentalStdlibApi"
            )
        }
    }

    hilt {
        enableAggregatingTask = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")

    val composeBom = platform("androidx.compose:compose-bom:2023.10.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.activity:activity-compose:1.7.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Compose Destination
    val composeDestination = "1.9.54"
    implementation("io.github.raamcosta.compose-destinations:core:$composeDestination")
    ksp("io.github.raamcosta.compose-destinations:ksp:$composeDestination")

    // Coroutines
    val coroutines = "1.7.3"

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    // Hilt
    val hilt = "2.48"

    implementation("com.google.dagger:hilt-android:$hilt")
    ksp("com.google.dagger:hilt-android-compiler:$hilt")

    // Lifecycle
    val lifecycle = "2.6.2"

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:6.1.0")

    // Moshi
    val moshi = "1.15.0"

    implementation("com.squareup.moshi:moshi:$moshi")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:$moshi")

    // Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // Paging
    val paging = "3.2.1"

    implementation("androidx.paging:paging-runtime:$paging")
    implementation("androidx.paging:paging-compose:$paging")

    // Retrofit
    val retrofit = "2.9.0"

    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")
}