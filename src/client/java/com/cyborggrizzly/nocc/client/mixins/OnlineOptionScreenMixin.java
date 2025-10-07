package com.cyborggrizzly.nocc.client.mixins;

import com.cyborggrizzly.nocc.client.NoccClientInit;
import com.cyborggrizzly.nocc.client.NoccConfig;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.OnlineOptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.text.Text;

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
        if (body == null)
            return;

        var opt = NoccConfig.confirmModeOption();
        ((OptionListWidgetInvoker) (Object) body).nocc$addSingle(opt);
        var widget = body.getWidgetFor(opt);

        if (widget != null) {
            widget.active = !NoccClientInit.serverLocked;
            widget.setTooltip(
                    NoccClientInit.serverLocked ? Tooltip.of(Text.translatable("nocc.options.locked.tooltip")) : null);
        }
    }
}
