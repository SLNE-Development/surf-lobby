plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-lobby-paper:surf-lobby-paper-common"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.paper.server.PaperMain")
    bootstrapper("dev.slne.surf.lobby.paper.server.PaperBootstrap")
    foliaSupported(true)
    generateLibraryLoader(false)
    withCloudClientPaper()
}