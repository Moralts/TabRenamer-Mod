package com.tabrenamer.moralts

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style

object TextUtil {
    private val COLOR_CODE_PATTERN = Regex("&([0-9a-fA-Fk-oK-OrR])")

    @JvmStatic
    fun translateColorCodes(text: String): String =
        COLOR_CODE_PATTERN.replace(text) { "§${it.groupValues[1].lowercase()}" }

    @JvmStatic
    fun parseLegacyText(text: String): MutableComponent {
        val result = Component.empty()
        var style = Style.EMPTY
        val buffer = StringBuilder()
        var i = 0

        while (i < text.length) {
            if (text[i] == '§' && i + 1 < text.length) {
                val formatting = ChatFormatting.getByCode(text[i + 1])
                if (formatting != null) {
                    if (buffer.isNotEmpty()) {
                        result.append(Component.literal(buffer.toString()).withStyle(style))
                        buffer.clear()
                    }
                    style = when {
                        formatting == ChatFormatting.RESET -> Style.EMPTY
                        formatting.isFormat -> when (formatting) {
                            ChatFormatting.OBFUSCATED -> style.withObfuscated(true)
                            ChatFormatting.BOLD -> style.withBold(true)
                            ChatFormatting.STRIKETHROUGH -> style.withStrikethrough(true)
                            ChatFormatting.UNDERLINE -> style.withUnderlined(true)
                            ChatFormatting.ITALIC -> style.withItalic(true)
                            else -> style
                        }
                        else -> Style.EMPTY.withColor(formatting)
                    }
                    i += 2
                    continue
                }
            }
            buffer.append(text[i])
            i++
        }

        if (buffer.isNotEmpty()) {
            result.append(Component.literal(buffer.toString()).withStyle(style))
        }
        return result
    }
}
