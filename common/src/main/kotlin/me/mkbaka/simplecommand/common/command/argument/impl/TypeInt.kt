package me.mkbaka.simplecommand.common.command.argument.impl

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import java.util.concurrent.CompletableFuture

class TypeInt(
    min: Int,
    max: Int
) : WrappedArgumentType<Int> {

    private val intType = IntegerArgumentType.integer(min, max)

    override fun parse(reader: StringReader): Int {
        return intType.parse(reader)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return intType.listSuggestions(context, builder)
    }

    override fun getExamples(): MutableCollection<String> {
        return intType.examples
    }

    companion object {

        @JvmStatic
        fun int(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE) = TypeInt(min, max)

    }

}