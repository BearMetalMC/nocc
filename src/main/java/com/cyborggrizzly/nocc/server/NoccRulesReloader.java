package com.cyborggrizzly.nocc.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.cyborggrizzly.nocc.net.ServerRules;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("deprecation")
public final class NoccRulesReloader implements SimpleSynchronousResourceReloadListener {
    private static final Gson GSON = new Gson();
    public static final Identifier RELOAD_ID = Identifier.of("nocc", "server_rules");

    @Override
    public Identifier getFabricId() {
        return RELOAD_ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        // Look for a single canonical file in any namespace:
        // data/<ns>/nocc_config/settings.json
        ServerRules chosen = null;
        int chosenPriority = -1;

        for (String ns : manager.getAllNamespaces()) {
            Identifier id = Identifier.of(ns, "nocc_config/settings.json");
            try {
                // Gather the resource stack so higher-priority packs override lower ones.
                // (getAllResources is preserved across many MCs)
                List<Resource> stack = manager.getAllResources(id);
                for (int i = 0; i < stack.size(); i++) {
                    Resource res = stack.get(i);
                    var jo = GSON.fromJson(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8),
                            JsonObject.class);
                    ServerRules r = jsonToRules(jo);
                    // highest i == top priority (vanilla->topmost pack). pick the last.
                    if (i >= chosenPriority) {
                        chosenPriority = i;
                        chosen = r;
                    }
                }
            } catch (Exception ignored) {
                // no file in this namespace, or API differences; continue
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