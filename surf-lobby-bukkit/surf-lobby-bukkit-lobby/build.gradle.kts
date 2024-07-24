plugins {
    id("dev.slne.bukkit-common-conventions")
}

dependencies {
    api(project(":surf-lobby-bukkit:surf-lobby-bukkit-common"))

    implementation(libs.com.github.stefvanschie.inventoryframework)
    compileOnly(libs.io.th0rgal.oraxen)
    implementation(libs.org.apache.commons.commons.collections4)
}

bukkit {
    main = "dev.slne.surf.lobby.bukkit.lobby.BukkitMain"
    depend = arrayListOf("surf-bukkit-api", "surf-data-bukkit", "LuckPerms", "Oraxen")
}