pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.slne.dev/repository/maven-public/") { name = "maven-public" }
    }
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
