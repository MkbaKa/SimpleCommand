import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":platform-bukkit"))
    implementation(kotlin("stdlib"))

    implementation("com.mojang:brigadier:1.2.9")
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set(rootProject.version.toString())
        archiveBaseName.set(rootProject.name)

        exclude("META-INF/maven/**")
        exclude("module-info")
        exclude("org/intellij/lang/annotations/**")
        exclude("org/jetbrains/annotations/**")

        relocate("com.mojang.", "${rootProject.group}.libs.com.mojang.")
        relocate("kotlin.", "${rootProject.group}.libs.kotlin.")

        destinationDirectory.set(File("E:\\Servers\\Teeeeeeeeeeest\\plugins"))
    }

    kotlinSourcesJar {
        rootProject.subprojects.forEach { proj ->
            from(proj.sourceSets["main"].allSource)
        }
    }
}