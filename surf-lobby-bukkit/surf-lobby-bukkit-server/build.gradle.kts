import dev.slne.surf.surfapi.gradle.util.registerRequired

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-lobby-bukkit:surf-lobby-bukkit-common"))
    paperLibrary(libs.org.apache.commons.commons.collections4)
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.bukkit.server.BukkitMain")
    foliaSupported(true)

    serverDependencies {
        registerRequired("surf-data-bukkit")
        registerRequired("LuckPerms")
    }
}