dependencies {
    implementation("com.mojang:brigadier:1.2.9")
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}