package me.mkbaka.simplecommand.common.command.component

import me.mkbaka.simplecommand.common.command.argument.WrappedArgumentType
import me.mkbaka.simplecommand.common.command.permission.PermissionDefault
import me.mkbaka.simplecommand.common.command.wrapper.SimpleDynamicBuilder

/**
 * 参数组件
 */
class DynamicComponent<T>(
    val name: String,
    private val type: WrappedArgumentType<T>,
    override val permission: String,
    override val permissionDefault: PermissionDefault
) : CommandComponent<SimpleDynamicBuilder<T>>() {

    override fun build(): SimpleDynamicBuilder<T> {
        return SimpleDynamicBuilder(this, name, type).apply {
            children.forEach { child -> register(child) }
        }
    }

    override fun toString(): String {
        return "DynamicComponent{name=$name, type=$type, children=${children.joinToString()}}"
    }

}