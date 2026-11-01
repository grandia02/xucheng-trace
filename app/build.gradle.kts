plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
    namespace = "com.xucheng.trace"
    compileSdk = 35
    defaultConfig { applicationId = "com.xucheng.trace"; minSdk = 29; targetSdk = 35; versionCode = 6; versionName = "0.1.0-f006" }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
