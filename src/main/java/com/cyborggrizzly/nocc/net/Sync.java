package com.cyborggrizzly.nocc.net;

import com.cyborggrizzly.nocc.Nocc;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class Sync {

    public static void sendTo(ServerPlayer player, ServerRules rules) {
        Nocc.LOGGER.info("Sending server rules");
        ServerPlayNetworking.send(player, new NoccRulesPayload(rules.mode(), rules.locked(),
                rules.confirm(), rules.bypass(), rules.present()));
    }
}