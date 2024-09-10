package me.mkbaka.simplecommand.common.command.component

import com.mojang.brigadier.builder.ArgumentBuilder
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.CommandNotify
import me.mkbaka.simplecommand.common.command.ExecutorContext
import me.mkbaka.simplecommand.common.command.argument.TypeFactory
import me.mkbaka.simplecommand.common.command.argument.TypeString
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import me.mkbaka.simplecommand.common.command.permission.Permissible
import me.mkbaka.simplecommand.common.command.permission.PermissionDefault
import java.util.function.Consumer

/**
 * 参数没有默认值的函数都是给java用的
 */
abstract class CommandComponent<R> : Permissible {

    var parent: CommandComponent<*>? = null
        private set

    val children = ArrayList<CommandComponent<*>>()

    internal var invalidArgument: CommandNotify? = null
        private set

    internal var permissionFailure: CommandNotify? = null
        private set

    internal abstract fun build(): R

    fun incorrectArgument(callback: CommandNotify) {
        this.invalidArgument = callback
    }

    override fun onPermissionCheckFailure(callback: CommandNotify) {
        this.permissionFailure = callback
    }

    open fun append(subComponent: CommandComponent<*>): CommandComponent<*> {
        subComponent.parent = this
        children.add(subComponent)
        return this
    }

    open fun argument(
        name: String,
        callback: Consumer<DynamicComponent<*>>
    ): CommandComponent<*> {
        return dynamic(name, callback)
    }

    open fun argument(
        name: String,
        type: WrappedArgumentType<*>,
        callback: Consumer<DynamicComponent<*>>
    ): CommandComponent<*> {
        return argument(name, type, "", PermissionDefault.ALLOW, callback)
    }

    open fun argument(
        name: String,
        type: WrappedArgumentType<*>,
        permission: String,
        permissionDefault: PermissionDefault,
        callback: Consumer<DynamicComponent<*>>
    ): CommandComponent<*> {
        return dynamic(name, type, permission, permissionDefault, callback)
    }

    open fun argument(
        name: String,
        type: WrappedArgumentType<*> = TypeString.word(),
        permission: String = "",
        permissionDefault: PermissionDefault = PermissionDefault.REQUIRE,
        callback: DynamicComponent<*>.() -> Unit
    ): CommandComponent<*> {
        return dynamic(name, type, permission, permissionDefault, callback)
    }

    open fun dynamic(
        name: String,
        callback: Consumer<DynamicComponent<*>>
    ): CommandComponent<*> {
        return dynamic(name, TypeFactory.word(), "", PermissionDefault.ALLOW, callback)
    }

    open fun dynamic(
        name: String,
        type: WrappedArgumentType<*>,
        callback: Consumer<DynamicComponent<*>>
    ): CommandComponent<*> {
        return dynamic(name, type, "", PermissionDefault.ALLOW, callback)
    }

    open fun dynamic(
        name: String,
        type: WrappedArgumentType<*>,
        permission: String,
        permissionDefault: PermissionDefault,
        callback: Consumer<DynamicComponent<*>>
    ): CommandComponent<*> {
        return dynamic(name, type, permission, permissionDefault) { callback.accept(this) }
    }

    open fun dynamic(
        name: String,
        type: WrappedArgumentType<*> = TypeString.word(),
        permission: String = "",
        permissionDefault: PermissionDefault = PermissionDefault.REQUIRE,
        callback: DynamicComponent<*>.() -> Unit
    ): CommandComponent<*> {
        append(DynamicComponent(name, type, permission, permissionDefault).also(callback))
        return this
    }

    open fun literal(
        name: String,
        aliases: Array<String>,
        callback: Consumer<LiteralComponent>
    ): CommandComponent<*> {
        return literal(name, aliases, "", PermissionDefault.ALLOW, callback)
    }

    open fun literal(
        name: String,
        aliases: Array<String>,
        permission: String,
        permissionDefault: PermissionDefault,
        callback: Consumer<LiteralComponent>
    ): CommandComponent<*> {
        return literal(name, aliases, permission, permissionDefault) { callback.accept(this) }
    }

    open fun literal(
        name: String,
        aliases: Array<String> = arrayOf(),
        permission: String = "",
        permissionDefault: PermissionDefault = PermissionDefault.REQUIRE,
        callback: LiteralComponent.() -> Unit
    ): CommandComponent<*> {
        append(LiteralComponent(name, aliases, permission, permissionDefault).also(callback))
        return this
    }

    open fun execute(consumer: Consumer<ExecutorContext>): CommandComponent<*> {
        execute { consumer.accept(it) }
        return this
    }

    open fun execute(callback: (ExecutorContext) -> Unit): CommandComponent<*> {
        append(ExecutorComponent().executor(callback))
        return this
    }

    protected fun ArgumentBuilder<CommandSource, *>.register(component: CommandComponent<*>) {
        when (component) {
            is ExecutorComponent -> executes {
                component.executor!!.invoke(ExecutorContext(it))
                1
            }

            is LiteralComponent -> component.build().forEach(this::then)

            is DynamicComponent<*> -> then(component.build())
            else -> error("Unexpected child type: $component")
        }
    }

}