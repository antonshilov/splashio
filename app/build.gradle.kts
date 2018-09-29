import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-android-extensions")
  id("io.fabric")
}
// workaround for the bug in kotlin plugin, taken from https://github.com/gradle/kotlin-dsl/issues/644#issuecomment-398502551
androidExtensions {
  configure(delegateClosureOf<AndroidExtensionsExtension> {
    isExperimental = true
  })
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
  packagingOptions {
    exclude("META-INF/DEPENDENCIES")
    exclude("META-INF/LICENSE")
    exclude("META-INF/LICENSE.txt")
    exclude("META-INF/license.txt")
    exclude("META-INF/NOTICE")
    exclude("META-INF/NOTICE.txt")
    exclude("META-INF/notice.txt")
    exclude("META-INF/ASL2.0")
    exclude("META-INF/proguard/androidx-annotations.pro")
  }
}

dependencies {
  implementation(project(":domain"))
  implementation(project(":remote"))

  implementation(Deps.kotlin_stdlib)
  implementation(Deps.support_v4)
  implementation(Deps.appcompat_v7)
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

  implementation(Deps.permissions_dispatcher) {
    // if you don"t use android.app.Fragment you can exclude support for them
    exclude(module = "support-v13")
  }
  kapt(Deps.permissions_dispatcher_processor)

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
