package me.mkbaka.simplecommand.common.command.argument.impl

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import java.util.concurrent.CompletableFuture

class TypeFloat(
    min: Float,
    max: Float
) : WrappedArgumentType<Float> {

    private val floatType = FloatArgumentType.floatArg(min, max)

    override fun parse(reader: StringReader): Float {
        return floatType.parse(reader)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return floatType.listSuggestions(context, builder)
    }

    override fun getExamples(): MutableCollection<String> {
        return floatType.examples
    }

    companion object {

        @JvmStatic
        fun float(min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE) = TypeFloat(min, max)

    }

}