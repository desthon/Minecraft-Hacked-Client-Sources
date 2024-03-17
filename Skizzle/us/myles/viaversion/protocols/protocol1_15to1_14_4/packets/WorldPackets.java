/*
 * Decompiled with CFR 0.150.
 */
package us.myles.ViaVersion.protocols.protocol1_15to1_14_4.packets;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.packets.InventoryPackets;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;

public class WorldPackets {
    public static void register(final Protocol1_15To1_14_4 protocol) {
        BlockRewriter blockRewriter = new BlockRewriter(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_14.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_14.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_14.MULTI_BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_14.ACKNOWLEDGE_PLAYER_DIGGING);
        protocol.registerOutgoing(ClientboundPackets1_14.CHUNK_DATA, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int i;
                        Chunk chunk = wrapper.read(new Chunk1_14Type());
                        wrapper.write(new Chunk1_15Type(), chunk);
                        if (chunk.isFullChunk()) {
                            int[] biomeData = chunk.getBiomeData();
                            int[] newBiomeData = new int[1024];
                            if (biomeData != null) {
                                for (i = 0; i < 4; ++i) {
                                    for (int j = 0; j < 4; ++j) {
                                        int x = (j << 2) + 2;
                                        int z = (i << 2) + 2;
                                        int oldIndex = z << 4 | x;
                                        newBiomeData[i << 2 | j] = biomeData[oldIndex];
                                    }
                                }
                                for (i = 1; i < 64; ++i) {
                                    System.arraycopy(newBiomeData, 0, newBiomeData, i * 16, 16);
                                }
                            }
                            chunk.setBiomeData(newBiomeData);
                        }
                        for (int s = 0; s < 16; ++s) {
                            ChunkSection section = chunk.getSections()[s];
                            if (section == null) continue;
                            for (i = 0; i < section.getPaletteSize(); ++i) {
                                int old = section.getPaletteEntry(i);
                                int newId = protocol.getMappingData().getNewBlockStateId(old);
                                section.setPaletteEntry(i, newId);
                            }
                        }
                    }
                });
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_14.EFFECT, 1010, 2001);
        protocol.registerOutgoing(ClientboundPackets1_14.SPAWN_PARTICLE, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map((Type)Type.FLOAT, Type.DOUBLE);
                this.map((Type)Type.FLOAT, Type.DOUBLE);
                this.map((Type)Type.FLOAT, Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(new PacketHandler(){

                    @Override
                    public void handle(PacketWrapper wrapper) throws Exception {
                        int id = wrapper.get(Type.INT, 0);
                        if (id == 3 || id == 23) {
                            int data = wrapper.passthrough(Type.VAR_INT);
                            wrapper.set(Type.VAR_INT, 0, protocol.getMappingData().getNewBlockStateId(data));
                        } else if (id == 32) {
                            InventoryPackets.toClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        }
                    }
                });
            }
        });
    }
}

