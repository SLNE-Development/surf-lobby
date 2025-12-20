import dev.slne.surf.surfapi.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    compileOnly("surf-hologram:surf-hologram-api:1.21.10-1.0.1-20251011.094119-1")
    compileOnly("dev.slne.surf.npc:surf-npc-api:1.21.10-1.5.0-20251009.154819-1")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.PaperMain")
    generateLibraryLoader(false)
    foliaSupported(true)

    authors.add("red")

    serverDependencies {
        registerSoft("surf-npc-bukkit")
        registerSoft("surf-hologram-paper")
    }
}