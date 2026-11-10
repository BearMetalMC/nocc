package com.cyborggrizzly.nocc;

import com.cyborggrizzly.nocc.net.Sync;
import com.cyborggrizzly.nocc.server.NoccServerState;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/** Server-side hooks the loader entrypoints call into. */
public final class NoccCommon {

  /** Send the current rules to a player who just joined. */
  public static void onPlayerJoin(ServerPlayer player) {
    Sync.sendTo(player, NoccServerState.rules());
  }

  /** Send the current rules to everyone, e.g. after a data pack reload. */
  public static void syncAll(MinecraftServer server) {
    var rules = NoccServerState.rules();
    for (ServerPlayer player : server.getPlayerList().getPlayers()) {
      Sync.sendTo(player, rules);
    }
  }

  private NoccCommon() {
  }
}
