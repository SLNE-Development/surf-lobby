import dev.slne.surf.api.gradle.util.registerRequired
import dev.slne.surf.api.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin") version "+"
}

repositories {
    maven("https://repo.nexomc.com/releases")
}

dependencies {
    compileOnly("dev.slne.surf.npc:surf-npc-api:+")
    compileOnly("com.nexomc:nexo:1.16.1")
    implementation("dev.slne.surf.event:surf-event-base-api-common:+")
    implementation("dev.slne.surf.tab:surf-tab-api:+")
    compileOnly("dev.slne.surf.parkour:surf-parkour-api:+")
    compileOnly("dev.slne.surf.trophy:surf-trophy-api:+")
    compileOnly("dev.slne.surf.profile:surf-profile-api:+")
    compileOnly("dev.slne.surf.settings:surf-settings-api:+")
    compileOnly("dev.slne.surf.queue:surf-queue-api:+")
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