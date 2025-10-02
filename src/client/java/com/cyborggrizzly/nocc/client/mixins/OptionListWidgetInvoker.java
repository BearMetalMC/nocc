// src/main/java/com/cyborggrizzly/nocc/mixin/access/OptionListWidgetInvoker.java
package com.cyborggrizzly.nocc.client.mixins;

import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(OptionListWidget.class)
public interface OptionListWidgetInvoker {
    @Invoker("addSingleOptionEntry")
    void nocc$addSingle(SimpleOption<?> option);
}
