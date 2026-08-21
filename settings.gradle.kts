rootProject.name = "surf-lobby"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://reposilite.slne.dev/releases")
    }
}

include("surf-lobby-core-client")
include("surf-lobby-paper")
include("surf-lobby-minestom")
