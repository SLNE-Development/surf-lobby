plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

velocityPluginFile {
    main = "dev.slne.surf.lobby.velocity.VelocityMain"

    pluginDependencies {
        register("luckperms")
        register("surf-api-velocity")
        register("surf-data-velocity")
    }
}

dependencies {
    api(project(":surf-lobby-core"))

    api(libs.org.apache.commons.commons.collections4)
}
