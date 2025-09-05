plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-lobby-bukkit:surf-lobby-bukkit-common"))

    compileOnly(libs.nexo)
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.lobby.bukkit.lobby.PaperMain")
    bootstrapper("dev.slne.surf.lobby.bukkit.lobby.PaperBootstrap")
    foliaSupported(true)
    withCloudClientPaper()

    serverDependencies {
//        registerRequired("Nexo")
    }
}