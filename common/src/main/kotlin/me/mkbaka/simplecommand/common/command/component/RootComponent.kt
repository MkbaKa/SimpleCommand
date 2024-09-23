package me.mkbaka.simplecommand.common.command.component

import com.mojang.brigadier.tree.RootCommandNode
import me.mkbaka.simplecommand.common.CommandSource

/**
 * 只是一个数据类
 * 实际的命令头组件是 subComponent
 */
class RootComponent(
    private val name: String,
    val subComponent: LiteralComponent
) : RootCommandNode<CommandSource>() {

    init {
        subComponent.incorrectArgument { source, input, header, args ->
            source.sendMessage("invalid argument by RootComponent: ${args.contentToString()}")
        }

        subComponent.incorrectCommandSource { source, input, header, args ->
            source.sendMessage("incorrect command source by RootComponent")
        }

        subComponent.onPermissionCheckFailure { source, input, header, args ->
            source.sendMessage("You need permission \"${subComponent.permission}\" to use this command.")
        }
    }

    override fun getName(): String {
        return name
    }

    override fun toString(): String {
        return "RootComponent{name=$name, subComponent=$subComponent}"
    }

}