package me.mkbaka.simplecommand.common.command.argument

import me.mkbaka.simplecommand.common.command.argument.impl.*

/**
 * 给 java 用的
 *
 * kt的伴生对象在java里调用太丑了 所以用这个类来代替
 */
class TypeFactory {

    companion object {

        @JvmStatic
        fun intType() = TypeInt.int()

        @JvmStatic
        fun intType(min: Int) = TypeInt.int(min)

        @JvmStatic
        fun intType(min: Int, max: Int) = TypeInt.int(min, max)

        @JvmStatic
        fun longType() = TypeLong.long()

        @JvmStatic
        fun longType(min: Long) = TypeLong.long(min)

        @JvmStatic
        fun longType(min: Long, max: Long) = TypeLong.long(min, max)

        @JvmStatic
        fun floatType() = TypeFloat.float()

        @JvmStatic
        fun floatType(min: Float) = TypeFloat.float(min)

        @JvmStatic
        fun floatType(min: Float, max: Float) = TypeFloat.float(min, max)

        @JvmStatic
        fun doubleType() = TypeDouble.double()

        @JvmStatic
        fun doubleType(min: Double) = TypeDouble.double(min)

        @JvmStatic
        fun doubleType(min: Double, max: Double) = TypeDouble.double(min, max)

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