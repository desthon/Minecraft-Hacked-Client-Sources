/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerDeepOcean
extends GenLayer {
    private static final String __OBFID = "CL_00000546";

    public GenLayerDeepOcean(long p_i45472_1_, GenLayer p_i45472_3_) {
        super(p_i45472_1_);
        this.parent = p_i45472_3_;
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
                int var13 = var9[var12 + 1 + var11 * (areaWidth + 2)];
                int var14 = var9[var12 + 1 + 1 + (var11 + 1) * (areaWidth + 2)];
                int var15 = var9[var12 + (var11 + 1) * (areaWidth + 2)];
                int var16 = var9[var12 + 1 + (var11 + 1 + 1) * (areaWidth + 2)];
                int var17 = var9[var12 + 1 + (var11 + 1) * var7];
                int var18 = 0;
                if (var13 == 0) {
                    ++var18;
                }
                if (var14 == 0) {
                    ++var18;
                }
                if (var15 == 0) {
                    ++var18;
                }
                if (var16 == 0) {
                    ++var18;
                }
                var10[var12 + var11 * areaWidth] = var17 == 0 && var18 > 3 ? BiomeGenBase.deepOcean.biomeID : var17;
            }
        }
        return var10;
    }
}

