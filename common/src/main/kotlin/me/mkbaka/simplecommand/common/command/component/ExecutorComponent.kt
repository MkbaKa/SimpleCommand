package me.mkbaka.simplecommand.common.command.component

import com.mojang.brigadier.builder.RequiredArgumentBuilder
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.ExecutorContext
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import me.mkbaka.simplecommand.common.command.permission.PermissionDefault

/**
 * 命令执行组件
 */
class ExecutorComponent<S>(
    val source: Class<*>
) : CommandComponent<RequiredArgumentBuilder<S, String>>() {

    override val permission: String
        get() = ""

    override val permissionDefault: PermissionDefault
        get() = PermissionDefault.ALLOW

    internal var executor: ((ExecutorContext<S>) -> Unit)? = null

    fun executor(executor: (ExecutorContext<S>) -> Unit): ExecutorComponent<S> {
        this.executor = executor
        return this
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
    override fun build(): RequiredArgumentBuilder<S, String> {
        throw IllegalStateException("ExecutorComponent cannot build")
    }

    override fun toString(): String {
        return "ExecutorComponent{}"
    }

}