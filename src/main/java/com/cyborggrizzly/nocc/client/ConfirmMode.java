package com.cyborggrizzly.nocc.client;

public enum ConfirmMode {
    VANILLA,            // do nothing
    OFF,                // never confirm (except signature-required)
    DANGEROUS_ONLY,     // confirm only if command matches confirmPatterns
    SAFE_LIST_ONLY      // confirm unless command matches bypassPatterns
}
