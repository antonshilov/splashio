plugins { kotlin("jvm") }

dependencies {
  implementation(Deps.rxkotlin)
  implementation("io.reactivex.rxjava2:rxjava:2.2.8")

  testImplementation(Deps.junit)
  testImplementation(Deps.mockito)
  testImplementation(Deps.randomBeans)
  implementation(kotlin("stdlib-jdk8"))
}
