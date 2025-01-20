plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

dependencies {
    api(project(":surf-lobby-core"))

    api(libs.org.apache.commons.commons.collections4)
}
