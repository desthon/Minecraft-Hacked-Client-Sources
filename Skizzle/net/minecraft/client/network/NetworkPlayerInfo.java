/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture$Type
 */
package net.minecraft.client.network;

import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;

public class NetworkPlayerInfo {
    public final GameProfile gameProfile;
    public WorldSettings.GameType gameType;
    public int responseTime;
    public boolean playerTexturesLoaded = false;
    public ResourceLocation locationSkin;
    public ResourceLocation locationCape;
    public String skinType;
    public IChatComponent displayName;
    public int field_178873_i = 0;
    public int field_178870_j = 0;
    public long field_178871_k = 0L;
    public long field_178868_l = 0L;
    public long field_178869_m = 0L;
    private static final String __OBFID = "CL_00000888";

    public NetworkPlayerInfo(GameProfile p_i46294_1_) {
        this.gameProfile = p_i46294_1_;
    }

    public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData p_i46295_1_) {
        this.gameProfile = p_i46295_1_.func_179962_a();
        this.gameType = p_i46295_1_.func_179960_c();
        this.responseTime = p_i46295_1_.func_179963_b();
    }

    public GameProfile func_178845_a() {
        return this.gameProfile;
    }

    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }

    public int getResponseTime() {
        return this.responseTime;
    }

    protected void func_178839_a(WorldSettings.GameType p_178839_1_) {
        this.gameType = p_178839_1_;
    }

    protected void func_178838_a(int p_178838_1_) {
        this.responseTime = p_178838_1_;
    }

    public boolean func_178856_e() {
        return this.locationSkin != null;
    }

    public String func_178851_f() {
        return this.skinType == null ? DefaultPlayerSkin.func_177332_b(this.gameProfile.getId()) : this.skinType;
    }

    public ResourceLocation func_178837_g() {
        if (this.locationSkin == null) {
            this.func_178841_j();
        }
        return (ResourceLocation)Objects.firstNonNull((Object)this.locationSkin, (Object)DefaultPlayerSkin.func_177334_a(this.gameProfile.getId()));
    }

    public ResourceLocation func_178861_h() {
        if (this.locationCape == null) {
            this.func_178841_j();
        }
        return this.locationCape;
    }

    public ScorePlayerTeam func_178850_i() {
        Minecraft.getMinecraft();
        return Minecraft.theWorld.getScoreboard().getPlayersTeam(this.func_178845_a().getName());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void func_178841_j() {
        NetworkPlayerInfo networkPlayerInfo = this;
        synchronized (networkPlayerInfo) {
            if (!this.playerTexturesLoaded) {
                this.playerTexturesLoaded = true;
                Minecraft.getMinecraft().getSkinManager().func_152790_a(this.gameProfile, new SkinManager.SkinAvailableCallback(){
                    private static final String __OBFID = "CL_00002619";

                    @Override
                    public void func_180521_a(MinecraftProfileTexture.Type p_180521_1_, ResourceLocation p_180521_2_, MinecraftProfileTexture p_180521_3_) {
                        switch (p_180521_1_) {
                            case SKIN: {
                                NetworkPlayerInfo.this.locationSkin = p_180521_2_;
                                NetworkPlayerInfo.this.skinType = p_180521_3_.getMetadata("model");
                                if (NetworkPlayerInfo.this.skinType != null) break;
                                NetworkPlayerInfo.this.skinType = "default";
                                break;
                            }
                            case CAPE: {
                                NetworkPlayerInfo.this.locationCape = p_180521_2_;
                            }
                        }
                    }
                }, true);
            }
        }
    }

    public void func_178859_a(IChatComponent p_178859_1_) {
        this.displayName = p_178859_1_;
    }

    public IChatComponent func_178854_k() {
        return this.displayName;
    }

    public int func_178835_l() {
        return this.field_178873_i;
    }

    public void func_178836_b(int p_178836_1_) {
        this.field_178873_i = p_178836_1_;
    }

    public int func_178860_m() {
        return this.field_178870_j;
    }

    public void func_178857_c(int p_178857_1_) {
        this.field_178870_j = p_178857_1_;
    }

    public long func_178847_n() {
        return this.field_178871_k;
    }

    public void func_178846_a(long p_178846_1_) {
        this.field_178871_k = p_178846_1_;
    }

    public long func_178858_o() {
        return this.field_178868_l;
    }

    public void func_178844_b(long p_178844_1_) {
        this.field_178868_l = p_178844_1_;
    }

    public long func_178855_p() {
        return this.field_178869_m;
    }

    public void func_178843_c(long p_178843_1_) {
        this.field_178869_m = p_178843_1_;
    }
}

