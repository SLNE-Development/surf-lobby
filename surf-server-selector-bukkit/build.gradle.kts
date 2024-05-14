import net.minecrell.pluginyml.paper.PaperPluginDescription.DependencyDefinition
import net.minecrell.pluginyml.paper.PaperPluginDescription.RelativeLoadOrder


plugins {
    id("dev.slne.java-library-conventions")
    id("dev.slne.java-shadow-conventions")
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

dependencies {
    api(project(":surf-server-selector-core"))
    compileOnlyApi(libs.dev.slne.surf.api.bukkit.api)
    compileOnlyApi(libs.io.papermc.paper.api)
}

tasks {
    runServer {
        minecraftVersion("1.20.6")

        downloadPlugins {
            modrinth("commandapi", "9.4.1")
        }
    }
}

paper {
    name = "surf-server-selector-bukkit"

    main = "dev.slne.surf.surfserverselector.bukkitBukkitMain"
    bootstrapper = "dev.slne.surf.surfserverselector.bukkitBukkitBootstrapper"
    loader = "dev.slne.surf.surfserverselector.bukkitBukkitLoader"
    generateLibrariesJson = true
    apiVersion = "1.20"

    serverDependencies {
        registerDepend("SurfBukkitAPI")
        registerDepend("surf-data-bukkit")
        registerDepend("LuckPerms")
    }
}

/**
 * Registers a soft dependency.
 *
 * @param name The name of the dependency.
 * @param joinClassPath Whether the dependency should be joined to the classpath.
 * @param loadOrder The load order of the dependency.
 */
fun NamedDomainObjectContainerScope<DependencyDefinition>.registerSoft(
    name: String,
    joinClassPath: Boolean = true,
    loadOrder: RelativeLoadOrder = RelativeLoadOrder.BEFORE
) = register(name) {
    this.required = false
    this.joinClasspath = joinClassPath
    this.load = loadOrder
}

/**
 * Registers a required dependency.
 *
 * @param name The name of the dependency.
 * @param joinClassPath Whether the dependency should be joined to the classpath.
 * @param loadOrder The load order of the dependency.
 */
fun NamedDomainObjectContainerScope<DependencyDefinition>.registerDepend(
    name: String,
    joinClassPath: Boolean = true,
    loadOrder: RelativeLoadOrder = RelativeLoadOrder.BEFORE
) = register(name) {
    this.required = true
    this.joinClasspath = joinClassPath
    this.load = loadOrder
}
