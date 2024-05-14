pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("build-logic")
}

rootProject.name = "surf-server-selector"
include(":surf-server-selector-api")
include(":surf-server-selector-core")
include(":surf-server-selector-bukkit")
include(":surf-server-selector-velocity")
