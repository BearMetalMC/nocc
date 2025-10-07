package com.cyborggrizzly.nocc.client;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NoccConfig {
    public ConfirmMode mode = ConfirmMode.OFF;
    public List<String> confirmPatterns = List.of(
            "^/(op|deop|ban|kick|pardon|whitelist)\\b",
            "^/(stop|reload)\\b",
            "^/(data|nbt|setblock|fill|clone|summon|give)\\b",
            "^/tp\\b.*@a\\b" // examples — tune to taste
    );
    public List<String> bypassPatterns = List.of(
            "^/(spawn|home|warp|msg|r|tpa|tpaccept)\\b");

    private static final Path PATH = Path.of("config", "no-command-confirm.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static NoccConfig INSTANCE;

    public static NoccConfig get() {
        if (INSTANCE == null)
            INSTANCE = load();
        return INSTANCE;
    }

    public static NoccConfig get(boolean refresh) {
        if (refresh)
            INSTANCE = load();
        return INSTANCE;
    }

    public static void save() {
        if (NoccClientInit.fromServer)
            return;
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer w = Files.newBufferedWriter(PATH)) {
                GSON.toJson(get(), w);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static NoccConfig load() {
        try {
            if (Files.exists(PATH))
                try (Reader r = Files.newBufferedReader(PATH)) {
                    NoccConfig c = GSON.fromJson(r, NoccConfig.class);
                    if (c != null)
                        return c;
                }
        } catch (IOException ignored) {
        }
        return new NoccConfig();
    }

    public void cycleMode() {
        switch (mode) {
            case ConfirmMode.OFF:
                mode = ConfirmMode.DANGEROUS_ONLY;
                break;
            case ConfirmMode.DANGEROUS_ONLY:
                mode = ConfirmMode.SAFE_LIST_ONLY;
                break;
            case ConfirmMode.SAFE_LIST_ONLY:
                mode = ConfirmMode.VANILLA;
                break;
            case ConfirmMode.VANILLA:
                mode = ConfirmMode.OFF;
                break;
        }
    }

    public transient List<java.util.regex.Pattern> confirm = new ArrayList<>();
    public transient List<java.util.regex.Pattern> bypass = new ArrayList<>();

    public void compile() {
        confirm = confirmPatterns.stream().map(Pattern::compile).toList();
        bypass = bypassPatterns.stream().map(Pattern::compile).toList();
    }

    public boolean matchesAny(String s, List<Pattern> ps) {
        for (var p : ps)
            if (p.matcher(s).find())
                return true;
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static SimpleOption<ConfirmMode> confirmModeOption() {
        var cfg = NoccConfig.get();

        Codec codec = NoccClientInit.serverLocked ? Codec.EMPTY.codec()
                : Codec.STRING.xmap(
                        s -> ConfirmMode.valueOf(s.toUpperCase(java.util.Locale.ROOT)),
                        m -> m.name().toLowerCase(java.util.Locale.ROOT));

        return new SimpleOption<ConfirmMode>(
                NoccClientInit.serverLocked ? "nocc.options.confirm_mode.locked" : "nocc.options.confirm_mode",
                t -> Tooltip.of(Text.translatable("nocc.options.confirm_mode.tooltip").append(" - ")
                        .append(Text.translatable("nocc.options.confirm_mode." + t.name().toLowerCase() + ".tooltip"))),
                (opt, value) -> Text.translatable("nocc.options.confirm_mode." + value.name().toLowerCase()),
                new SimpleOption.PotentialValuesBasedCallbacks<>(List.of(ConfirmMode.values()), codec),
                cfg.mode,
                newValue -> {
                    if (NoccClientInit.serverLocked)
                        return;
                    cfg.mode = newValue;
                    NoccConfig.save();
                    cfg.compile();
                });
    }
}
