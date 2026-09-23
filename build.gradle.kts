import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.slne.surf.api.gradle.util.slneReleases

buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://reposilite.slne.dev/releases")
    }
    dependencies {
        classpath("dev.slne.surf.api:surf-api-gradle-plugin:+")
    }
}

allprojects {
    group = "dev.slne.surf.lobby"
    version = findProperty("version") as String
}

subprojects {
    tasks.withType<ShadowJar>().configureEach {
        relocate("dev.slne.surf.event.data", "dev.slne.surf.lobby.libs.event.data")
        relocate("dev.slne.surf.event.state", "dev.slne.surf.lobby.libs.event.state")
    }

    afterEvaluate {
        plugins.withType<PublishingPlugin> {
            configure<PublishingExtension> {
                repositories {
                    slneReleases()
                }
            }
        }
    }
}
