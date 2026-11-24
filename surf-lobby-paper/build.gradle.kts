import dev.slne.surf.api.gradle.util.registerRequired
import dev.slne.surf.api.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
}

repositories {
    maven("https://repo.nexomc.com/snapshots")
}

dependencies {
    api(project(":surf-lobby-core-client"))

    compileOnly("dev.slne.surf.npc:surf-npc-api:+")
    compileOnly("com.nexomc:nexo:1.25.0-dev.11")
    compileOnly("dev.slne.surf.parkour:surf-parkour-api:+")
    compileOnly("dev.slne.surf.player:surf-player-api:1.0.3")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.PaperMain")
    generateLibraryLoader(false)
    foliaSupported(false)

    withSurfRedis()
    withCorePaper()

    authors.add("red")

    serverDependencies {
        registerSoft("surf-npc-paper")
        registerSoft("surf-parkour-paper")
        registerSoft("Nexo")
        registerSoft("surf-trophy-paper")
        registerRequired("surf-player-paper")
    }
}

paper {
    name = "surf-lobby"
}
