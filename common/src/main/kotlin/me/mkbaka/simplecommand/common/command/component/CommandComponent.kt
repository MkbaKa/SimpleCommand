package me.mkbaka.simplecommand.common.command.component

import com.mojang.brigadier.builder.ArgumentBuilder
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.CommandNotify
import me.mkbaka.simplecommand.common.command.ExecutorContext
import me.mkbaka.simplecommand.common.command.argument.TypeFactory
import me.mkbaka.simplecommand.common.command.argument.impl.TypeString
import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import me.mkbaka.simplecommand.common.command.permission.Permissible
import me.mkbaka.simplecommand.common.command.permission.PermissionDefault
import java.util.function.Consumer

/**
 * 参数没有默认值的函数都是给java用的
 */
abstract class CommandComponent<R> : Permissible {

    /**
     * 父组件
     */
    var parent: CommandComponent<*>? = null
        private set

    /**
     * 子组件
     */
    val children = ArrayList<CommandComponent<*>>()

    /**
     * 参数错误回调
     */
    internal var invalidArgument: CommandNotify? = null
        private set

    /**
     * 未通过权限判断回调
     */
    internal var permissionFailure: CommandNotify? = null
        private set

    internal abstract fun build(): R

    /**
     * 设置参数错误时的回调
     *
     * @param [callback]
     */
    fun incorrectArgument(callback: CommandNotify) {
        this.invalidArgument = callback
    }

    /**
     * 设置权限判断未通过时的回调
     *
     * @param [callback]
     */
    override fun onPermissionCheckFailure(callback: CommandNotify) {
        this.permissionFailure = callback
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

    private fun append(subComponent: CommandComponent<*>): CommandComponent<*> {
        subComponent.parent = this
        children.add(subComponent)
        return this
    }

    open fun argument(
        name: String
    ): CommandComponent<*> {
        return argument(name) {}
    }

    open fun argument(
        name: String,
        callback: Consumer<DynamicComponent<*>>
    ): CommandComponent<*> {
        return argument(name, TypeFactory.word(), callback)
    }

    open fun argument(
        name: String,
        type: WrappedArgumentType<*>
    ): CommandComponent<*> {
        return argument(name, type, "", PermissionDefault.ALLOW)
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
        return dynamic(name, type, permission, permissionDefault) { callback.accept(this) }
    }

    open fun argument(
        name: String,
        type: WrappedArgumentType<*> = TypeString.word(),
        permission: String = "",
        permissionDefault: PermissionDefault = PermissionDefault.REQUIRE
    ): CommandComponent<*> {
        return argument(name, type, permission, permissionDefault) {}
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

//    open fun dynamic(
//        name: String
//    ): CommandComponent<*> {
//        return dynamic(name, TypeFactory.word(), "", PermissionDefault.ALLOW)
//    }
//
//    open fun dynamic(
//        name: String,
//        callback: Consumer<DynamicComponent<*>>
//    ): CommandComponent<*> {
//        return dynamic(name, TypeFactory.word(), "", PermissionDefault.ALLOW, callback)
//    }
//
//    open fun dynamic(
//        name: String,
//        type: WrappedArgumentType<*>
//    ): CommandComponent<*> {
//        return dynamic(name, type, "", PermissionDefault.ALLOW)
//    }
//
//    open fun dynamic(
//        name: String,
//        type: WrappedArgumentType<*>,
//        callback: Consumer<DynamicComponent<*>>
//    ): CommandComponent<*> {
//        return dynamic(name, type, "", PermissionDefault.ALLOW, callback)
//    }
//
//    open fun dynamic(
//        name: String,
//        type: WrappedArgumentType<*>,
//        permission: String,
//        permissionDefault: PermissionDefault
//    ): CommandComponent<*> {
//        return dynamic(name, type, permission, permissionDefault) {}
//    }
//
//    open fun dynamic(
//        name: String,
//        type: WrappedArgumentType<*>,
//        permission: String,
//        permissionDefault: PermissionDefault,
//        callback: Consumer<DynamicComponent<*>>
//    ): CommandComponent<*> {
//        return dynamic(name, type, permission, permissionDefault) { callback.accept(this) }
//    }

    open fun dynamic(
        name: String,
        type: WrappedArgumentType<*> = TypeString.word(),
        permission: String = "",
        permissionDefault: PermissionDefault = PermissionDefault.ALLOW,
        callback: DynamicComponent<*>.() -> Unit
    ): CommandComponent<*> {
        append(DynamicComponent(name, type, permission, permissionDefault).also(callback))
        return this
    }

    open fun literal(
        name: String
    ): CommandComponent<*> {
        return literal(name, emptyArray())
    }

    open fun literal(
        name: String,
        aliases: Array<String>
    ): CommandComponent<*> {
        return literal(name, aliases, "", PermissionDefault.ALLOW)
    }

    open fun literal(
        name: String,
        callback: Consumer<LiteralComponent>
    ): CommandComponent<*> {
        return literal(name, emptyArray(), "", PermissionDefault.ALLOW, callback)
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
        permissionDefault: PermissionDefault = PermissionDefault.REQUIRE
    ): CommandComponent<*> {
        return literal(name, aliases, permission, permissionDefault) {}
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

    open fun execute(consumer: Consumer<ExecutorContext>) {
        execute { consumer.accept(it) }
    }

    open fun execute(callback: (ExecutorContext) -> Unit) {
        append(ExecutorComponent().executor(callback))
    }

}