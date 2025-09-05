plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-lobby-paper:surf-lobby-paper-common"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.paper.lobby.PaperMain")
    bootstrapper("dev.slne.surf.lobby.paper.lobby.PaperBootstrap")
    foliaSupported(true)
    withCloudClientPaper()
}