object Versions {
  const val kotlin = "1.3.40"
  const val android_plugin = "3.4.1"
  const val versions_plugin = "0.21.0"
  const val quality_tools_plugin = "0.18.0"
  const val navigation = "2.0.0"
  const val core_ktx = "1.2.0-alpha02"
  const val lifecycle = "2.2.0-alpha01"
  const val glide = "4.9.0"
  const val timber = "4.7.1"
  const val photo_view = "2.3.0"
  const val paging = "2.1.0"
  const val retrofit = "2.5.0"
  const val okhttp = "3.11.0"
  const val junit = "4.12"
  const val junit5 = "5.4.2"
  const val test_runner = "1.1.0"
  const val espresso = "3.1.0"
  const val mockito = "2.1.0"
  const val koin = "2.0.1"
  const val permissions_dispatcher = "3.2.0"
  const val work = "1.0.0-alpha09"
  const val rxkotlin = "2.3.0"
  const val rxandroid = "2.1.0"
  const val googleServices = "4.1.0"
  const val fabric = "1.25.4"
  const val firebaseCore = "15.0.0"
  const val crashlytics = "2.9.5"
  const val randomBeans = "3.7.0"
}

object Deps {
  const val android_plugin = "com.android.tools.build:gradle:${Versions.android_plugin}"
  const val versions_plugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.versions_plugin}"
  const val quality_tools_plugin = "com.vanniktech:gradle-code-quality-tools-plugin:${Versions.quality_tools_plugin}"

  const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
  const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

  const val design = "com.google.android.material:material:1.1.0-alpha07"
  const val cardview_v7 = "androidx.cardview:cardview:1.0.0"
  const val recyclerview_v7 = "androidx.recyclerview:recyclerview:1.1.0-alpha06"

  const val constraint_layout = "androidx.constraintlayout:constraintlayout:2.0.0-beta2"

  const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
  const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
  const val paging = "androidx.paging:paging-runtime:${Versions.paging}"

  const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
  const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

  const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

  const val photo_view = "com.github.chrisbanes:PhotoView:${Versions.photo_view}"

  const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
  const val rxCallAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
  const val gson_converter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
  const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
  const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

  const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"

  const val work = "android.arch.work:work-runtime-ktx:${Versions.work}"
  const val navigation_runtime = "android.arch.navigation:navigation-runtime:${Versions.navigation}"
  const val navigation_runtime_ktx = "android.arch.navigation:navigation-runtime-ktx:${Versions.navigation}"
  const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
  const val navigation_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
  const val navigation_safe_args_plugin = "android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
  const val navigation_testing_ktx = "android.arch.navigation:navigation-testing-ktx:${Versions.navigation}"

  const val koin_arch = "org.koin:koin-android-viewmodel:${Versions.koin}"
  const val koin = "org.koin:koin-core:${Versions.koin}"
  const val koin_Android = "org.koin:koin-android:${Versions.koin}"

  const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxkotlin}"
  const val rxandroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}"

  const val permissions_dispatcher = "com.github.hotchemi:permissionsdispatcher:${Versions.permissions_dispatcher}"
  const val permissions_dispatcher_processor =
    "com.github.hotchemi:permissionsdispatcher-processor:${Versions.permissions_dispatcher}"

  const val googleServicesPlugin = "com.google.gms:google-services:${Versions.googleServices}"
  const val fabricPlugin = "io.fabric.tools:gradle:${Versions.fabric}"

  const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
  const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"

  const val junit = "junit:junit:${Versions.junit}"
  const val junit_jupiter = "org.junit.jupiter:junit-jupiter-api:${Versions.junit5}"
  const val junit_params = "org.junit.jupiter:junit-jupiter-params:${Versions.junit5}"
  const val test_runner = "androidx.test:runner:${Versions.test_runner}"
  const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
  const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
  const val mockito = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockito}"
  const val randomBeans = "io.github.benas:random-beans:${Versions.randomBeans}"
}
