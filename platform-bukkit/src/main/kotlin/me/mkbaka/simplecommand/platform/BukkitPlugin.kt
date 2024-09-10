package me.mkbaka.simplecommand.platform

import org.bukkit.plugin.java.JavaPlugin

class BukkitPlugin : JavaPlugin() {

    val registry = BukkitCommandRegistry(this)

    override fun onEnable() {
        registry.register(TestCommand())
        registry.register(ExampleCommand())
    }

}