package me.mkbaka.simplecommand.platform.argument

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

class TypePlayer : WrappedArgumentType<Player> {

    override fun parse(reader: StringReader): Player? {
        val name = reader.readUnquotedString()
        return Bukkit.getPlayerExact(name)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val input = builder.remaining.lowercase()
        Bukkit.getOnlinePlayers().forEach { player ->
            if (player.name.lowercase().startsWith(input)) {
                builder.suggest(player.name)
            }
        }
        return builder.buildFuture()
    }

    override fun getExamples(): MutableCollection<String> {
        return Bukkit.getOnlinePlayers().mapTo(ArrayList()) { it.name }
    }

    companion object {

        @JvmStatic
        fun player() = TypePlayer()

    }

}