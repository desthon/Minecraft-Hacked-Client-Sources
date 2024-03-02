/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C09PacketHeldItemChange
implements Packet {
    private int slotId;
    private static final String __OBFID = "CL_00001368";

    public C09PacketHeldItemChange() {
    }

    public C09PacketHeldItemChange(int p_i45262_1_) {
        this.slotId = p_i45262_1_;
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.slotId = data.readShort();
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeShort(this.slotId);
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.processHeldItemChange(this);
    }

    public int getSlotId() {
        return this.slotId;
    }

    @Override
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}

