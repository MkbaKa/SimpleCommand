package me.mkbaka.simplecommand.common.command.argument

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import java.util.concurrent.CompletableFuture

class TypeDouble(
    min: Double,
    max: Double
) : WrappedArgumentType<Double> {

    private val doubleType = DoubleArgumentType.doubleArg(min, max)

    override fun parse(reader: StringReader): Double {
        return doubleType.parse(reader)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return doubleType.listSuggestions(context, builder)
    }

    override fun getExamples(): MutableCollection<String> {
        return doubleType.examples
    }

    companion object {

        @JvmStatic
        fun double(min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE) = TypeDouble(min, max)

    }

}