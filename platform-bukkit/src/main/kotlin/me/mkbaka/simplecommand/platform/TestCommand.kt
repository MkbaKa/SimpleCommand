package me.mkbaka.simplecommand.platform

import me.mkbaka.simplecommand.common.command.CommandNotify
import me.mkbaka.simplecommand.common.util.mainCommand
import me.mkbaka.simplecommand.common.util.simple.CommandBody
import me.mkbaka.simplecommand.common.util.simple.CommandHeader
import me.mkbaka.simplecommand.common.util.subCommand
import me.mkbaka.simplecommand.platform.argument.TypePlayer
import me.mkbaka.simplecommand.platform.argument.TypeWorld
import org.bukkit.World
import org.bukkit.entity.Player

@CommandHeader("Test", aliases = ["ttt"])
class TestCommand {

    @CommandBody
    val main = mainCommand {
        onPermissionCheckFailure(CommandNotify { sender, input, _, _ ->
            sender.sendMessage("You need permission to use: $input")
        })
    }

    @CommandBody
    val run = subCommand {
        dynamic("key") {
            execute {
                println("key = ${it["key"]}")
            }
        }
    }

    @CommandBody
    val kill = subCommand {
        dynamic("player", TypePlayer.player()) {
            dynamic("world", TypeWorld.world()) {
                execute {
                    println("kill ${it.get<Player>("player")} in world ${it.get<World>("world")}")
                }
            }
            execute {
                println(it.get<Player>("player"))
            }
        }
    }

    @CommandBody
    val literal = subCommand {
        literal("hello") {
            literal("world") {
                execute {
                    println("hello world")
                }
            }
        }
    }

}