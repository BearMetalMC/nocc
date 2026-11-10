package com.cyborggrizzly.nocc.platform.neoforge;

//? if neoforge {
/*import com.cyborggrizzly.nocc.Nocc;
import com.cyborggrizzly.nocc.client.NoccClientInit;
import com.cyborggrizzly.nocc.net.NoccRulesPayload;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Nocc.MOD_ID, dist = Dist.CLIENT)
public class NoccNeoForgeClient {

  public NoccNeoForgeClient(IEventBus modBus) {
    NoccClientInit.init();

    modBus.addListener(RegisterClientPayloadHandlersEvent.class, event -> event.register(NoccRulesPayload.ID,
        (payload, context) -> NoccClientInit.onRulesPayload(payload)));

    NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingOut.class,
        event -> NoccClientInit.onDisconnect());
  }
}
*///?}
