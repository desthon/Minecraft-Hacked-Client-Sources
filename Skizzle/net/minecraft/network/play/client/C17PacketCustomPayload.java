/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C17PacketCustomPayload
implements Packet {
    private String channel;
    private PacketBuffer data;
    private static final String __OBFID = "CL_00001356";

    public C17PacketCustomPayload() {
    }

    public C17PacketCustomPayload(String p_i45945_1_, PacketBuffer p_i45945_2_) {
        this.channel = p_i45945_1_;
        this.data = p_i45945_2_;
        if (p_i45945_2_.writerIndex() > 32767) {
            throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
        }
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.channel = data.readStringFromBuffer(20);
        int var2 = data.readableBytes();
        if (var2 < 0 || var2 > 32767) {
            throw new IOException("Payload may not be larger than 32767 bytes");
        }
        this.data = new PacketBuffer(data.readBytes(var2));
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeString(this.channel);
        data.writeBytes(this.data);
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.processVanilla250Packet(this);
    }

    public String getChannelName() {
        return this.channel;
    }

    public PacketBuffer getBufferData() {
        return this.data;
    }

    @Override
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}

