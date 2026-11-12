plugins {
    alias(libs.plugins.android.test)
    // Required while android.builtInKotlin=false, otherwise the .kt sources are never compiled.
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.gleidsonlm.businesscard.baselineprofile"
    compileSdk = 37

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    defaultConfig {
        minSdk = 31
        targetSdk = 37

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"

    testOptions {
        managedDevices {
            allDevices {
                create<com.android.build.api.dsl.ManagedVirtualDevice>("pixel6Api34") {
                    device = "Pixel 6"
                    apiLevel = 34
                    systemImageSource = "aosp"
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.test.junit4)
}

baselineProfile {
    // Generate the baseline profile during the 'release' build.
    // By default a Gradle managed Pixel 6 / API 34 emulator is used. To use a running emulator or a
    // rooted physical Pixel 6 instead, pass -Pbaselineprofile.useConnectedDevices=true
    val useConnected = providers.gradleProperty("baselineprofile.useConnectedDevices")
        .map { it.toBoolean() }
        .getOrElse(false)
    if (useConnected) {
        useConnectedDevices = true
    } else {
        managedDevices += "pixel6Api34"
        useConnectedDevices = false
    }
}
