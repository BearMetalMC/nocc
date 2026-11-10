package com.cyborggrizzly.nocc.platform.neoforge;

//? if neoforge {
/*import com.cyborggrizzly.nocc.Nocc;
import com.cyborggrizzly.nocc.NoccCommon;
import com.cyborggrizzly.nocc.net.NoccRulesPayload;
import com.cyborggrizzly.nocc.net.Sync;
import com.cyborggrizzly.nocc.server.NoccRulesReloader;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(Nocc.MOD_ID)
public class NoccNeoForge {

  public NoccNeoForge(IEventBus modBus) {
    Sync.setSender(new Sync.PlatformSender() {
      @Override
      public boolean canSend(ServerPlayer player, CustomPacketPayload.Type<?> type) {
        return player.connection.hasChannel(type);
      }

      @Override
      public void send(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
      }
    });

    // optional(): neither side is required to have the mod installed
    modBus.addListener(RegisterPayloadHandlersEvent.class, event -> event.registrar("1").optional()
        .playToClient(NoccRulesPayload.ID, NoccRulesPayload.CODEC));

    NeoForge.EVENT_BUS.addListener(AddServerReloadListenersEvent.class,
        event -> event.addListener(NoccRulesReloader.RELOAD_ID, new NoccRulesReloader()));

    // Fires for the joining player on login, and for everyone after a data pack reload
    NeoForge.EVENT_BUS.addListener(OnDatapackSyncEvent.class,
        event -> event.getRelevantPlayers().forEach(NoccCommon::onPlayerJoin));
  }
}
*///?}
