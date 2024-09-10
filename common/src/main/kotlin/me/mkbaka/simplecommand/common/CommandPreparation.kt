package me.mkbaka.simplecommand.common

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.ParseResults
import me.mkbaka.simplecommand.common.command.component.CommandComponent

data class CommandPreparation(
    val currentComponent: CommandComponent<*>,
    val parseResults: ParseResults<CommandSource>,
    val dispatcher: CommandDispatcher<CommandSource>,
    val inputString: String
) {

    fun execute() {
        dispatcher.execute(parseResults)
    }

    fun getSuggestTexts() = dispatcher.getCompletionSuggestions(parseResults).get().list.map { it.text }

}