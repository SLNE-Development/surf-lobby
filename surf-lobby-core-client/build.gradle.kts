plugins {
    id("dev.slne.surf.api.gradle.core")
}

surfCoreApi {
    withCoreCommon()
    withSurfRedis()
}

repositories {
    mavenLocal()
}

dependencies {
    api("dev.slne.surf.event:surf-event-base-api-common:+")

    compileOnly("dev.slne.surf.queue:surf-queue-api:+")
    compileOnly("dev.slne.surf.parkour:surf-parkour-api:+")
    compileOnly("dev.slne.surf.trophy:surf-trophy-api:+")
    compileOnly("dev.slne.surf.profile:surf-profile-api:+")
    compileOnly("dev.slne.surf.settings:surf-settings-api:+")
    implementation("dev.slne.surf.event.data:surf-event-data-source:+")
}

sourceSets.test {
    compileClasspath += sourceSets.main.get().compileClasspath
    runtimeClasspath += sourceSets.main.get().compileClasspath
}
