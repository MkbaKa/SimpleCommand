package me.mkbaka.simplecommand.common.command.wrapper

import com.mojang.brigadier.Command
import com.mojang.brigadier.RedirectModifier
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.component.LiteralComponent
import java.util.function.Predicate

class LiteralCommandNode(
    override val component: LiteralComponent,
    literal: String,
    command: Command<CommandSource>?,
    requirement: Predicate<CommandSource>?,
    redirect: CommandNode<CommandSource>?,
    modifier: RedirectModifier<CommandSource>?,
    forks: Boolean
) : LiteralCommandNode<CommandSource>(literal, command, requirement, redirect, modifier, forks), WrappedCommandNode