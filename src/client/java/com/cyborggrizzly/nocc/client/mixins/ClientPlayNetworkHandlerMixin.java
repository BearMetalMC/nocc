package com.cyborggrizzly.nocc.client.mixins;

import com.cyborggrizzly.nocc.client.NoccConfig;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Inject(method = "parseCommand", at = @At("RETURN"), cancellable = true)
    private void nocc$policyAdjust(String command, CallbackInfoReturnable cir) {
        var cfg = NoccConfig.get();
        Object ret = cir.getReturnValue();
        if (!(ret instanceof Enum<?> e)) return;

        String name = e.name();
        if ("SIGNATURE_REQUIRED".equals(name)) return; // keep safety

        boolean bypass = switch (cfg.mode) {
            case OFF -> true;
            case DANGEROUS_ONLY -> !cfg.matchesAny(command, cfg.confirm);
            case SAFE_LIST_ONLY ->  cfg.matchesAny(command, cfg.bypass);
            default -> false;
        };

        String desired = bypass ? "NO_ISSUES" : "PERMISSIONS_REQUIRED";
        if (!name.equals(desired)) {
            Enum<?> replacement = Enum.valueOf((Class) e.getDeclaringClass(), desired);
            cir.setReturnValue(replacement);
        }
    }
}
