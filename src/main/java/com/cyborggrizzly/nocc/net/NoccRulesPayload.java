package com.cyborggrizzly.nocc.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cyborggrizzly.nocc.Nocc;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record NoccRulesPayload(
                String mode,
                boolean locked,
                Optional<List<String>> confirm,
                Optional<List<String>> bypass,
                boolean present) implements CustomPacketPayload {

        public static final Identifier PACKET_ID = Identifier.fromNamespaceAndPath(Nocc.MOD_ID.toLowerCase(), "rules");

        public static final CustomPacketPayload.Type<NoccRulesPayload> ID = new CustomPacketPayload.Type<>(PACKET_ID);

        public static final StreamCodec<RegistryFriendlyByteBuf, NoccRulesPayload> CODEC = StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8, NoccRulesPayload::mode,
                        ByteBufCodecs.BOOL, NoccRulesPayload::locked,
                        ByteBufCodecs.optional(ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8)),
                        NoccRulesPayload::confirm,
                        ByteBufCodecs.optional(ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8)),
                        NoccRulesPayload::bypass,
                        ByteBufCodecs.BOOL, NoccRulesPayload::present,
                        NoccRulesPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
                return ID;
        }
}
