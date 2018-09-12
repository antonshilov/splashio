object Versions {
  const val kotlin = "1.2.70"
  val android_plugin = "3.3.0-alpha03"
  val versions_plugin = "0.20.0"
  val quality_tools_plugin = "0.13.0"
  val support = "28.0.0-rc02"
  val constraint_layout = "1.1.1"
  val navigation = "1.0.0-alpha05"
  val core_ktx = "0.3"
  val lifecycle = "1.1.1"
  val glide = "4.8.0"
  val timber = "4.7.1"
  val photo_view = "2.1.4"
  val paging = "1.0.1"
  val retrofit = "2.4.0"
  val okhttp = "3.11.0"
  val junit = "4.12"
  val test_runner = "1.0.2"
  val espresso = "3.0.2"
  val mockito = "2.0.0-alpha03"
  val koin = "1.0.0-alpha-12"
  val permissions_dispatcher = "3.2.0"
  val work = "1.0.0-alpha08"
  val rxkotlin = "2.3.0"
}

object Deps {
  val android_plugin = "com.android.tools.build:gradle:${Versions.android_plugin}"
  val versions_plugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.versions_plugin}"
  val quality_tools_plugin = "com.vanniktech:gradle-code-quality-tools-plugin:${Versions.quality_tools_plugin}"

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
  val rxCallAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
  val gson_converter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
  val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
  val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

  val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"

  val work = "android.arch.work:work-runtime-ktx:${Versions.work}"
  val navigation_runtime = "android.arch.navigation:navigation-runtime:${Versions.navigation}"
  val navigation_runtime_ktx = "android.arch.navigation:navigation-runtime-ktx:${Versions.navigation}"
  val navigation_ui_ktx = "android.arch.navigation:navigation-ui-ktx:${Versions.navigation}"
  val navigation_fragment = "android.arch.navigation:navigation-fragment:${Versions.navigation}"
  val navigation_fragment_ktx = "android.arch.navigation:navigation-fragment-ktx:${Versions.navigation}"
  val navigation_safe_args_plugin = "android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
  val navigation_testing_ktx = "android.arch.navigation:navigation-testing-ktx:${Versions.navigation}"

  val koin_arch = "org.koin:koin-android-architecture:${Versions.koin}"

  val rxkotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxkotlin}"

  val permissions_dispatcher = "com.github.hotchemi:permissionsdispatcher:${Versions.permissions_dispatcher}"
  val permissions_dispatcher_processor =
    "com.github.hotchemi:permissionsdispatcher-processor:${Versions.permissions_dispatcher}"

  val junit = "junit:junit:${Versions.junit}"
  val test_runner = "com.android.support.test:runner:${Versions.test_runner}"
  val espresso = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
  val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
  val mockito = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockito}"
}
