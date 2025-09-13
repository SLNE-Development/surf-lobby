plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

surfRawPaperApi {
    withCloudClientPaper()
}

dependencies {
    api(project(":surf-lobby-core"))
}