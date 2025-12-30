package com.cyborggrizzly.nocc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyborggrizzly.nocc.net.NoccRulesPayload;
import com.cyborggrizzly.nocc.net.Sync;
import com.cyborggrizzly.nocc.server.NoccRulesReloader;
import com.cyborggrizzly.nocc.server.NoccServerState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;

public class Nocc implements ModInitializer {
    public static final String MOD_ID = "NoCC";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(NoccRulesReloader.RELOAD_ID,
                new NoccRulesReloader());

        PayloadTypeRegistry.playS2C().register(NoccRulesPayload.ID,
                NoccRulesPayload.CODEC);

        // send rules to players on join
        ServerPlayConnectionEvents.JOIN
                .register((handler, sender, server) -> Sync.sendTo(handler.player,
                        NoccServerState.rules()));

        // after reload, broadcast
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager,
                success) -> {
            var rules = NoccServerState.rules();
            for (ServerPlayer player : PlayerLookup.all(server)) {
                Sync.sendTo(player, rules);
            }
        });

    }
}
