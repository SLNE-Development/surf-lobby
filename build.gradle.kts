import dev.slne.surf.surfapi.gradle.util.slnePrivate

buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://repo.slne.dev/repository/maven-public/") { name = "maven-public" }
    }
    dependencies {
        classpath("dev.slne.surf:surf-api-gradle-plugin:1.21.7+")
    }
}

allprojects {
    group = "dev.slne.surf"
    version = "1.21.7-1.4.0-SNAPSHOT"

    repositories {
        slnePrivate()
    }
}
