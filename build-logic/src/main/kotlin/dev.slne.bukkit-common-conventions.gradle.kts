import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("dev.slne.java-library-conventions")
    id("dev.slne.java-shadow-conventions")
    id("net.minecrell.plugin-yml.paper")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    api(project(":surf-lobby-core"))

    compileOnlyApi(libs.dev.slne.surf.api.bukkit.api)
    compileOnlyApi(libs.io.papermc.paper.api)
}

tasks {
    runServer {
        minecraftVersion("1.20.4")

        downloadPlugins {
            modrinth("commandapi", "9.5.1")
            url("https://repo.slne.dev/service/rest/v1/search/assets/download?repository=maven-snapshots&group=dev.slne&name=surf-data-bukkit&sort=version&direction=desc")
        }
    }
}

paper {
    name = "surf-lobby-bukkit"

    main = "SHOULD_BE_CHANGED_BY_SUBPROJECTS"
    bootstrapper = "dev.slne.surf.lobby.bukkit.BukkitBootstrapper"
    loader = "dev.slne.surf.lobby.bukkit.BukkitLoader"
    generateLibrariesJson = false
    apiVersion = "1.20"

    serverDependencies {
        registerDepend("SurfBukkitAPI")
        registerDepend("surf-data-bukkit")
        registerDepend("LuckPerms")
    }
}

