package com.cyborggrizzly.nocc.client;

import net.fabricmc.api.ClientModInitializer;

public class NoccClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NoccConfig.get();
        Runtime.getRuntime().addShutdownHook(new Thread(NoccConfig::save));
    }
}
