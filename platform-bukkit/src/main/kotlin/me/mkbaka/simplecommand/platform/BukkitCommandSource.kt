package me.mkbaka.simplecommand.platform

import me.mkbaka.simplecommand.common.CommandSource
import org.bukkit.command.CommandSender

class BukkitCommandSource(
    private val sender: CommandSender
) : CommandSource {

    override fun hasPermission(perm: String): Boolean {
        return sender.hasPermission(perm)
    }

    override fun sendMessage(str: String) {
        sender.sendMessage(str)
    }

    override fun toString(): String {
        return sender.toString()
    }

    override fun hashCode(): Int {
        return sender.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return sender == other
    }

}