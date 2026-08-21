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
    implementation("dev.slne.surf.tab:surf-tab-api:+")
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
        registerSoft("surf-profile-paper")
        registerSoft("surf-settings-paper")
        registerRequired("surf-queue-paper")
    }
}

paper {
    name = "surf-lobby"
}
