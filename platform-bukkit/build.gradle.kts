repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/public")
}

dependencies {
    compileOnly(project(":common"))
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}