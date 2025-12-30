package com.cyborggrizzly.nocc.client;

import com.cyborggrizzly.nocc.Nocc;
import com.cyborggrizzly.nocc.net.NoccRulesPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class NoccClientInit {
    public static volatile boolean serverLocked = false;
    public static volatile boolean fromServer = false;

    public static void init() {

        ClientPlayNetworking.registerGlobalReceiver(NoccRulesPayload.ID, (payload, context) -> {
            Nocc.LOGGER.info("Received rules payload from server");
            if (!payload.present()) {
                serverLocked = false;
                fromServer = false;
                NoccConfig.get(true);
                return;
            }

            var cfg = NoccConfig.get();
            cfg.mode = ConfirmMode.valueOf(payload.mode().toUpperCase());
            if (payload.confirm().isPresent())
                cfg.confirmPatterns = payload.confirm().get();
            if (payload.bypass().isPresent())
                cfg.bypassPatterns = payload.bypass().get();
            cfg.compile();
            serverLocked = payload.locked();
            fromServer = true;

            // Minecraft mc = Minecraft.getInstance();
            // mc.execute(() -> {
            // if (mc.screen instanceof OnlineOptionsScreen) {
            // Nocc.LOGGER.info("Re-initializing current screen due to rule change");
            // mc.screen.init(mc.getWindow().getWidth(), mc.getWindow().getHeight());
            // }
            // });
        });
    }
}