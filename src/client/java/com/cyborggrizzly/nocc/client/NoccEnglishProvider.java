package com.cyborggrizzly.nocc.client;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup.Provider;

import java.util.concurrent.CompletableFuture;

public class NoccEnglishProvider extends FabricLanguageProvider {
    protected NoccEnglishProvider(FabricDataOutput dataOutput, CompletableFuture<Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(Provider wrapperLookup, TranslationBuilder translationBuilder) {

        translationBuilder.add("nocc.options.confirm_mode", "Command Confirmations");
        translationBuilder.add("nocc.options.confirm_mode.dangerous_only", "Dangerous Only");
        translationBuilder.add("nocc.options.confirm_mode.off", "Disabled");
        translationBuilder.add("nocc.options.confirm_mode.safe_list_only", "Non-approved Only");
        translationBuilder.add("nocc.options.confirm_mode.tooltip",
                "Determine when the \"Confirm Command Execution\" permission pop up occurs");
        translationBuilder.add("nocc.options.confirm_mode.vanilla", "Vanilla");
        translationBuilder.add("nocc.options.confirm_mode.locked", "Command Confirmations (Locked by server)");
        translationBuilder.add("nocc.options.locked.tooltip",
                "The ability to change this setting has been locked by the server.");

        translationBuilder.add("nocc.options.confirm_mode.vanilla.tooltip", "The default Minecraft behavior");
        translationBuilder.add("nocc.options.confirm_mode.off.tooltip", "The confirmation pop up is disabled");
        translationBuilder.add("nocc.options.confirm_mode.dangerous_only.tooltip",
                "Only for potentially dangerous commands");
        translationBuilder.add("nocc.options.confirm_mode.safe_list_only.tooltip", "Disabled for non-approved items");

    }

}
