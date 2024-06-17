plugins {
    id("dev.slne.java-library-conventions")
    id("dev.slne.java-shadow-conventions")
}

dependencies {
    api(project(":surf-lobby-core"))

    compileOnlyApi(libs.dev.slne.surf.api.velocity.api)
    compileOnlyApi(libs.com.velocitypowered.velocity.api)

    api(libs.org.apache.commons.commons.collections4)

    annotationProcessor(libs.com.velocitypowered.velocity.api)
}
