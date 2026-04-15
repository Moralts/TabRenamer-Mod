package com.tabrenamer.moralts

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path

object ServerConfig {
    private val logger = LoggerFactory.getLogger("tabrenamer")
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val configFile: Path = FabricLoader.getInstance().configDir.resolve("tabrenamer-server.json")

    var enableRules: Boolean = true
        private set

    fun load() {
        try {
            if (Files.exists(configFile)) {
                val data = gson.fromJson(Files.readString(configFile), ConfigData::class.java)
                enableRules = data.enableRules
                logger.info("Server config loaded: enableRules=$enableRules")
            }
        } catch (e: Exception) {
            logger.error("Failed to load server config", e)
        }
    }

    fun save() {
        try {
            Files.createDirectories(configFile.parent)
            Files.writeString(configFile, gson.toJson(ConfigData(enableRules)))
            logger.info("Server config saved: enableRules=$enableRules")
        } catch (e: Exception) {
            logger.error("Failed to save server config", e)
        }
    }

    fun setEnableRules(value: Boolean) {
        enableRules = value
        save()
    }

    private data class ConfigData(val enableRules: Boolean = true)
}
