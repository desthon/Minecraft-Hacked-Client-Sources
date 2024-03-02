/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C16PacketClientStatus
implements Packet {
    private EnumState status;
    private static final String __OBFID = "CL_00001348";

    public C16PacketClientStatus() {
    }

    public C16PacketClientStatus(EnumState statusIn) {
        this.status = statusIn;
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.status = (EnumState)data.readEnumValue(EnumState.class);
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeEnumValue(this.status);
    }

    public void func_180758_a(INetHandlerPlayServer p_180758_1_) {
        p_180758_1_.processClientStatus(this);
    }

    public EnumState getStatus() {
        return this.status;
    }

    @Override
    public void processPacket(INetHandler handler) {
        this.func_180758_a((INetHandlerPlayServer)handler);
    }

    public static enum EnumState {
        PERFORM_RESPAWN("PERFORM_RESPAWN", 0),
        REQUEST_STATS("REQUEST_STATS", 1),
        OPEN_INVENTORY_ACHIEVEMENT("OPEN_INVENTORY_ACHIEVEMENT", 2);

        private static final EnumState[] $VALUES;
        private static final String __OBFID = "CL_00001349";

        static {
            $VALUES = new EnumState[]{PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY_ACHIEVEMENT};
        }

        private EnumState(String p_i45947_1_, int p_i45947_2_) {
        }
    }
}

