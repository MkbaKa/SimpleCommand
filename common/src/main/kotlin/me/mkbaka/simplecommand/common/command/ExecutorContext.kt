package me.mkbaka.simplecommand.common.command

import com.mojang.brigadier.context.CommandContext
import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.component.CommandComponent

open class ExecutorContext<S>(
    private val origin: CommandContext<out CommandSource>,
    val currentComponent: CommandComponent<*>,
    private val s: S
) {

    /**
     * 命令执行者
     */
    val source: S
        get() = s
        // 无法理解为什么 catch 不到这个 ClassCastException
        // 只能用个最傻逼的办法解决了
//        get() = (origin.source as S) ?: origin.source.origin as S
//        get() = try {
//            origin.source as S
//        } catch (e: Throwable) {
//        } catch (e: ClassCastException) {
//            println("exception!!!")
//            origin.source.origin as S
//        }

    /**
     * 完整命令文本
     */
    val input: String
        get() = origin.input

    /**
     * 获取参数
     * 用于像 context["xxx"] 这样简略获取字符串类型参数
     *
     * @param [key]
     * @return [String?]
     */
    operator fun get(key: String): String? {
        return getArgument(key, String::class.java)
    }

    /**
     * 获取参数
     *
     * @param [key]
     * @return [T?]
     */
    inline fun <reified T> get(key: String): T? {
        return getArgument(key, T::class.java)
    }

    /**
     * java
     * 获取参数
     *
     * @param [key]
     * @param [type]
     * @return [T?]
     */
    fun <T> getBy(key: String, type: Class<T>): T? {
        return getArgument(key, type)
    }

    /**
     * 获取参数
     *
     * @param [key]
     * @return [T?]
     */
    inline fun <reified T> getBy(key: String): T? {
        return getArgument(key, T::class.java)
    }

    /**
     * 获取参数
     *
     * @param [key]
     * @param [type]
     * @return [T?]
     */
    fun <T> getArgument(key: String, type: Class<T>): T? {
        return try {
            origin.getArgument(key, type)
//        } catch (e: IllegalArgumentException | NullPointerException) {
            //         传入的 key 不存在
        } catch (e: IllegalArgumentException) {
            null
            //         ArgumentType 获取到的结果为 null
        } catch (e: NullPointerException) {
            null
        }
    }

}