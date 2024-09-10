package me.mkbaka.simplecommand.common.command.component

import com.mojang.brigadier.builder.RequiredArgumentBuilder
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.ExecutorContext
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import me.mkbaka.simplecommand.common.command.permission.PermissionDefault

class ExecutorComponent : CommandComponent<RequiredArgumentBuilder<CommandSource, String>>() {

    override val permission: String
        get() = ""

    override val permissionDefault: PermissionDefault
        get() = PermissionDefault.ALLOW

    internal var executor: ((ExecutorContext) -> Unit)? = null

    fun executor(executor: (ExecutorContext) -> Unit): ExecutorComponent {
        this.executor = executor
        return this
    }

    override fun append(subComponent: CommandComponent<*>): CommandComponent<*> {
        throw IllegalStateException("ExecutorComponent cannot append children")
    }

    override fun argument(
        name: String,
        type: WrappedArgumentType<*>,
        permission: String,
        permissionDefault: PermissionDefault,
        callback: DynamicComponent<*>.() -> Unit
    ): CommandComponent<*> {
        throw IllegalStateException("ExecutorComponent cannot append children")
    }


    override fun dynamic(
        name: String,
        type: WrappedArgumentType<*>,
        permission: String,
        permissionDefault: PermissionDefault,
        callback: DynamicComponent<*>.() -> Unit
    ): CommandComponent<*> {
        throw IllegalStateException("ExecutorComponent cannot append children")
    }

    override fun literal(
        name: String,
        aliases: Array<String>,
        permission: String,
        permissionDefault: PermissionDefault,
        callback: LiteralComponent.() -> Unit
    ): CommandComponent<*> {
        throw IllegalStateException("ExecutorComponent cannot append children")
    }

    // 永远都不会使用这个函数
    override fun build(): RequiredArgumentBuilder<CommandSource, String> {
        throw IllegalStateException("ExecutorComponent cannot build")
    }

    override fun toString(): String {
        return "ExecutorComponent{}"
    }

}