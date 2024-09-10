package me.mkbaka.simplecommand.common.command

import com.mojang.brigadier.context.CommandContext
import me.mkbaka.simplecommand.common.CommandSource

data class ExecutorContext(
    internal val original: CommandContext<CommandSource>
) {

    val source: CommandSource
        get() = original.source

    val input: String
        get() = original.input

    operator fun get(key: String): String? {
        return getArgument(key, String::class.java)
    }

    inline fun <reified T> get(key: String): T? {
        return getArgument(key, T::class.java)
    }

    fun <T> getBy(key: String, type: Class<T>): T? {
        return getArgument(key, type)
    }

    inline fun <reified T> getBy(key: String): T? {
        return getArgument(key, T::class.java)
    }

    fun <T> getArgument(key: String, type: Class<T>): T? {
        return try {
            original.getArgument(key, type)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

}