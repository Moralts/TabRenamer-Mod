package com.tabrenamer.moralts.client.mixin;

import com.mojang.authlib.GameProfile;
import com.tabrenamer.moralts.client.ClientRuleStore;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public abstract class PlayerInfoMixin {
    @Shadow
    public abstract GameProfile getProfile();

    @Inject(method = "getTabListDisplayName", at = @At("RETURN"), cancellable = true)
    private void tabrenamer$modifyTabListDisplayName(CallbackInfoReturnable<Component> cir) {
        if (!com.tabrenamer.moralts.client.ClientConfig.INSTANCE.getEnableTabRenaming()) return;
        String playerName = this.getProfile().getName();
        String replacement = ClientRuleStore.INSTANCE.getReplacement(playerName);
        if (replacement != null) {
            cir.setReturnValue(com.tabrenamer.moralts.TextUtil.parseLegacyText(replacement));
        }
    }
}
