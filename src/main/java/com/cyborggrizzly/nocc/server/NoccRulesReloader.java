package com.cyborggrizzly.nocc.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.cyborggrizzly.nocc.Nocc;
import com.cyborggrizzly.nocc.net.ServerRules;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("deprecation")
public final class NoccRulesReloader implements SimpleSynchronousResourceReloadListener {
    private static final Gson GSON = new Gson();
    public static final Identifier RELOAD_ID = Identifier.fromNamespaceAndPath(Nocc.MOD_ID.toLowerCase(),
            "server_rules");

    @Override
    public Identifier getFabricId() {
        return RELOAD_ID;
    }

    public void onResourceManagerReload(ResourceManager manager) {
        Nocc.LOGGER.info("Reloading server rules from data packs");
        ServerRules chosen = null;

        var namespaces = manager.getNamespaces();

        for (String ns : namespaces) {
            if (ns == null || ns.isEmpty())
                continue;
            Identifier id = Identifier.fromNamespaceAndPath(ns, "nocc_config/settings.json");
            try {
                Optional<Resource> res = manager.getResource(id);
                if (res.isEmpty())
                    continue;
                InputStreamReader reader = new InputStreamReader(res.get().open(), StandardCharsets.UTF_8);

                ServerRules r = jsonToRules(GSON.fromJson(reader, JsonObject.class));

                chosen = r;

                Nocc.LOGGER.info("Loaded server rules from namespace: " + ns);
                if (chosen != null)
                    Nocc.LOGGER.info(chosen.toString());
            } catch (Exception ignored) {
                Nocc.LOGGER.warn("No server rules file found in namespace: " + ns);
                Nocc.LOGGER.error(ignored.getMessage());
            }
        }

        if (chosen == null)
            chosen = ServerRules.DEFAULT;
        NoccServerState.apply(chosen);
    }

    private static ServerRules jsonToRules(JsonObject jo) {
        String mode = optString(jo, "mode", "VANILLA").toUpperCase(Locale.ROOT);
        boolean locked = optBool(jo, "locked", false);
        List<String> confirm = jo.has("confirm_patterns") ? optStrList(jo, "confirm_patterns") : null;
        List<String> bypass = jo.has("bypass_patterns") ? optStrList(jo, "bypass_patterns") : null;
        return new ServerRules(mode, locked, Optional.ofNullable(confirm), Optional.ofNullable(bypass), true);
    }

    private static String optString(JsonObject jo, String k, String def) {
        return jo.has(k) ? jo.get(k).getAsString() : def;
    }

    private static boolean optBool(JsonObject jo, String k, boolean def) {
        return jo.has(k) ? jo.get(k).getAsBoolean() : def;
    }

    private static List<String> optStrList(JsonObject jo, String k) {
        if (!jo.has(k) || !jo.get(k).isJsonArray())
            return List.of();
        var arr = jo.getAsJsonArray(k);
        var out = new ArrayList<String>(arr.size());
        arr.forEach(e -> out.add(e.getAsString()));
        return out;
    }
}