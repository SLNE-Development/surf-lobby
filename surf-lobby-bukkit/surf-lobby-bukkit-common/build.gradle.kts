plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.bukkit.CommonBukkitMain")
}

dependencies {
    api(project(":surf-lobby-core"))
    paperLibrary(libs.org.apache.commons.commons.collections4)
}