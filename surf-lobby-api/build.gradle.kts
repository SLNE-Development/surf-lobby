plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    compileOnlyApi(libs.net.luckperms.api)
    compileOnlyApi(libs.dev.slne.data.api)
}
