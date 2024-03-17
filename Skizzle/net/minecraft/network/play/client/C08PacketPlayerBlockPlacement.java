/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;

public class C08PacketPlayerBlockPlacement
implements Packet {
    private static final BlockPos field_179726_a = new BlockPos(-1, -1, -1);
    private BlockPos field_179725_b;
    private int placedBlockDirection;
    private ItemStack stack;
    private float facingX;
    private float facingY;
    private float facingZ;
    private static final String __OBFID = "CL_00001371";

    public C08PacketPlayerBlockPlacement() {
    }

    public C08PacketPlayerBlockPlacement(ItemStack p_i45930_1_) {
        this(field_179726_a, 255, p_i45930_1_, 0.0f, 0.0f, 0.0f);
    }

    public C08PacketPlayerBlockPlacement(BlockPos p_i45931_1_, int p_i45931_2_, ItemStack p_i45931_3_, float p_i45931_4_, float p_i45931_5_, float p_i45931_6_) {
        this.field_179725_b = p_i45931_1_;
        this.placedBlockDirection = p_i45931_2_;
        this.stack = p_i45931_3_ != null ? p_i45931_3_.copy() : null;
        this.facingX = p_i45931_4_;
        this.facingY = p_i45931_5_;
        this.facingZ = p_i45931_6_;
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179725_b = data.readBlockPos();
        this.placedBlockDirection = data.readUnsignedByte();
        this.stack = data.readItemStackFromBuffer();
        this.facingX = (float)data.readUnsignedByte() / 16.0f;
        this.facingY = (float)data.readUnsignedByte() / 16.0f;
        this.facingZ = (float)data.readUnsignedByte() / 16.0f;
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeBlockPos(this.field_179725_b);
        data.writeByte(this.placedBlockDirection);
        data.writeItemStackToBuffer(this.stack);
        data.writeByte((int)(this.facingX * 16.0f));
        data.writeByte((int)(this.facingY * 16.0f));
        data.writeByte((int)(this.facingZ * 16.0f));
    }

    public void func_180769_a(INetHandlerPlayServer p_180769_1_) {
        p_180769_1_.processPlayerBlockPlacement(this);
    }

    public BlockPos func_179724_a() {
        return this.field_179725_b;
    }

    public int getPlacedBlockDirection() {
        return this.placedBlockDirection;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public float getPlacedBlockOffsetX() {
        return this.facingX;
    }

    public float getPlacedBlockOffsetY() {
        return this.facingY;
    }

    public float getPlacedBlockOffsetZ() {
        return this.facingZ;
    }

    @Override
    public void processPacket(INetHandler handler) {
        this.func_180769_a((INetHandlerPlayServer)handler);
    }
}

