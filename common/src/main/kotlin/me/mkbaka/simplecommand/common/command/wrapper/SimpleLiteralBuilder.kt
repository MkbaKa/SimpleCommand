package me.mkbaka.simplecommand.common.command.wrapper

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.component.LiteralComponent

class SimpleLiteralBuilder(
    val component: LiteralComponent,
    literal: String
) : LiteralArgumentBuilder<CommandSource>(literal) {

    override fun build(): LiteralCommandNode<CommandSource> {
        val result = LiteralCommandNode(component, literal, command, requirement, redirect, redirectModifier, isFork)

        arguments.forEach { result.addChild(it) }
        return result
    }

}
