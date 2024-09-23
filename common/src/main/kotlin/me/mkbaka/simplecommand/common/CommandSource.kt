package me.mkbaka.simplecommand.common

interface CommandSource {

    /**
     * 获取命令源的原始对象
     */
    val origin: Any

    fun hasPermission(perm: String): Boolean

    fun sendMessage(str: String)

}