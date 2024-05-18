plugins {
    id("dev.slne.bukkit-common-conventions")
}

dependencies {
    api(project(":surf-server-selector-bukkit:surf-server-selector-bukkit-common"))

    paperLibrary(libs.com.github.stefvanschie.inventoryframework)
}

paper {
    main = "dev.slne.surf.surfserverselector.bukkit.lobby.BukkitMain"
    generateLibrariesJson = true
}