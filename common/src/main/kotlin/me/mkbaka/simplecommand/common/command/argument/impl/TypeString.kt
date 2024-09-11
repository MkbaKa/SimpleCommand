package me.mkbaka.simplecommand.common.command.argument.impl

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import java.util.concurrent.CompletableFuture

class TypeString(
    type: StringType
) : WrappedArgumentType<String> {

    private val stringType = type.get()

    override fun parse(reader: StringReader): String {
        return stringType.parse(reader)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return stringType.listSuggestions(context, builder)
    }

    override fun getExamples(): MutableCollection<String> {
        return stringType.examples
    }

    enum class StringType {
        SINGLE_WORD {
            override fun get(): StringArgumentType {
                return StringArgumentType.word()
            }
        },
        QUOTABLE_PHRASE {
            override fun get(): StringArgumentType {
                return StringArgumentType.string()
            }
        },
        GREEDY_PHRASE {
            override fun get(): StringArgumentType {
                return StringArgumentType.greedyString()
            }
        };

        abstract fun get(): StringArgumentType
    }

    companion object {

        @JvmStatic
        fun word() = TypeString(StringType.SINGLE_WORD)

        @JvmStatic
        fun string() = TypeString(StringType.QUOTABLE_PHRASE)

        @JvmStatic
        fun greedyString() = TypeString(StringType.GREEDY_PHRASE)

    }

}