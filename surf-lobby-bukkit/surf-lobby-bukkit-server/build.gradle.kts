import dev.slne.surf.surfapi.gradle.util.withSurfApiBukkit

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-lobby-bukkit:surf-lobby-bukkit-common"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.bukkit.server.PaperMain")
    bootstrapper("dev.slne.surf.lobby.bukkit.server.PaperBootstrap")
    foliaSupported(true)
    withCloudClientPaper()

    runServer {
        withSurfApiBukkit()
    }
}