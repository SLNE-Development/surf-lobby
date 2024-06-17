plugins {
    id("dev.slne.bukkit-common-conventions")
}

dependencies {
    api(project(":surf-lobby-bukkit:surf-lobby-bukkit-common"))
    paperLibrary(libs.org.apache.commons.commons.collections4)
}

paper {
    main = "dev.slne.surf.lobby.bukkit.server.BukkitMain"
    generateLibrariesJson = true
}