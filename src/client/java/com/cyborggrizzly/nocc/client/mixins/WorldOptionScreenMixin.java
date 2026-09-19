package com.cyborggrizzly.nocc.client.mixins;

import com.cyborggrizzly.nocc.Nocc;
import com.cyborggrizzly.nocc.client.NoccClientInit;
import com.cyborggrizzly.nocc.client.NoccConfig;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.WorldOptionsScreen;
import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOptionsScreen.class)
public abstract class WorldOptionScreenMixin extends Screen {
  private WorldOptionScreenMixin() {
    super(null);
  }

  @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;addToFooter(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"))
  private void nocc$addToggle(CallbackInfo ci, @Local GridLayout.RowHelper gridHelper) {
    Nocc.LOGGER.info("Creating WorldOptionsScreen widget");

    var opt = NoccConfig.confirmModeOption();
    var widget = opt.createButton(this.minecraft.options, 0, 0, 310);

    gridHelper.addChild(widget, 2);

    widget.active = !NoccClientInit.serverLocked;
    widget.setTooltip(
        NoccClientInit.serverLocked
            ? Tooltip.create(Component.translatable("nocc.options.locked.tooltip"))
            : null);
  }
}
