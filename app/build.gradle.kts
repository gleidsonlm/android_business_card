plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.gleidsonlm.businesscard"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.gleidsonlm.businesscard"
        minSdk = 34
        targetSdk = 37
        versionCode = 1754478577
        versionName = "November 11, 2026"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
    }
    
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    
    lint {
        // Disable SelectedPhotoAccess warning - app already uses modern photo picker
        disable += "SelectedPhotoAccess"
        // Disable IconXmlAndPng warning - it's common to have both adaptive and fallback icons
        disable += "IconXmlAndPng"
    }
}

dependencies {

    implementation(files("libs/Appdome_com.playdigital.devicebinding_c49e7748.aar"))
    implementation(libs.gson)
    implementation(libs.googlecode.ez.vcard)
    implementation("com.journeyapps:zxing-android-embedded:4.3.0") {
        exclude(group = "com.android.support")
    }
    implementation(libs.core)
    implementation(libs.coil.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    baselineProfile(project(":baselineprofile"))
    ksp(libs.hilt.android.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.rules)
    testImplementation(libs.androidx.runner)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

tasks.register<Zip>("exportBaselineProfileForAppdome") {
    group = "appdome"
    description = "Exports and zips the baseline profile for Appdome fusion."
    
    archiveFileName.set("baseline_profile.zip")
    destinationDirectory.set(layout.buildDirectory.dir("outputs/appdome"))

    // Use the merged profile from the build directory as the primary source
    val mergedProfile = layout.buildDirectory.file("intermediates/merged_art_profile/release/mergeReleaseArtProfile/baseline-prof.txt")
    
    from(mergedProfile) {
        rename { "baseline_profile.txt" }
    }
    
    // Fallback: If the profile has been copied to src/main
    from(layout.projectDirectory.file("src/main/baseline-prof.txt")) {
        rename { "baseline_profile.txt" }
    }
}
