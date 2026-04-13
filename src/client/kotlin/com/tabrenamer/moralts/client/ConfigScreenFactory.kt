package com.tabrenamer.moralts.client

import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

object ConfigScreenFactory {
    fun create(parent: Screen?): Screen {
        val builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.translatable("tabrenamer.config.title"))
            .setSavingRunnable {
                ClientConfig.save()
            }

        val general = builder.getOrCreateCategory(
            Component.translatable("tabrenamer.config.category.general")
        )

        general.addEntry(
            builder.entryBuilder()
                .startBooleanToggle(
                    Component.translatable("tabrenamer.config.enable_tab_renaming"),
                    ClientConfig.enableTabRenaming
                )
                .setDefaultValue(true)
                .setTooltip(Component.translatable("tabrenamer.config.enable_tab_renaming.tooltip"))
                .setSaveConsumer { ClientConfig.setEnableTabRenaming(it) }
                .build()
        )

        return builder.build()
    }
}
