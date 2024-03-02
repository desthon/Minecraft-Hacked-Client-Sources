/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package us.myles.ViaVersion.sponge.providers;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BulkChunkTranslatorProvider;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.ClientChunks;
import us.myles.ViaVersion.util.ReflectionUtil;

public class SpongeViaBulkChunkTranslator
extends BulkChunkTranslatorProvider {
    private static ReflectionUtil.ClassReflection mapChunkBulkRef;
    private static ReflectionUtil.ClassReflection mapChunkRef;

    @Override
    public List<Object> transformMapChunkBulk(Object packet, ClientChunks clientChunks) {
        ArrayList list = Lists.newArrayList();
        try {
            int[] xcoords = mapChunkBulkRef.getFieldValue("field_149266_a", packet, int[].class);
            int[] zcoords = mapChunkBulkRef.getFieldValue("field_149264_b", packet, int[].class);
            Object[] chunkMaps = mapChunkBulkRef.getFieldValue("field_179755_c", packet, Object[].class);
            for (int i = 0; i < chunkMaps.length; ++i) {
                int x = xcoords[i];
                int z = zcoords[i];
                Object chunkMap = chunkMaps[i];
                Object chunkPacket = mapChunkRef.newInstance();
                mapChunkRef.setFieldValue("field_149284_a", chunkPacket, x);
                mapChunkRef.setFieldValue("field_149282_b", chunkPacket, z);
                mapChunkRef.setFieldValue("field_179758_c", chunkPacket, chunkMap);
                mapChunkRef.setFieldValue("field_149279_g", chunkPacket, true);
                clientChunks.getBulkChunks().add(ClientChunks.toLong(x, z));
                list.add(chunkPacket);
            }
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Failed to transform chunks bulk", e);
        }
        return list;
    }

    @Override
    public boolean isFiltered(Class<?> packetClass) {
        return packetClass.getName().endsWith("S26PacketMapChunkBulk");
    }

    @Override
    public boolean isPacketLevel() {
        return false;
    }

    static {
        try {
            mapChunkBulkRef = new ReflectionUtil.ClassReflection(Class.forName("net.minecraft.network.play.server.S26PacketMapChunkBulk"));
            mapChunkRef = new ReflectionUtil.ClassReflection(Class.forName("net.minecraft.network.play.server.S21PacketChunkData"));
        }
        catch (ClassNotFoundException classNotFoundException) {
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Failed to initialise chunks reflection", e);
        }
    }
}

