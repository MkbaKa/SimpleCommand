package me.mkbaka.simplecommand.common.command.wrapper

import me.mkbaka.simplecommand.common.command.component.CommandComponent

interface WrappedCommandNode {

    val component: CommandComponent<*>

}