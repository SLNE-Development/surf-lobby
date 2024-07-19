plugins {
    java
    `maven-publish`
    id("net.linguica.maven-settings")
}

repositories {
    maven("https://repo.slne.dev/repository/maven-public/") { name = "maven-public" }
    maven("https://repo.slne.dev/repository/maven-snapshots/") { name = "maven-snapshots" }
    maven("https://repo.slne.dev/repository/maven-proxy/") { name = "maven-proxy" }
}

group = "dev.slne.surf"
version = "1.20.4-1.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven("https://repo.slne.dev/repository/maven-snapshots/") { name = "maven-snapshots" }
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.add("-parameters")
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}
