plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation("net.linguica.gradle:maven-settings-plugin:0.5")
    implementation("com.github.johnrengelman:shadow:8.1.1")
    implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:4.2.1")

    implementation("net.minecrell:plugin-yml:0.6.0")
    implementation("xyz.jpenilla:run-task:2.3.0")
}