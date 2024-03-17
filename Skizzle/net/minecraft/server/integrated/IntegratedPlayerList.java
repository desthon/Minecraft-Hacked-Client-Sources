/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.ServerConfigurationManager;

public class IntegratedPlayerList
extends ServerConfigurationManager {
    private NBTTagCompound hostPlayerData;
    private static final String __OBFID = "CL_00001128";

    public IntegratedPlayerList(IntegratedServer p_i1314_1_) {
        super(p_i1314_1_);
        this.setViewDistance(10);
    }

    @Override
    protected void writePlayerData(EntityPlayerMP playerIn) {
        if (playerIn.getName().equals(this.func_180603_b().getServerOwner())) {
            this.hostPlayerData = new NBTTagCompound();
            playerIn.writeToNBT(this.hostPlayerData);
        }
        super.writePlayerData(playerIn);
    }

    @Override
    public String allowUserToConnect(SocketAddress address, GameProfile profile) {
        return profile.getName().equalsIgnoreCase(this.func_180603_b().getServerOwner()) && this.getPlayerByUsername(profile.getName()) != null ? "That name is already taken." : super.allowUserToConnect(address, profile);
    }

    public IntegratedServer func_180603_b() {
        return (IntegratedServer)super.getServerInstance();
    }

    @Override
    public NBTTagCompound getHostPlayerData() {
        return this.hostPlayerData;
    }

    @Override
    public MinecraftServer getServerInstance() {
        return this.func_180603_b();
    }
}

