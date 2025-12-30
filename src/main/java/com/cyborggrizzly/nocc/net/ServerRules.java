package com.cyborggrizzly.nocc.net;

import java.util.List;
import java.util.Optional;

public record ServerRules(
        String mode,
        boolean locked,
        Optional<List<String>> confirm,
        Optional<List<String>> bypass,
        boolean present) {
    public static final ServerRules DEFAULT = new ServerRules("VANILLA", false, Optional.empty(), Optional.empty(),
            false);
}
