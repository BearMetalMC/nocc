// NoccRulesPayload.java
package com.cyborggrizzly.nocc.net;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record NoccRulesPayload(
                String mode,
                boolean locked,
                Optional<List<String>> confirm,
                Optional<List<String>> bypass,
                boolean present) implements CustomPayload {
        public static final CustomPayload.Id<NoccRulesPayload> ID = new CustomPayload.Id<>(
                        Identifier.of("nocc", "rules"));

        public static final PacketCodec<RegistryByteBuf, NoccRulesPayload> CODEC = PacketCodec.tuple(
                        PacketCodecs.STRING, NoccRulesPayload::mode,
                        PacketCodecs.BOOLEAN, NoccRulesPayload::locked,
                        PacketCodecs.optional(PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING)),
                        NoccRulesPayload::confirm,
                        PacketCodecs.optional(PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING)),
                        NoccRulesPayload::bypass,
                        PacketCodecs.BOOLEAN, NoccRulesPayload::present,
                        NoccRulesPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
                return ID;
        }
}
