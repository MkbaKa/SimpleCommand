package me.mkbaka.simplecommand.common.command

import com.mojang.brigadier.suggestion.SuggestionsBuilder

class SuggestionContext(
    private val builder: SuggestionsBuilder
) {

    val input: String
        get() = builder.input

    val remaining: String
        get() = builder.remaining

}