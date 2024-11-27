plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "br.com.api_consumption"
    compileSdk = 34

    defaultConfig {
        applicationId = "br.com.api_consumption"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.github.marcoscgdev:CurrencyEditText:1.0.1")
    implementation("io.github.vicmikhailau:MaskedEditText:5.0.3")

    implementation(libs.retrofit)
    implementation(libs.converter.gson) // Para converter JSON para objetos
    implementation(libs.logging.interceptor) // Opcional para log de requisições
}