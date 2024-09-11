package me.mkbaka.simplecommand.common.command.argument.impl

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import java.util.concurrent.CompletableFuture

class TypeBoolean : WrappedArgumentType<Boolean> {

    private val booleanType = BoolArgumentType.bool()

    override fun parse(reader: StringReader): Boolean {
        return booleanType.parse(reader)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return booleanType.listSuggestions(context, builder)
    }

    override fun getExamples(): MutableCollection<String> {
        return booleanType.examples
    }

    companion object {

        @JvmStatic
        fun boolean() = TypeBoolean()

    }

}