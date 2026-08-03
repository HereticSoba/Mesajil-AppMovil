import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if(localPropertiesFile.exists()){
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}
val mercadoPagoPublicKey = localProperties.getProperty("MERCADO_PAGO_PUBLIC_KEY") ?: ""

android {
    namespace = "com.mesajil.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mesajil.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "MERCADO_PAGO_PUBLIC_KEY", "\"$mercadoPagoPublicKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    //MercadoPago
    implementation(platform(libs.mercadopago.sdk.bom))
    implementation(libs.mercadopago.sdk.coreMethods)
    // Jetpack Compose - para que los componentes de MercadoPago funcionen
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    //AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)

    //MVVM
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)

    //RecyclerView
    implementation(libs.androidx.recyclerview)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Glide
    implementation(libs.glide)

    //SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)

    //CircleImageView
    implementation(libs.circleimageview)
    implementation(libs.vision.internal.vkp)

    //Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.logging.interceptor)
}