package me.mkbaka.simplecommand.common.command.permission

import me.mkbaka.simplecommand.common.CommandSource
import me.mkbaka.simplecommand.common.command.CommandNotify

interface Permissible {

    val permission: String

    val permissionDefault: PermissionDefault

    fun onPermissionCheckFailure(callback: CommandNotify)

    fun requirePermission() = permissionDefault == PermissionDefault.REQUIRE && permission.isNotEmpty()

}