package me.mkbaka.simplecommand.platform

import org.bukkit.Bukkit

internal object ServerVersion {

    val runningVersion by lazy {
        // git-Paper-1620 (MC: 1.12.2)
        val version = Bukkit.getServer().version.split("MC:")[1]
        // 1.12.2
        version.substring(0, version.length - 1).trim()
    }

    val supportedVersion = arrayOf(
        arrayOf("1.8", "1.8.3", "1.8.4", "1.8.5", "1.8.6", "1.8.7", "1.8.8", "1.8.9"),   // 0
        arrayOf("1.9", "1.9.2", "1.9.4"),                                                // 1
        arrayOf("1.10.2"),                                                               // 2
        arrayOf("1.11", "1.11.2"),                                                       // 3
        arrayOf("1.12", "1.12.1", "1.12.2"),                                             // 4
        arrayOf("1.13", "1.13.1", "1.13.2"),                                             // 5
        arrayOf("1.14", "1.14.1", "1.14.2", "1.14.3", "1.14.4"),                         // 6
        arrayOf("1.15", "1.15.1", "1.15.2"),                                             // 7
        arrayOf("1.16.1", "1.16.2", "1.16.3", "1.16.4", "1.16.5"),                       // 8
        arrayOf("1.17", "1.17.1"),                                                       // 9 (universal)
        arrayOf("1.18", "1.18.1", "1.18.2"),                                             // 10
        arrayOf("1.19", "1.19.1", "1.19.2", "1.19.3", "1.19.4"),                         // 11
        arrayOf("1.20", "1.20.1", "1.20.2", "!1.20.3", "1.20.4", "!1.20.5", "1.20.6"),   // 12 (跳过 1.20.3、1.20.5)
        arrayOf("1.21")
    )

    val major by lazy {
        supportedVersion.indexOfFirst { it.contains(runningVersion) }
    }

}