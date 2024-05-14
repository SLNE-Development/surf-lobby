plugins {
    id("dev.slne.java-library-conventions")
    id("dev.slne.java-shadow-conventions")
}

dependencies {
    api(project(":surf-server-selector-core"))
    compileOnlyApi(libs.dev.slne.surf.api.velocity.api)
    compileOnlyApi(libs.com.velocitypowered.velocity.api)

    annotationProcessor(libs.com.velocitypowered.velocity.api)
}
