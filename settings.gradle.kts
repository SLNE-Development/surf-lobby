pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("build-logic")
}

rootProject.name = "surf-lobby"

// Core
include(":surf-lobby-api")
include(":surf-lobby-core")

// Velocity
include(":surf-lobby-velocity")

// Bukkit
include(":surf-lobby-bukkit")
include(":surf-lobby-bukkit:surf-lobby-bukkit-common")
include(":surf-lobby-bukkit:surf-lobby-bukkit-server")
include(":surf-lobby-bukkit:surf-lobby-bukkit-lobby")
