package com.cyborggrizzly.nocc.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class NoccClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NoccConfig.get();
        Runtime.getRuntime().addShutdownHook(new Thread(NoccConfig::save));
        NoccClientInit.init();

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            client.execute(() -> {
                NoccClientInit.fromServer = false;
                NoccClientInit.serverLocked = false;
                NoccConfig.get(true);

                // if (client.screen instanceof OnlineOptionsScreen oos) {
                // oos.init(client.getWindow().getWidth(), client.getWindow().getHeight());
                // }
            });
        });
    }
}
