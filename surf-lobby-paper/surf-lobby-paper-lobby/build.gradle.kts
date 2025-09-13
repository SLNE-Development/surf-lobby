plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-lobby-paper:surf-lobby-paper-common"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.paper.common.PaperMain")
    bootstrapper("dev.slne.surf.lobby.paper.common.PaperBootstrap")
    foliaSupported(true)
    generateLibraryLoader(false)
    withCloudClientPaper()
}