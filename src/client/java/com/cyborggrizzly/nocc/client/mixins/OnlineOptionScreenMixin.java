package com.cyborggrizzly.nocc.client.mixins;

import com.cyborggrizzly.nocc.Nocc;
import com.cyborggrizzly.nocc.client.NoccClientInit;
import com.cyborggrizzly.nocc.client.NoccConfig;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.options.OnlineOptionsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OnlineOptionsScreen.class)
public abstract class OnlineOptionScreenMixin extends OptionsSubScreen {
    private OnlineOptionScreenMixin() {
        super(null, null, null);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void nocc$addToggle(CallbackInfo ci) {
        Nocc.LOGGER.info("Creating OnlineOptionsScreen widget");

        var opt = NoccConfig.confirmModeOption();

        this.list.addBig(opt);

        this.list.findOption(opt).active = !NoccClientInit.serverLocked;
        this.list.findOption(opt).setTooltip(
                NoccClientInit.serverLocked ? Tooltip.create(Component.translatable("nocc.options.locked.tooltip"))
                        : null);
    }
}
