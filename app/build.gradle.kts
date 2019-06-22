plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-android-extensions")
  id("io.fabric")
}
androidExtensions {
  isExperimental = true
}
android {
  compileSdkVersion(28)
  defaultConfig {
    applicationId = "io.github.antonshilov.splashio"
    minSdkVersion(21)
    targetSdkVersion(28)
    versionCode = 1
    versionName = "1.0"
    testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}

dependencies {
  implementation(project(":domain"))
  implementation(project(":remote"))
  implementation(project(":widgets"))

  implementation(Deps.kotlin_stdlib)
  implementation(Deps.design)
  implementation(Deps.recyclerview_v7)
  implementation(Deps.cardview_v7)

  implementation(Deps.constraint_layout)

  implementation(Deps.core_ktx)
  implementation(Deps.navigation_fragment_ktx)
  implementation(Deps.navigation_ui_ktx)

  implementation(Deps.work)

  implementation(Deps.lifecycle_extensions)
  kapt(Deps.lifecycle_compiler)

  implementation(Deps.paging)
  implementation("android.arch.paging:rxjava2:1.0.1")

  implementation(Deps.retrofit)
  implementation(Deps.gson_converter)
  implementation(Deps.okhttp)
  implementation(Deps.logging_interceptor)

  implementation(Deps.rxkotlin)
  implementation(Deps.rxandroid)

  implementation(Deps.koin_arch)
  implementation(Deps.koin)
  implementation(Deps.koin_Android)

  implementation(Deps.glide)
  kapt(Deps.glide_compiler)
  implementation(Deps.timber)

  implementation(Deps.photo_view)

  implementation("com.crashlytics.sdk.android:crashlytics:2.9.5@aar") {
    isTransitive = true
  }
  testImplementation(Deps.junit)
  androidTestImplementation(Deps.test_runner)
  androidTestImplementation(Deps.espresso)
}
