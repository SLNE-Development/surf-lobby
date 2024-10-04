
plugins {
    id("dev.slne.bukkit-common-conventions")
}

dependencies {
    api(project(":surf-lobby-bukkit:surf-lobby-bukkit-common"))

    compileOnly(libs.io.th0rgal.oraxen)
    compileOnlyApi(libs.dev.slne.surf.proxy.api)
    paperLibrary(libs.org.apache.commons.commons.collections4)
}

paper {
    main = "dev.slne.surf.lobby.bukkit.lobby.BukkitMain"
    generateLibrariesJson = true

    serverDependencies {
        registerDepend("Oraxen")
        registerDepend("surf-proxy-bukkit")
    }
}