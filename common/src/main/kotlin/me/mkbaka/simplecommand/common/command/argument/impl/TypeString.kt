package me.mkbaka.simplecommand.common.command.argument.impl

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.StringReader.isQuotedStringStart
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

    /**
     * 原方法 com.mojang.brigadier.StringReader#readUnquotedString
     * 仅支持 0-9 a-z A-Z  _ - . +
     * 不符合需求
     */
    private fun readUnquotedString(reader: StringReader): String {
        val start = reader.cursor
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip()
        }
        return reader.string.substring(start, reader.cursor)
    }

    private fun readString(reader: StringReader): String {
        if (!reader.canRead()) {
            return "";
        }
        val next = reader.peek();
        if (isQuotedStringStart(next)) {
            reader.skip();
            return reader.readStringUntil(next);
        }
        return readUnquotedString(reader);
    }

    override fun parse(reader: StringReader): String {
        return when (stringType.type) {
            StringArgumentType.StringType.SINGLE_WORD -> readUnquotedString(reader)

            StringArgumentType.StringType.QUOTABLE_PHRASE -> readString(reader)

            else -> stringType.parse(reader)
        }
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