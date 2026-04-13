package com.tabrenamer.moralts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path

object RenameRuleManager {
    private val logger = LoggerFactory.getLogger("tabrenamer")
    private val gson = Gson()
    private val rules = mutableMapOf<String, String>()
    private val configDir: Path = FabricLoader.getInstance().configDir.resolve("tabrenamer")
    private val rulesFile: Path = configDir.resolve("rules.json")

    fun load() {
        try {
            if (Files.exists(rulesFile)) {
                val json = Files.readString(rulesFile)
                val type = object : TypeToken<Map<String, String>>() {}.type
                val loaded: Map<String, String> = gson.fromJson(json, type)
                rules.clear()
                rules.putAll(loaded)
                logger.info("Loaded ${rules.size} rename rule(s)")
            }
        } catch (e: Exception) {
            logger.error("Failed to load rename rules", e)
        }
    }

    private fun save() {
        try {
            Files.createDirectories(configDir)
            Files.writeString(rulesFile, gson.toJson(rules))
        } catch (e: Exception) {
            logger.error("Failed to save rename rules", e)
        }
    }

    fun setRule(original: String, replacement: String) {
        rules[original] = replacement
        save()
    }

    fun removeRule(original: String): Boolean {
        val removed = rules.remove(original) != null
        if (removed) save()
        return removed
    }

    fun clearRules() {
        rules.clear()
        save()
    }

    fun getRules(): Map<String, String> = rules.toMap()

    fun toJson(): String = gson.toJson(rules)
}
