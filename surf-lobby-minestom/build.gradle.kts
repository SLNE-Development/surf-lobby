plugins {
    id("dev.slne.surf.api.gradle.minestom")
}

surfMinestomApi {
    withCoreMinestom()
    withSurfRedis()
}

minestomPluginFile {
    main = "dev.slne.surf.lobby.minestom.LobbyMinestomPlugin"
    authors = listOf("red")

    pluginDependencies {
        register("surf-redis-minestom")
        register("surf-player-minestom")
        register("surf-parkour-minestom")
        register("surf-trophy-minestom")
    }
}

dependencies {
    api(project(":surf-lobby-core-client"))
    compileOnly("dev.slne.surf.player:surf-player-api:1.0.3")
    compileOnly("dev.slne.surf.parkour:surf-parkour-api:+")
    compileOnly("dev.slne.surf.trophy:surf-trophy-api:+")
}
