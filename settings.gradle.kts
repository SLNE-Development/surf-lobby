pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("build-logic")
}

rootProject.name = "surf-server-selector"
include(":surf-server-selector-api")
include(":surf-server-selector-core")
include(":surf-server-selector-bukkit")
include(":surf-server-selector-velocity")
include("surf-server-selector-bukkit:surf-server-selector-bukkit-common")
findProject(":surf-server-selector-bukkit:surf-server-selector-bukkit-common")?.name = "surf-server-selector-bukkit-common"
include("surf-server-selector-bukkit:surf-server-seloctor-bukkit-server")
findProject(":surf-server-selector-bukkit:surf-server-seloctor-bukkit-server")?.name = "surf-server-seloctor-bukkit-server"
include("surf-server-selector-bukkit:surf-server-selector-bukkit-lobby")
findProject(":surf-server-selector-bukkit:surf-server-selector-bukkit-lobby")?.name = "surf-server-selector-bukkit-lobby"
