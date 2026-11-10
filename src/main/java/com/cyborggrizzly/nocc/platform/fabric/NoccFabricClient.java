package com.cyborggrizzly.nocc.platform.fabric;

//? if fabric {
import com.cyborggrizzly.nocc.client.NoccClientInit;
import com.cyborggrizzly.nocc.net.NoccRulesPayload;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class NoccFabricClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    NoccClientInit.init();

    ClientPlayNetworking.registerGlobalReceiver(NoccRulesPayload.ID,
        (payload, context) -> NoccClientInit.onRulesPayload(payload));

    ClientPlayConnectionEvents.DISCONNECT
        .register((handler, client) -> client.execute(NoccClientInit::onDisconnect));
  }
}
//?}
