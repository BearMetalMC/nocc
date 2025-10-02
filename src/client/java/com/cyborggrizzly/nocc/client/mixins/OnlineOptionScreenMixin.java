package com.cyborggrizzly.nocc.client.mixins;

import com.cyborggrizzly.nocc.client.NoccConfig;
import com.cyborggrizzly.nocc.client.OptionListCompat;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.OnlineOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OnlineOptionsScreen.class)
public abstract class OnlineOptionScreenMixin extends GameOptionsScreen {
    private OnlineOptionScreenMixin() {
        super(null, null, null);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void nocc$addToggle(CallbackInfo ci) {
        OptionListWidget body = ((GameOptionsScreenAccessor) this).nocc$getBody();
        if (body == null) return;

        var opt = NoccConfig.confirmModeOption();

        var inv = (OptionListWidgetInvoker) (Object) body;
        inv.nocc$addSingle(opt);
    }
}
