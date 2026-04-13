package com.tabrenamer.moralts

import com.mojang.brigadier.arguments.StringArgumentType
import io.netty.buffer.Unpooled
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.Commands
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import org.slf4j.LoggerFactory

object TabRenamer : ModInitializer {
    private val logger = LoggerFactory.getLogger("tabrenamer")
    val SYNC_RULES_CHANNEL = ResourceLocation("tabrenamer", "sync_rules")

    override fun onInitialize() {
        RenameRuleManager.load()
        registerCommands()
        registerNetworking()
        logger.info("TabRenamer initialized")
    }

    private fun registerCommands() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                Commands.literal("tabrenamer")
                    .requires { it.hasPermission(2) }
                    .then(
                        Commands.literal("set")
                            .then(
                                Commands.argument("original", StringArgumentType.string())
                                    .then(
                                        Commands.argument("replacement", StringArgumentType.string())
                                            .executes { ctx ->
                                                val original = StringArgumentType.getString(ctx, "original")
                                                val replacement = StringArgumentType.getString(ctx, "replacement")
                                                RenameRuleManager.setRule(original, replacement)
                                                broadcastRules(ctx.source.server.playerList.players)
                                                ctx.source.sendSuccess(
                                                    { Component.translatable("tabrenamer.command.set.success", original, replacement) },
                                                    true
                                                )
                                                1
                                            }
                                    )
                            )
                    )
                    .then(
                        Commands.literal("remove")
                            .then(
                                Commands.argument("original", StringArgumentType.string())
                                    .executes { ctx ->
                                        val original = StringArgumentType.getString(ctx, "original")
                                        if (RenameRuleManager.removeRule(original)) {
                                            broadcastRules(ctx.source.server.playerList.players)
                                            ctx.source.sendSuccess(
                                                { Component.translatable("tabrenamer.command.remove.success", original) },
                                                true
                                            )
                                        } else {
                                            ctx.source.sendFailure(
                                                Component.translatable("tabrenamer.command.remove.not_found", original)
                                            )
                                        }
                                        1
                                    }
                            )
                    )
                    .then(
                        Commands.literal("list")
                            .executes { ctx ->
                                val rules = RenameRuleManager.getRules()
                                if (rules.isEmpty()) {
                                    ctx.source.sendSuccess(
                                        { Component.translatable("tabrenamer.command.list.empty") },
                                        false
                                    )
                                } else {
                                    ctx.source.sendSuccess(
                                        { Component.translatable("tabrenamer.command.list.header", rules.size.toString()) },
                                        false
                                    )
                                    rules.forEach { (original, replacement) ->
                                        ctx.source.sendSuccess(
                                            { Component.translatable("tabrenamer.command.list.entry", original, replacement) },
                                            false
                                        )
                                    }
                                }
                                1
                            }
                    )
                    .then(
                        Commands.literal("clear")
                            .executes { ctx ->
                                RenameRuleManager.clearRules()
                                broadcastRules(ctx.source.server.playerList.players)
                                ctx.source.sendSuccess(
                                    { Component.translatable("tabrenamer.command.clear.success") },
                                    true
                                )
                                1
                            }
                    )
            )
        })
    }

    private fun registerNetworking() {
        ServerPlayConnectionEvents.JOIN.register(ServerPlayConnectionEvents.Join { handler, _, _ ->
            syncRulesToPlayer(handler.player)
        })
    }

    private fun syncRulesToPlayer(player: ServerPlayer) {
        val buf = FriendlyByteBuf(Unpooled.buffer())
        buf.writeUtf(RenameRuleManager.toJson())
        ServerPlayNetworking.send(player, SYNC_RULES_CHANNEL, buf)
    }

    private fun broadcastRules(players: List<ServerPlayer>) {
        players.forEach { syncRulesToPlayer(it) }
    }
}