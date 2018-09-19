plugins { kotlin("jvm") }

dependencies {
  implementation(Deps.rxkotlin)

  testImplementation(Deps.junit)
  testImplementation(Deps.mockito)
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_7
  targetCompatibility = JavaVersion.VERSION_1_7
}
