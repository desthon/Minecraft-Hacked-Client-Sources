/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerRiver
extends GenLayer {
    private static final String __OBFID = "CL_00000566";

    public GenLayerRiver(long p_i2128_1_, GenLayer p_i2128_3_) {
        super(p_i2128_1_);
        this.parent = p_i2128_3_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int var5 = areaX - 1;
        int var6 = areaY - 1;
        int var7 = areaWidth + 2;
        int var8 = areaHeight + 2;
        int[] var9 = this.parent.getInts(var5, var6, var7, var8);
        int[] var10 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int var11 = 0; var11 < areaHeight; ++var11) {
            for (int var12 = 0; var12 < areaWidth; ++var12) {
                int var13 = this.func_151630_c(var9[var12 + 0 + (var11 + 1) * var7]);
                int var14 = this.func_151630_c(var9[var12 + 2 + (var11 + 1) * var7]);
                int var15 = this.func_151630_c(var9[var12 + 1 + (var11 + 0) * var7]);
                int var16 = this.func_151630_c(var9[var12 + 1 + (var11 + 2) * var7]);
                int var17 = this.func_151630_c(var9[var12 + 1 + (var11 + 1) * var7]);
                var10[var12 + var11 * areaWidth] = var17 == var13 && var17 == var15 && var17 == var14 && var17 == var16 ? -1 : BiomeGenBase.river.biomeID;
            }
        }
        return var10;
    }

    private int func_151630_c(int p_151630_1_) {
        return p_151630_1_ >= 2 ? 2 + (p_151630_1_ & 1) : p_151630_1_;
    }
}

