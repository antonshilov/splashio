plugins { kotlin("jvm") }

dependencies {
  implementation(Deps.rxkotlin)

  testImplementation(Deps.junit)
  testImplementation(Deps.mockito)
  testImplementation(Deps.randomBeans)
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_7
  targetCompatibility = JavaVersion.VERSION_1_7
}
