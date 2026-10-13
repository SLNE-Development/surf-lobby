plugins {
    id("dev.slne.surf.api.gradle.minestom")
}

surfMinestomApi {
    withCoreMinestom()
    withSurfRedis()
}

repositories {
    mavenLocal()
}

minestomPluginFile {
    main = "dev.slne.surf.lobby.minestom.LobbyMinestomPlugin"
    authors = listOf("red")

    pluginDependencies {
        register("surf-redis-minestom")
        register("surf-queue-minestom")
        register("surf-parkour-minestom")
        register("surf-profile-minestom")
        register("surf-settings-minestom")
        register("surf-trophy-minestom")
        register("surf-friends-minestom")
    }
}

dependencies {
    api(project(":surf-lobby-core-client"))
    compileOnly("dev.slne.surf.queue:surf-queue-api:+")
    compileOnly("dev.slne.surf.parkour:surf-parkour-api:+")
    compileOnly("dev.slne.surf.trophy:surf-trophy-api:+")
    compileOnly("dev.slne.surf.profile:surf-profile-api:+")
    compileOnly("dev.slne.surf.settings:surf-settings-api:+")
    compileOnly("dev.slne.surf.friends:surf-friends-api:+")
}
