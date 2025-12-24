import dev.slne.surf.surfapi.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

repositories {
    maven("https://repo.nexomc.com/releases")
}

dependencies {
    compileOnly("surf-hologram:surf-hologram-api:1.21.11-1.0.2-SNAPSHOT")
    compileOnly("dev.slne.surf.npc:surf-npc-api:1.21.10-1.5.0-20251009.154819-1")
    compileOnly("com.nexomc:nexo:1.16.1")
    implementation("dev.slne.surf:surf-redis:1.0.0-SNAPSHOT")
    implementation("dev.slne.surf.event:surf-event-base-api-redis:1.21.11-1.0.0-SNAPSHOT")
    implementation("dev.slne.surf.tab:surf-tab-api:1.21.11-1.0.2-SNAPSHOT")
}

version = findProperty("version") as String
group = "dev.slne.surf.lobby"

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.PaperMain")
    generateLibraryLoader(false)
    foliaSupported(false)

    authors.add("red")

    serverDependencies {
        registerSoft("surf-npc-bukkit")
        registerSoft("surf-hologram-paper")
        registerSoft("Nexo")
    }
}