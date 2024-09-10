package me.mkbaka.simplecommand.common.command.argument

/**
 * 给 java 用的
 *
 * 如果给数值类参数提供范围限定
 * 在命令执行时若实际参数不在范围中会直接抛出异常 不太好处理
 * 所以直接开摆! 自行在 executor 中判断一下吧(
 */
class TypeFactory {

    companion object {

        @JvmStatic
        fun intType() = TypeInt.int()

        @JvmStatic
        fun longType() = TypeLong.long()

        @JvmStatic
        fun floatType() = TypeFloat.float()

        @JvmStatic
        fun doubleType() = TypeDouble.double()

        @JvmStatic
        fun booleanType() = TypeBoolean.boolean()

        @JvmStatic
        fun word() = TypeString.word()

        @JvmStatic
        fun string() = TypeString.string()

        @JvmStatic
        fun greedyString() = TypeString.greedyString()

    }

}