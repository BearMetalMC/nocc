package com.cyborggrizzly.nocc.client.mixins;

import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.jetbrains.annotations.Nullable;

@Mixin(GameOptionsScreen.class)
public interface GameOptionsScreenAccessor {
    @Accessor("body") @Nullable OptionListWidget nocc$getBody();
}
