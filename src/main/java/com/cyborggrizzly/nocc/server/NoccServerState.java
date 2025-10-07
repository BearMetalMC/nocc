package com.cyborggrizzly.nocc.server;

import com.cyborggrizzly.nocc.net.ServerRules;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class NoccServerState {
    private static volatile ServerRules rules = ServerRules.DEFAULT;

    public static volatile List<Pattern> confirm = List.of();
    public static volatile List<Pattern> bypass = List.of();

    public static ServerRules rules() {
        return rules;
    }

    public static void apply(ServerRules r) {
        rules = r;
        if (r.confirm() != null && r.confirm().isPresent())
            confirm = compile(r.confirm().get());
        if (r.bypass() != null && r.bypass().isPresent())
            bypass = compile(r.bypass().get());
    }

    private static List<Pattern> compile(List<String> src) {
        var out = new ArrayList<Pattern>(src.size());
        for (var s : src)
            out.add(Pattern.compile(s));
        return out;
    }
}