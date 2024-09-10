repositories {
    maven("https://libraries.minecraft.net")
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}