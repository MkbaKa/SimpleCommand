pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.10"
    }
}
rootProject.name = "SimpleCommand"

include("common")
include("platform-bukkit")
include("plugin")
