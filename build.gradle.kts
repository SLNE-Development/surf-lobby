import dev.slne.surf.surfapi.gradle.util.registerRequired
import dev.slne.surf.surfapi.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin") version "1.21.11+"
}

repositories {
    maven("https://repo.nexomc.com/releases")
}

dependencies {
    compileOnly("surf-hologram:surf-hologram-api:1.21.11-1.0.2-SNAPSHOT")
    compileOnly("dev.slne.surf.npc:surf-npc-api:1.21.11-1.6.0-SNAPSHOT")
    compileOnly("com.nexomc:nexo:1.16.1")
    implementation("dev.slne.surf.event:surf-event-base-api-common:1.21.11-1.0.0-SNAPSHOT")
    implementation("dev.slne.surf.tab:surf-tab-api:1.21.11-1.0.2-SNAPSHOT")
    compileOnly("dev.slne.surf.parkour:surf-parkour-api:1.21.11-3.4.0-SNAPSHOT")
    compileOnly("dev.slne.surf.trophy:surf-trophy-api:1.21.11-1.0.0-SNAPSHOT")
    compileOnly("dev.slne.surf.profile:surf-profile-api:1.21.11-1.0.0-SNAPSHOT")
    compileOnly("dev.slne.surf.settings:surf-settings-api:1.21.11-2.0.0-SNAPSHOT")
    compileOnly("dev.slne.surf:surf-queue-api:1.0.0-SNAPSHOT")
}

version = findProperty("version") as String
group = "dev.slne.surf.lobby"

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.PaperMain")
    generateLibraryLoader(false)
    foliaSupported(false)

    withSurfRedis()
    withCorePaper()

    authors.add("red")

    serverDependencies {
        registerSoft("surf-npc-paper")
        registerSoft("surf-hologram-paper")
        registerSoft("surf-parkour-paper")
        registerSoft("Nexo")
        registerSoft("surf-trophy-paper")
        registerSoft("surf-profile-paper")
        registerSoft("surf-settings-paper")
        registerRequired("surf-queue-paper")
    }
}