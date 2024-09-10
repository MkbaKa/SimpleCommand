package me.mkbaka.simplecommand.platform

import me.mkbaka.simplecommand.common.CommandRegistry
import me.mkbaka.simplecommand.common.command.component.LiteralComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.SimplePluginManager

class BukkitCommandRegistry(
    private val plugin: Plugin
) : CommandRegistry(plugin.logger) {

    override fun registerToPlatform(component: LiteralComponent): Boolean {
        return newCommand(component.name, plugin)?.also { pluginCommand ->
            setOwner(pluginCommand, plugin)
            pluginCommand.aliases = component.aliases.toList()

            pluginCommand.setExecutor { sender, _, header, args ->
                super.execute(BukkitCommandSource(sender), header, args)
                true
            }

            pluginCommand.setTabCompleter { sender, _, header, args ->
                super.tabCompleter(BukkitCommandSource(sender), header, args)
            }

            commandMap.register(pluginCommand.name, pluginCommand)
        } != null
    }

    companion object {

        private val commandMap by lazy {
            SimplePluginManager::class.java.getDeclaredField("commandMap").run {
                isAccessible = true
                get(Bukkit.getPluginManager()) as SimpleCommandMap
            }
        }

        private val knowCommands by lazy {
            SimpleCommandMap::class.java.getDeclaredField("knownCommands").run {
                isAccessible = true
                get(commandMap) as MutableMap<String, Command>
            }
        }

        private val constructor by lazy {
            PluginCommand::class.java.getDeclaredConstructor(String::class.java, Plugin::class.java).also {
                it.isAccessible = true
            }
        }

        private val ownPlugin by lazy {
            PluginCommand::class.java.getDeclaredField("owningPlugin").also {
                it.isAccessible = true
            }
        }

        @JvmStatic
        fun newCommand(name: String, owner: Plugin): PluginCommand? {
            return kotlin.runCatching {
                constructor.newInstance(name, owner)
            }.getOrElse { ex ->
                ex.printStackTrace()
                null
            }
        }

        @JvmStatic
        fun setOwner(command: PluginCommand, plugin: Plugin) {
            kotlin.runCatching {
                ownPlugin.set(command, plugin)
            }.exceptionOrNull()?.printStackTrace()
        }

    }

}