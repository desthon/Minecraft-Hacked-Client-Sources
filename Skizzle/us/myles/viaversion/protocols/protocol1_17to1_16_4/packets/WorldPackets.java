/*
 * Decompiled with CFR 0.150.
 */
package us.myles.ViaVersion.protocols.protocol1_17to1_16_4.packets;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.types.Chunk1_16_2Type;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.storage.BiomeStorage;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.types.Chunk1_17Type;

public class WorldPackets {
    public static void register(final Protocol1_17To1_16_4 protocol) {
        BlockRewriter blockRewriter = new BlockRewriter(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_16_2.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_16_2.BLOCK_CHANGE);
        blockRewriter.registerVarLongMultiBlockChange(ClientboundPackets1_16_2.MULTI_BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_16_2.ACKNOWLEDGE_PLAYER_DIGGING);
        protocol.registerOutgoing(ClientboundPackets1_16_2.UPDATE_LIGHT, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough(Type.VAR_INT);
                    wrapper.passthrough(Type.VAR_INT);
                    wrapper.passthrough(Type.BOOLEAN);
                    wrapper.write(Type.VAR_LONG, wrapper.read(Type.VAR_INT).longValue());
                    wrapper.write(Type.VAR_LONG, wrapper.read(Type.VAR_INT).longValue());
                    wrapper.write(Type.VAR_LONG, wrapper.read(Type.VAR_INT).longValue());
                    wrapper.write(Type.VAR_LONG, wrapper.read(Type.VAR_INT).longValue());
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_16_2.CHUNK_DATA, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    Chunk chunk = wrapper.read(new Chunk1_16_2Type());
                    wrapper.write(new Chunk1_17Type(), chunk);
                    BiomeStorage biomeStorage = wrapper.user().get(BiomeStorage.class);
                    if (chunk.isFullChunk()) {
                        biomeStorage.setBiomes(chunk.getX(), chunk.getZ(), chunk.getBiomeData());
                    } else {
                        int[] biomes = biomeStorage.getBiomes(chunk.getX(), chunk.getZ());
                        if (biomes != null) {
                            chunk.setBiomeData(biomes);
                        } else {
                            Via.getPlatform().getLogger().warning("Biome data not found for chunk at " + chunk.getX() + ", " + chunk.getZ());
                            chunk.setBiomeData(new int[1024]);
                        }
                    }
                    for (int s = 0; s < 16; ++s) {
                        ChunkSection section = chunk.getSections()[s];
                        if (section == null) continue;
                        for (int i = 0; i < section.getPaletteSize(); ++i) {
                            int old = section.getPaletteEntry(i);
                            section.setPaletteEntry(i, protocol.getMappingData().getNewBlockStateId(old));
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_16_2.JOIN_GAME, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.map(Type.NBT);
                this.handler(wrapper -> {
                    String world = wrapper.passthrough(Type.STRING);
                    wrapper.user().get(BiomeStorage.class).setWorld(world);
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_16_2.RESPAWN, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough(Type.NBT);
                    String world = wrapper.passthrough(Type.STRING);
                    BiomeStorage biomeStorage = wrapper.user().get(BiomeStorage.class);
                    if (!world.equals(biomeStorage.getWorld())) {
                        biomeStorage.clearBiomes();
                    }
                    biomeStorage.setWorld(world);
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_16_2.UNLOAD_CHUNK, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    int x = wrapper.passthrough(Type.INT);
                    int z = wrapper.passthrough(Type.INT);
                    wrapper.user().get(BiomeStorage.class).clearBiomes(x, z);
                });
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_16_2.EFFECT, 1010, 2001);
    }
}

