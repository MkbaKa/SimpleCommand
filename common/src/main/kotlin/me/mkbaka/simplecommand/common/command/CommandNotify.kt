package me.mkbaka.simplecommand.common.command

import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.component.CommandComponent

class CommandNotify(
    private val callback: (source: CommandSource, currentComponent: CommandComponent<*>, input: String, header: String, args: Array<String>) -> Unit
) {

    constructor(callback: CommandNotifyCallback) : this(callback::execute)

    operator fun invoke(
        source: CommandSource,
        currentComponent: CommandComponent<*>,
        input: String,
        header: String,
        args: Array<String>
    ) {
        callback(source, currentComponent, input, header, args)
    }

}

@FunctionalInterface
interface CommandNotifyCallback {

    fun execute(
        source: CommandSource,
        currentComponent: CommandComponent<*>,
        input: String,
        header: String,
        args: Array<String>
    )

}