package com.cyborggrizzly.nocc.net;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public final class Sync {

    public static void sendTo(ServerPlayerEntity player, ServerRules rules) {
        ServerPlayNetworking.send(player, new NoccRulesPayload(rules.mode(), rules.locked(),
                rules.confirm(), rules.bypass(), rules.present()));
    }
}