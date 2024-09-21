package me.mkbaka.simplecommand.common.command

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.component.CommandComponent

class SuggestionContext(
    private val builder: SuggestionsBuilder,
    context: CommandContext<CommandSource>,
    component: CommandComponent<*>
) : ExecutorContext(context, component) {

    /**
     * 当前节点位置对应的文本内容
     */
    val remaining: String
        get() = builder.remaining

}