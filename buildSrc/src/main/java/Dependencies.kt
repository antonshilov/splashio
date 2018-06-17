object Versions {
  const val kotlin = "1.2.50"
  val android_plugin = "3.2.0-alpha18"
  val support = "28.0.0-alpha3"
  val constraint_layout = "1.1.1"
  val navigation = "1.0.0-alpha02"
  val core_ktx = "0.3"
  val lifecycle = "1.1.1"
  val glide = "4.6.1"
  val timber = "4.7.0"
  val photo_view = "2.1.3"
  val paging = "1.0.0"
  val retrofit = "2.4.0"
  val okhttp = "3.10.0"
  val junit = "4.12"
  val test_runner = "1.0.2"
  val espresso = "3.0.2"
  val koin = "0.9.3"

}

object Deps {
  val android_plugin = "com.android.tools.build:gradle:${Versions.android_plugin}"


  val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
  val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

  val appcompat_v7 = "com.android.support:appcompat-v7:${Versions.support}"
  val support_v4 = "com.android.support:support-v4:${Versions.support}"
  val design = "com.android.support:design:${Versions.support}"
  val cardview_v7 = "com.android.support:cardview-v7:${Versions.support}"
  val recyclerview_v7 = "com.android.support:recyclerview-v7:${Versions.support}"

  val constraint_layout = "com.android.support.constraint:constraint-layout:${Versions.constraint_layout}"

  val lifecycle_extensions = "android.arch.lifecycle:extensions:${Versions.lifecycle}"
  val lifecycle_compiler = "android.arch.lifecycle:compiler:${Versions.lifecycle}"
  val paging = "android.arch.paging:runtime:${Versions.paging}"

  val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
  val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

  val timber = "com.jakewharton.timber:timber:${Versions.timber}"

  val photo_view = "com.github.chrisbanes:PhotoView:${Versions.photo_view}"

  val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
  val gson_converter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
  val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
  val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

  val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
  val navigation_runtime = "android.arch.navigation:navigation-runtime:${Versions.navigation}"
  val navigation_runtime_ktx = "android.arch.navigation:navigation-runtime-ktx:${Versions.navigation}"
  val navigation_ui_ktx = "android.arch.navigation:navigation-ui-ktx:${Versions.navigation}"
  val navigation_fragment = "android.arch.navigation:navigation-fragment:${Versions.navigation}"
  val navigation_fragment_ktx = "android.arch.navigation:navigation-fragment-ktx:${Versions.navigation}"
  val navigation_safe_args_plugin = "android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
  val navigation_testing_ktx = "android.arch.navigation:navigation-testing-ktx:${Versions.navigation}"

  val koin_arch = "org.koin:koin-android-architecture:${Versions.koin}"

  val junit = "junit:junit:${Versions.junit}"
  val test_runner = "com.android.support.test:runner:${Versions.test_runner}"
  val espresso = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"

}