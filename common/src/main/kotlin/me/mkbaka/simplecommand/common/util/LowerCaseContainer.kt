package me.mkbaka.simplecommand.common.util

import java.util.concurrent.ConcurrentHashMap

class LowerCaseContainer<V : Any> : ConcurrentHashMap<String, V>() {

    override fun get(key: String): V? {
        return super.get(key.lowercase())
    }

    override fun put(key: String, value: V): V? {
        return super.put(key.lowercase(), value)
    }

}