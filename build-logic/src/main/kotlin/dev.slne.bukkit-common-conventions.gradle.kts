import net.minecrell.pluginyml.paper.PaperPluginDescription.DependencyDefinition
import net.minecrell.pluginyml.paper.PaperPluginDescription.RelativeLoadOrder
import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("dev.slne.java-library-conventions")
    id("dev.slne.java-shadow-conventions")
    id("net.minecrell.plugin-yml.paper")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    api(project(":surf-server-selector-core"))

    compileOnlyApi(libs.dev.slne.surf.api.bukkit.api)
    compileOnlyApi(libs.io.papermc.paper.api)
}

tasks {
    runServer {
        minecraftVersion("1.20.4")

        downloadPlugins {
            modrinth("commandapi", "9.4.1")
        }
    }
}

paper {
    name = "surf-server-selector-bukkit"

    main = "SHOULD_BE_CHANGED_BY_SUBPROJECTS"
    bootstrapper = "dev.slne.surf.surfserverselector.bukkit.BukkitBootstrapper"
    loader = "dev.slne.surf.surfserverselector.bukkit.BukkitLoader"
    generateLibrariesJson = false
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