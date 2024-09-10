package me.mkbaka.simplecommand.common

interface CommandSource {

    fun hasPermission(perm: String): Boolean

    fun sendMessage(str: String)

}