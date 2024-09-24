package me.mkbaka.simplecommand.common.util

import me.mkbaka.simplecommand.common.command.component.LiteralComponent
import me.mkbaka.simplecommand.common.command.component.RootComponent
import me.mkbaka.simplecommand.common.util.simple.CommandBody
import me.mkbaka.simplecommand.common.util.simple.CommandHeader

fun simpleComponentOf(obj: Any): RootComponent? {
    if (!obj::class.java.isAnnotationPresent(CommandHeader::class.java)) return null

    val header = obj::class.java.getAnnotation(CommandHeader::class.java)

    val commandHeader = LiteralComponent(header.name, header.aliases, header.permission, header.permissionDefault)

    return RootComponent(header.name, commandHeader).also { build(obj, commandHeader) }
}

private fun build(obj: Any, header: LiteralComponent) {
    obj::class.java.declaredFields.forEach { field ->
        if (!field.isAnnotationPresent(CommandBody::class.java)) return@forEach

        val body = field.getAnnotation(CommandBody::class.java)

        field.isAccessible = true
        when (val simple = field.get(obj)) {
            is SimpleSubCommand -> {
                header.literal(
                    field.name,
                    body.aliases,
                    body.permission,
                    body.permissionDefault,
                    simple.callback
                )
            }

            is SimpleMainCommand -> header.also(simple.callback)

            else -> build(
                simple,
                LiteralComponent(field.name, body.aliases, body.permission, body.permissionDefault).apply {
                    header.literal(this)
                }
            )
        }
    }
}

fun mainCommand(callback: LiteralComponent.() -> Unit) = SimpleMainCommand(callback)

fun subCommand(callback: LiteralComponent.() -> Unit) = SimpleSubCommand(callback)

class SimpleMainCommand(val callback: LiteralComponent.() -> Unit)

class SimpleSubCommand(val callback: LiteralComponent.() -> Unit)