plugins { kotlin("jvm") }

dependencies {
  implementation(project(":domain"))
  implementation(Deps.rxkotlin)
  implementation(Deps.retrofit)
  implementation(Deps.rxCallAdapter)
  implementation(Deps.gson_converter)
  implementation(Deps.okhttp)
  implementation(Deps.logging_interceptor)

  testImplementation(Deps.junit)
  testImplementation(Deps.mockito)
  testImplementation(Deps.mockWebServer)
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_7
  targetCompatibility = JavaVersion.VERSION_1_7
}
