package com.cyborggrizzly.nocc.client.mixins;

import com.cyborggrizzly.nocc.client.NoccConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Method;
import java.util.Locale;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "parseCommand", at = @At("RETURN"), cancellable = true)
    private void nocc$policyAdjust(String command, CallbackInfoReturnable cir) {
        var cfg = NoccConfig.get();

        Enum<?> current = (Enum<?>) cir.getReturnValue();
        if (current == null) return;

        String name = current.name();
        if ("SIGNATURE_REQUIRED".equals(name)) return;

        boolean bypass;
        switch (cfg.mode) {
            case OFF -> bypass = true;
            case VANILLA -> bypass = false;
            case DANGEROUS_ONLY -> bypass = !cfg.matchesAny(command, cfg.confirm);
            case SAFE_LIST_ONLY -> bypass = cfg.matchesAny(command, cfg.bypass);
            default -> bypass = false;
        }

        String desired = bypass ? "NO_ISSUES" : "PERMISSIONS_REQUIRED";
        if (!name.equals(desired)) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            Enum<?> replacement = Enum.valueOf((Class) current.getClass(), desired);
            cir.setReturnValue(replacement);
        }
    }
}
