repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/public")
}

val dependModules = listOf("common")

dependencies {
    dependModules.forEach { module ->
        implementation(project(":$module"))
    }
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
}

tasks {
    kotlinSourcesJar {
        dependModules.forEach { module ->
            from(project(":$module").sourceSets["main"].allSource) {
                into("main/")
            }
        }
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}