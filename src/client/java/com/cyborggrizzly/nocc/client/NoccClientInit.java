package com.cyborggrizzly.nocc.client;

import com.cyborggrizzly.nocc.net.NoccRulesPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.OnlineOptionsScreen;

public class NoccClientInit {
    public static volatile boolean serverLocked = false;
    public static volatile boolean fromServer = false;

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(NoccRulesPayload.ID, (payload, context) -> {
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

            var mc = MinecraftClient.getInstance();
            if (mc.currentScreen instanceof OnlineOptionsScreen oos) {
                oos.init(mc, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
            }
        });
    }
}