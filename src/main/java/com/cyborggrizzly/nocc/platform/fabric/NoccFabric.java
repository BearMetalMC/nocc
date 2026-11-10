package com.cyborggrizzly.nocc.platform.fabric;

//? if fabric {
import com.cyborggrizzly.nocc.NoccCommon;
import com.cyborggrizzly.nocc.net.NoccRulesPayload;
import com.cyborggrizzly.nocc.net.Sync;
import com.cyborggrizzly.nocc.server.NoccRulesReloader;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;

public class NoccFabric implements ModInitializer {

  @Override
  public void onInitialize() {
    Sync.setSender(new Sync.PlatformSender() {
      @Override
      public boolean canSend(ServerPlayer player, CustomPacketPayload.Type<?> type) {
        return ServerPlayNetworking.canSend(player, type);
      }

      @Override
      public void send(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
      }
    });

    ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(NoccRulesReloader.RELOAD_ID,
        new NoccRulesReloader());

    PayloadTypeRegistry.clientboundPlay().register(NoccRulesPayload.ID, NoccRulesPayload.CODEC);

    ServerPlayConnectionEvents.JOIN
        .register((handler, sender, server) -> NoccCommon.onPlayerJoin(handler.player));

    ServerLifecycleEvents.END_DATA_PACK_RELOAD
        .register((server, resourceManager, success) -> NoccCommon.syncAll(server));
  }
}
//?}
