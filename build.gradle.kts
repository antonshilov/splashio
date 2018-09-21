// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  id("com.github.ben-manes.versions") version Versions.versions_plugin
  id("com.vanniktech.code.quality.tools") version Versions.quality_tools_plugin
}
buildscript {
  repositories {
    google()
    jcenter()
    gradlePluginPortal()
  }
  dependencies {
    classpath(Deps.android_plugin)
    classpath(Deps.kotlin_plugin)
    classpath(Deps.versions_plugin)
    classpath(Deps.quality_tools_plugin)
  }
}

allprojects {
  repositories {
    google()
    jcenter()
    maven("https://jitpack.io")
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}