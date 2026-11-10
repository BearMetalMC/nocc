package com.cyborggrizzly.nocc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Loader-neutral constants. Loader entrypoints live in {@code platform.*}. */
public final class Nocc {
  public static final String MOD_ID = "nocc";
  public static final Logger LOGGER = LoggerFactory.getLogger("NoCC");

  private Nocc() {
  }
}
