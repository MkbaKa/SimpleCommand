package me.mkbaka.simplecommand.common.util.simple

import me.mkbaka.simplecommand.common.command.permission.PermissionDefault

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandHeader(
    val name: String,
    val aliases: Array<String> = [],
    val permission: String = "",
    val permissionDefault: PermissionDefault = PermissionDefault.REQUIRE
)
