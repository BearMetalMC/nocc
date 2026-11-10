package com.cyborggrizzly.nocc.net;

import com.cyborggrizzly.nocc.Nocc;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public final class Sync {

    /** The loader's way of getting a payload to a player. Installed by the loader entrypoint. */
    public interface PlatformSender {
        /** Whether the player's client has declared it understands this payload. */
        boolean canSend(ServerPlayer player, CustomPacketPayload.Type<?> type);

        void send(ServerPlayer player, CustomPacketPayload payload);
    }

    private static volatile PlatformSender sender;

    public static void setSender(PlatformSender platformSender) {
        sender = platformSender;
    }

    public static void sendTo(ServerPlayer player, ServerRules rules) {
        PlatformSender s = sender;
        if (s == null)
            throw new IllegalStateException("NoCC platform sender was not installed");
        // Clients without the mod never registered the channel; leave them alone.
        if (!s.canSend(player, NoccRulesPayload.ID))
            return;

        Nocc.LOGGER.info("Sending server rules");
        s.send(player, new NoccRulesPayload(rules.mode(), rules.locked(),
                rules.confirm(), rules.bypass(), rules.present()));
    }

    private Sync() {
    }
}
