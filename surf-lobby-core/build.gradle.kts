plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    api(project(":surf-lobby-api"))

    compileOnlyApi(libs.org.apache.commons.commons.collections4)
}
