package com.cyborggrizzly.nocc.client;

import com.cyborggrizzly.nocc.Nocc;
import com.cyborggrizzly.nocc.net.NoccRulesPayload;

/** Client-side state and the hooks the loader entrypoints call into. */
public class NoccClientInit {
    public static volatile boolean serverLocked = false;
    public static volatile boolean fromServer = false;

    public static void init() {
        NoccConfig.get();
        Runtime.getRuntime().addShutdownHook(new Thread(NoccConfig::save));
    }

    /** Call on the client thread when the server's rules arrive. */
    public static void onRulesPayload(NoccRulesPayload payload) {
        Nocc.LOGGER.info("Received rules payload from server");
        if (!payload.present()) {
            Nocc.LOGGER.info("No rules present in payload, reverting to local config");
            serverLocked = false;
            fromServer = false;
            NoccConfig.get(true);
            return;
        }

        Nocc.LOGGER.info("Applying server rules");
        var cfg = NoccConfig.get();
        cfg.mode = ConfirmMode.valueOf(payload.mode().toUpperCase());
        if (payload.confirm().isPresent())
            cfg.confirmPatterns = payload.confirm().get();
        if (payload.bypass().isPresent())
            cfg.bypassPatterns = payload.bypass().get();
        cfg.compile();
        serverLocked = payload.locked();
        fromServer = true;
    }

    /** Call on the client thread when leaving a server: drop server rules, restore local config. */
    public static void onDisconnect() {
        fromServer = false;
        serverLocked = false;
        NoccConfig.get(true);
    }
}
