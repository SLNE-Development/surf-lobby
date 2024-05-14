plugins {
    id("dev.slne.java-library-conventions")
    id("dev.slne.java-shadow-conventions")
}

dependencies {
    compileOnlyApi(libs.dev.slne.surf.api.core.api)
    compileOnlyApi(libs.dev.slne.data.api)
}
