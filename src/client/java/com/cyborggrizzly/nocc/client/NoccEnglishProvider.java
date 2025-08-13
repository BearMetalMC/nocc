package com.cyborggrizzly.nocc.client;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class NoccEnglishProvider extends FabricLanguageProvider {
    protected NoccEnglishProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("nocc.options.confirm_mode", "Command Confirmations");
        translationBuilder.add("nocc.options.confirm_mode.tooltip", "Determine when the \"Confirm Command Execution\" permission pop up occurs");
        translationBuilder.add("nocc.options.confirm_mode.vanilla", "Vanilla");
        translationBuilder.add("nocc.options.confirm_mode.off", "Disabled (be cautious, here be dragons)");
        translationBuilder.add("nocc.options.confirm_mode.dangerous_only", "Only for potentially dangerous commands");
        translationBuilder.add("nocc.options.confirm_mode.safe_list_only", "Disabled for non-approved items");
    }
}
