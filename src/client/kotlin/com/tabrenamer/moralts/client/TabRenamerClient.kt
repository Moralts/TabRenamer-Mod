package com.tabrenamer.moralts.client

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.resources.ResourceLocation
import org.slf4j.LoggerFactory

object TabRenamerClient : ClientModInitializer {
    private val logger = LoggerFactory.getLogger("tabrenamer-client")
    private val gson = Gson()
    private val SYNC_RULES_CHANNEL = ResourceLocation("tabrenamer", "sync_rules")

    override fun onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_RULES_CHANNEL) { _, _, buf, _ ->
            val json = buf.readUtf()
            val type = object : TypeToken<Map<String, String>>() {}.type
            val rules: Map<String, String> = gson.fromJson(json, type)
            ClientRuleStore.updateRules(rules)
            logger.debug("Received ${rules.size} rename rule(s) from server")
        }

        ClientPlayConnectionEvents.DISCONNECT.register(ClientPlayConnectionEvents.Disconnect { _, _ ->
            ClientRuleStore.clear()
        })
    }
}