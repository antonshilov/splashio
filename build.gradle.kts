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

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}

codeQualityTools {
  failEarly = true

  findbugs {
    enabled = false
  }
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
    toolVersion = "0.26.0"
  }
  detekt {
    enabled = true
    toolVersion = "1.0.0.RC8"
    config = "codeQuality/detekt.yml"
  }
  cpd {
    enabled = false
  }
  errorProne {
    enabled = false
  }
}
