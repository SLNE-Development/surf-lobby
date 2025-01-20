import dev.slne.surf.surfapi.gradle.util.registerRequired

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-lobby-bukkit:surf-lobby-bukkit-common"))

    compileOnly(libs.io.th0rgal.oraxen)
    compileOnlyApi(libs.dev.slne.surf.proxy.api)
    paperLibrary(libs.org.apache.commons.commons.collections4)
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.bukkit.lobby.BukkitMain")

    serverDependencies {
        registerRequired("Oraxen")
        registerRequired("surf-data-bukkit")
        registerRequired("surf-proxy-bukkit")
        registerRequired("LuckPerms")
    }
}