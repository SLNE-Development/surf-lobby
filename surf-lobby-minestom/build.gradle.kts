plugins {
    id("dev.slne.surf.api.gradle.minestom")
}

surfMinestomApi {
    withCoreMinestom()
    withSurfRedis()
}

dependencies {
    api(project(":surf-lobby-core-client"))
    compileOnly("dev.slne.surf.queue:surf-queue-api:+")
    compileOnly("dev.slne.surf.parkour:surf-parkour-api:+")
    compileOnly("dev.slne.surf.trophy:surf-trophy-api:+")
    compileOnly("dev.slne.surf.profile:surf-profile-api:+")
    compileOnly("dev.slne.surf.settings:surf-settings-api:+")
}
