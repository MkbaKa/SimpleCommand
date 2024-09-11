package me.mkbaka.simplecommand.common.command.argument.impl

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import java.util.concurrent.CompletableFuture

class TypeLong(
    min: Long,
    max: Long
) : WrappedArgumentType<Long> {

    private val longType = LongArgumentType.longArg(min, max)

    override fun parse(reader: StringReader): Long {
        return longType.parse(reader)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return longType.listSuggestions(context, builder)
    }

    override fun getExamples(): MutableCollection<String> {
        return longType.examples
    }

    companion object {

        @JvmStatic
        fun long(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE) = TypeLong(min, max)

    }

}