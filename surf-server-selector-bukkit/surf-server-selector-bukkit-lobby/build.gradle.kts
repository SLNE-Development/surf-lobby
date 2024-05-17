plugins {
    id("dev.slne.bukkit-common-conventions")
}

dependencies {
    api(project(":surf-server-selector-bukkit:surf-server-selector-bukkit-common"))

    paperLibrary(libs.me.devnatan.inventory.framework.bukkit)
    paperLibrary(libs.me.devnatan.inventory.framework.paper)
}

paper {
    main = "dev.slne.surf.surfserverselector.bukkit.lobby.BukkitMain"
    generateLibrariesJson = true
}