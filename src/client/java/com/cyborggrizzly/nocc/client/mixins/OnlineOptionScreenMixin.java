package com.cyborggrizzly.nocc.client.mixins;

import com.cyborggrizzly.nocc.client.ConfirmMode;
import com.cyborggrizzly.nocc.client.NoccConfig;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.OnlineOptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(OnlineOptionsScreen.class)
public abstract class OnlineOptionScreenMixin extends GameOptionsScreen {
    private OnlineOptionScreenMixin() {super(null,null,null);}

    @Inject(method = "init", at = @At("TAIL"))
    private void nocc$addToggle(CallbackInfo ci) {
        OptionListWidget body = ((GameOptionsScreenAccessor)this).nocc$getBody();
        if (body == null) return;

        var button = NoccConfig.confirmModeOption();
        body.addSingleOptionEntry(button);
    }
}
