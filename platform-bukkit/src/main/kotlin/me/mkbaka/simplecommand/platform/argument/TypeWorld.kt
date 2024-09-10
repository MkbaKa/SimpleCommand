package me.mkbaka.simplecommand.platform.argument

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import org.bukkit.Bukkit
import org.bukkit.World
import java.util.concurrent.CompletableFuture

class TypeWorld : WrappedArgumentType<World> {

    override fun parse(reader: StringReader): World? {
        return Bukkit.getWorld(reader.readUnquotedString())
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val input = builder.remaining.lowercase()
        Bukkit.getWorlds().forEach { world ->
            if (world.name.startsWith(input)) {
                builder.suggest(world.name)
            }
        }
        return builder.buildFuture()
    }

    override fun getExamples(): MutableCollection<String> {
        return Bukkit.getWorlds().mapTo(ArrayList()) { it.name }
    }

    companion object {

        @JvmStatic
        fun world() = TypeWorld()

    }

}