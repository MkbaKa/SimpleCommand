package me.mkbaka.simplecommand.common.util.simple

import me.mkbaka.simplecommand.common.command.permission.PermissionDefault

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandBody(
    val aliases: Array<String> = [],
    val permission: String = "",
    val permissionDefault: PermissionDefault = PermissionDefault.REQUIRE
)
