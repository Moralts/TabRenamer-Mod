package com.tabrenamer.moralts.client

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path

object ClientConfig {
    private val logger = LoggerFactory.getLogger("tabrenamer-client")
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val configFile: Path = FabricLoader.getInstance().configDir.resolve("tabrenamer-client.json")

    var enableTabRenaming: Boolean = true
        private set

    fun load() {
        try {
            if (Files.exists(configFile)) {
                val data = gson.fromJson(Files.readString(configFile), ConfigData::class.java)
                enableTabRenaming = data.enableTabRenaming
            }
        } catch (e: Exception) {
            logger.error("Failed to load client config", e)
        }
    }

    fun save() {
        try {
            Files.createDirectories(configFile.parent)
            Files.writeString(configFile, gson.toJson(ConfigData(enableTabRenaming)))
        } catch (e: Exception) {
            logger.error("Failed to save client config", e)
        }
    }

    fun setEnableTabRenaming(value: Boolean) {
        enableTabRenaming = value
    }

    private data class ConfigData(val enableTabRenaming: Boolean = true)
}
