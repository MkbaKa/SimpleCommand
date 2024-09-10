package me.mkbaka.simplecommand.common.command

import me.mkbaka.simplecommand.common.CommandSource

class CommandNotify(
    private val callback: (source: CommandSource, input: String, header: String, args: Array<String>) -> Unit
) {

    constructor(callback: CommandNotifyCallback) : this(callback::execute)

    operator fun invoke(source: CommandSource, input: String, header: String, args: Array<String>) {
        callback(source, input, header, args)
    }

}

@FunctionalInterface
interface CommandNotifyCallback {

    fun execute(source: CommandSource, input: String, header: String, args: Array<String>)

}