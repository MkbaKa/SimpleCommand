package me.mkbaka.simplecommand.common.util

import me.mkbaka.simplecommand.common.command.component.LiteralComponent
import java.util.function.Consumer

class ComponentExtraJava {

    companion object {

        @JvmStatic
        fun mainCommand(callback: Consumer<LiteralComponent>): SimpleMainCommand {
            return SimpleMainCommand {
                callback.accept(this)
            }
        }

        @JvmStatic
        fun subCommand(callback: Consumer<LiteralComponent>): SimpleSubCommand {
            return SimpleSubCommand {
                callback.accept(this)
            }
        }

    }

}