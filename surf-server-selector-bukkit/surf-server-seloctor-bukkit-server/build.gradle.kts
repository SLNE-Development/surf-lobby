plugins {
    id("dev.slne.bukkit-common-conventions")
}

dependencies {
    api(project(":surf-server-selector-bukkit:surf-server-selector-bukkit-common"))
}

paper {
    main = "dev.slne.surf.surfserverselector.bukkit.server.BukkitMain"
}