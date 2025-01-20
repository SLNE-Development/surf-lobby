buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://repo.slne.dev/repository/maven-unsafe/") { name = "maven-unsafe" }
    }
    dependencies {
        classpath("dev.slne.surf:surf-api-gradle-plugin:1.21.4+")
    }
}

allprojects {
group = "dev.slne.surf"

version = "1.21.4-1.2.0-SNAPSHOT"
}
