package com.cyborggrizzly.nocc.platform.forge;

//? if forge {
/*import com.cyborggrizzly.nocc.Nocc;
import com.cyborggrizzly.nocc.NoccCommon;
import com.cyborggrizzly.nocc.net.NoccRulesPayload;
import com.cyborggrizzly.nocc.net.Sync;
import com.cyborggrizzly.nocc.server.NoccRulesReloader;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;

@Mod(Nocc.MOD_ID)
public class NoccForge {

  // optional(): neither side is required to have the mod installed
  private static final Channel<CustomPacketPayload> CHANNEL = ChannelBuilder.named(Nocc.MOD_ID + ":main")
      .networkProtocolVersion(1)
      .optional()
      .payloadChannel()
      .play()
      .clientbound()
      // Only ever invoked on a client, so the client class is never loaded on a dedicated server
      .addMain(NoccRulesPayload.ID, NoccRulesPayload.CODEC, (payload, context) -> NoccForgeClient.onRules(payload))
      .build();

  public NoccForge(FMLJavaModLoadingContext context) {
    Sync.setSender(new Sync.PlatformSender() {
      @Override
      public boolean canSend(ServerPlayer player, CustomPacketPayload.Type<?> type) {
        return CHANNEL.isRemotePresent(player.connection.getConnection());
      }

      @Override
      public void send(ServerPlayer player, CustomPacketPayload payload) {
        CHANNEL.send(payload, PacketDistributor.PLAYER.with(player));
      }
    });

    AddReloadListenerEvent.BUS.addListener(event -> event.addListener(new NoccRulesReloader()));

    // Fires for the joining player on login, and for everyone after a data pack reload
    OnDatapackSyncEvent.BUS.addListener(event -> event.getPlayers().forEach(NoccCommon::onPlayerJoin));

    if (FMLEnvironment.dist == Dist.CLIENT)
      NoccForgeClient.init();
  }
}
*///?}
