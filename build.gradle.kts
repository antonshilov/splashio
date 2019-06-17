import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  id("com.github.ben-manes.versions") version Versions.versions_plugin
  id("com.vanniktech.code.quality.tools") version Versions.quality_tools_plugin
  kotlin("jvm") version "1.3.30"
}
buildscript {
  repositories {
    mavenCentral()
    google()
    jcenter()
    gradlePluginPortal()
    maven("https://maven.fabric.io/public")
  }
  dependencies {
    classpath(Deps.android_plugin)
    classpath(Deps.kotlin_plugin)
    classpath(Deps.versions_plugin)
    classpath(Deps.quality_tools_plugin)
    classpath(Deps.fabricPlugin)

  }
}

allprojects {
  repositories {
    google()
    jcenter()
    maven("https://jitpack.io")
    maven("https://maven.fabric.io/public")
  }
}

//tasks {
//  val clean by registering(Delete::class) {
//    delete(buildDir)
//  }
//}

codeQualityTools {
  failEarly = true
  checkstyle {
    enabled = false
  }
  pmd {
    enabled = false
  }
  lint {
    enabled = true
    abortOnError = false
  }
  ktlint {
    enabled = true
  }
  detekt {
    enabled = true
    config = "codeQuality/detekt.yml"
  }
  cpd {
    enabled = false
  }
  errorProne {
    enabled = false
  }
}
dependencies {
  implementation(kotlin("stdlib-jdk8"))
}
repositories {
  mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
  jvmTarget = "1.8"
}
