package me.mkbaka.simplecommand.common

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.ParseResults
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import me.mkbaka.simplecommand.common.command.component.CommandComponent
import me.mkbaka.simplecommand.common.command.component.LiteralComponent
import me.mkbaka.simplecommand.common.command.component.RootComponent
import me.mkbaka.simplecommand.common.command.wrapper.WrappedCommandNode
import me.mkbaka.simplecommand.common.exceptions.CommandNotFoundException
import me.mkbaka.simplecommand.common.util.LowerCaseContainer
import me.mkbaka.simplecommand.common.util.simpleComponentOf
import java.util.logging.Logger

abstract class CommandRegistry(
    private val logger: Logger
) {

    private val commands = LowerCaseContainer<RootComponent>()
    private val aliasToHeader = LowerCaseContainer<String>()

    private val dispatchers = LowerCaseContainer<CommandDispatcher<CommandSource>>()

    private val exceptionTypes = hashSetOf(
        DynamicCommandExceptionType::class.java,
        Dynamic2CommandExceptionType::class.java,
        SimpleCommandExceptionType::class.java
    )

    abstract fun registerToPlatform(component: LiteralComponent): Boolean

    /**
     * 获取 header 对应的 RootComponent
     * @param [header]
     */
    fun getCommand(header: String) = header.rootComponent

    /**
     * 获取已注册的所有 RootComponent
     */
    fun getCommands() = commands

    /**
     * 用于懒蛋式注册
     *
     * @param [obj]
     */
    fun register(obj: Any) {
        val rootComponent = simpleComponentOf(obj)
            ?: return logger.warning("Annotation @CommandHeader not found on ${obj::class.java.name}")

        register(rootComponent)
    }

    /**
     * 注册命令
     *
     * @param [rootComponent]
     */
    fun register(rootComponent: RootComponent) {
        if (commands.containsKey(rootComponent.name)) error("Command ${rootComponent.name} already registered")

        if (!registerToPlatform(rootComponent.subComponent)) {
            return logger.warning("Failed to register command ${rootComponent.name} to platform")
        }

        val dispatcher = CommandDispatcher(rootComponent)

        rootComponent.subComponent.build().forEach(dispatcher::register)

        rootComponent.subComponent.aliases.forEach { alias ->
            if (aliasToHeader.containsKey(alias)) {
                return@forEach logger.warning("Alias $alias already registered by command ${aliasToHeader[alias]}")
            }

            aliasToHeader[alias] = rootComponent.name.lowercase()
        }
        commands[rootComponent.name] = rootComponent
        dispatchers[rootComponent.name] = dispatcher
    }

    /**
     * 执行命令
     *
     * @param [source] 执行源
     * @param [header] 头节点名
     * @param [args] 参数
     */
    @Throws(CommandNotFoundException::class)
    fun execute(source: CommandSource, header: String, args: Array<String>) {
        val preparation = prepareCommand(source, header, args)

        if (preparation.currentComponent.isFailure(source)) {
            return findNotify(
                preparation.currentComponent,
                "Cannot find permission notify for this commands."
            ) { comp ->
                comp.permissionFailure
            }.invoke(source, preparation.inputString, header, args)
        }

        try {
            preparation.execute()
        } catch (e: CommandSyntaxException) {
            if (e.type::class.java in exceptionTypes) {
                findNotify(preparation.currentComponent, "Cannot find argument notify for this commands.") { comp ->
                    comp.invalidArgument
                }.invoke(source, preparation.inputString, header, args)
            } else {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取纯文本建议
     *
     * @param [source] 执行源
     * @param [header] 头节点
     * @param [args] 参数
     * @return [List<String>]
     */
    fun tabCompleter(source: CommandSource, header: String, args: Array<String>): List<String> {
        val preparation = prepareCommand(source, header, args)

        if (preparation.currentComponent.isFailure(source)) {
            return emptyList()
        }

        return preparation.getSuggestTexts()
    }

    private val String.rootComponent: RootComponent?
        get() = commands[this] ?: commands[aliasToHeader[this]]

    private val String.dispatcher: CommandDispatcher<CommandSource>?
        get() = dispatchers[this]

    private fun prepareCommand(source: CommandSource, header: String, args: Array<String>): CommandPreparation {
        val (rootComponent, dispatcher) = get(header)
        val input = if (args.isEmpty()) rootComponent.name else "${rootComponent.name} ${args.joinToString(" ")}"
        val parsed = dispatcher.parse(input, source)
        return CommandPreparation(parsed.getCurrentComponent(), parsed, dispatcher, input)
    }

    private fun get(header: String): Pair<RootComponent, CommandDispatcher<CommandSource>> {
        val rootComponent = header.rootComponent ?: throw CommandNotFoundException("Command $header not found")
        val dispatcher = header.dispatcher ?: throw CommandNotFoundException("Command $header not found")
        return Pair(rootComponent, dispatcher)
    }

    private fun <T : Any> findNotify(
        start: CommandComponent<*>,
        errorMsg: String,
        selector: (CommandComponent<*>) -> T?
    ): T {
        return generateSequence(start) { it.parent }
            .mapNotNull(selector)
            .firstOrNull()
            ?: throw IllegalStateException(errorMsg)
    }

    private fun ParseResults<*>.getCurrentComponent(): CommandComponent<*> {
        return (this.context.nodes.lastOrNull { it.node is WrappedCommandNode }?.node as? WrappedCommandNode)?.component
            ?: error("Cannot find current component")
    }

}