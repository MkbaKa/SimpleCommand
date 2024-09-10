package me.mkbaka.simplecommand.common.command.wrapper

import com.mojang.brigadier.Command
import com.mojang.brigadier.RedirectModifier
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.component.CommandComponent
import java.util.function.Predicate

class DynamicCommandNode<T>(
    override val component: CommandComponent<*>,
    name: String?,
    type: ArgumentType<T>?,
    command: Command<CommandSource>?,
    requirement: Predicate<CommandSource>?,
    redirect: CommandNode<CommandSource>?,
    modifier: RedirectModifier<CommandSource>?,
    forks: Boolean,
    customSuggestions: SuggestionProvider<CommandSource>?
) : ArgumentCommandNode<CommandSource, T>(
    name, type, command, requirement, redirect, modifier, forks, customSuggestions
), WrappedCommandNode