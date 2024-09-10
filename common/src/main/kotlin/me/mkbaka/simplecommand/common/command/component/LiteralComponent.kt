package me.mkbaka.simplecommand.common.command.component

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.permission.PermissionDefault
import me.mkbaka.simplecommand.common.command.wrapper.SimpleLiteralBuilder

class LiteralComponent(
    val name: String,
    val aliases: Array<String> = emptyArray(),
    override val permission: String,
    override val permissionDefault: PermissionDefault
) : CommandComponent<List<LiteralArgumentBuilder<CommandSource>>>() {

    override fun build(): List<LiteralArgumentBuilder<CommandSource>> {
        return arrayOf(name, *aliases).map { build(it) }
    }

    private fun build(name: String): LiteralArgumentBuilder<CommandSource> {
        return SimpleLiteralBuilder(this, name).apply {
            children.forEach { child ->
                register(child)
            }
        }
    }

    override fun toString(): String {
        return "LiteralComponent{name=$name,aliases=${aliases.contentToString()},children=${children.joinToString()}}"
    }

}