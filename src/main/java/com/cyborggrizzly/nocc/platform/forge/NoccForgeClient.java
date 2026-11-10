package com.cyborggrizzly.nocc.platform.forge;

//? if forge {
/*import com.cyborggrizzly.nocc.client.NoccClientInit;
import com.cyborggrizzly.nocc.net.NoccRulesPayload;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;

/^* Client-only half of the Forge entrypoint. Only touched when running on a client. ^/
public final class NoccForgeClient {

  static void init() {
    NoccClientInit.init();

    ClientPlayerNetworkEvent.LoggingOut.BUS.addListener(event -> NoccClientInit.onDisconnect());
  }

  static void onRules(NoccRulesPayload payload) {
    NoccClientInit.onRulesPayload(payload);
  }

  private NoccForgeClient() {
  }
}
*///?}
