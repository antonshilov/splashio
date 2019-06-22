plugins {
  id("com.android.library")
  id("kotlin-android")
}
android {
  compileSdkVersion(28)
  defaultConfig {
    minSdkVersion(21)
    targetSdkVersion(28)
    versionCode = 1
    versionName = "1.0"
    testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }

dependencies {
  implementation(Deps.kotlin_stdlib)
  implementation(Deps.design)
  testImplementation(Deps.junit_jupiter)
  testImplementation(Deps.junit_params)
}
