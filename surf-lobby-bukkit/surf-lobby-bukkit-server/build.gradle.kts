plugins {
    id("dev.slne.bukkit-common-conventions")
}

dependencies {
    api(project(":surf-lobby-bukkit:surf-lobby-bukkit-common"))
    implementation(libs.org.apache.commons.commons.collections4)
}

bukkit {
    main = "dev.slne.surf.lobby.bukkit.server.BukkitMain"
}