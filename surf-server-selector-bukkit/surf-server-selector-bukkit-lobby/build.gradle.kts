plugins {
    id("dev.slne.bukkit-common-conventions")
}

dependencies {
    api(project(":surf-server-selector-bukkit:surf-server-selector-bukkit-common"))

    paperLibrary(libs.com.github.stefvanschie.inventoryframework)
    compileOnly(libs.io.th0rgal.oraxen)
}

paper {
    main = "dev.slne.surf.surfserverselector.bukkit.lobby.BukkitMain"
    generateLibrariesJson = true

    serverDependencies {
        registerDepend("Oraxen")
    }
}