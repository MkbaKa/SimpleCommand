package me.mkbaka.simplecommand.common.command.wrapper

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.tree.CommandNode
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.SuggestionContext
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import me.mkbaka.simplecommand.common.command.component.DynamicComponent

class SimpleDynamicBuilder<T>(
    val component: DynamicComponent<*>,
    val name: String,
    private val type: WrappedArgumentType<T>
) : ArgumentBuilder<CommandSource, SimpleDynamicBuilder<T>>() {

    override fun getThis(): SimpleDynamicBuilder<T> {
        return this
    }

    override fun build(): CommandNode<CommandSource> {
        val result = DynamicCommandNode(component, name, type, command, requirement, redirect, redirectModifier, isFork,
            component.suggest?.run {
                SuggestionProvider<CommandSource> { context, builder ->
                    this@run.invoke(SuggestionContext(builder, context, component)).forEach {
                        builder.suggest(it)
                    }
                    builder.buildFuture()
                }
            })

        arguments.forEach { result.addChild(it) }
        return result
    }

}