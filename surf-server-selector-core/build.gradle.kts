plugins {
    id("dev.slne.java-library-conventions")
    id("dev.slne.java-shadow-conventions")
}

dependencies {
    api(project(":surf-server-selector-api"))
}
