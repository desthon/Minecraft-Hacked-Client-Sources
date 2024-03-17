/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenLayerHills
extends GenLayer {
    private static final Logger logger = LogManager.getLogger();
    private GenLayer field_151628_d;
    private static final String __OBFID = "CL_00000563";

    public GenLayerHills(long p_i45479_1_, GenLayer p_i45479_3_, GenLayer p_i45479_4_) {
        super(p_i45479_1_);
        this.parent = p_i45479_3_;
        this.field_151628_d = p_i45479_4_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] var5 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] var6 = this.field_151628_d.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] var7 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int var8 = 0; var8 < areaHeight; ++var8) {
            for (int var9 = 0; var9 < areaWidth; ++var9) {
                int var14;
                boolean var12;
                this.initChunkSeed(var9 + areaX, var8 + areaY);
                int var10 = var5[var9 + 1 + (var8 + 1) * (areaWidth + 2)];
                int var11 = var6[var9 + 1 + (var8 + 1) * (areaWidth + 2)];
                boolean bl = var12 = (var11 - 2) % 29 == 0;
                if (var10 > 255) {
                    logger.debug("old! " + var10);
                }
                if (var10 != 0 && var11 >= 2 && (var11 - 2) % 29 == 1 && var10 < 128) {
                    if (BiomeGenBase.getBiome(var10 + 128) != null) {
                        var7[var9 + var8 * areaWidth] = var10 + 128;
                        continue;
                    }
                    var7[var9 + var8 * areaWidth] = var10;
                    continue;
                }
                if (this.nextInt(3) != 0 && !var12) {
                    var7[var9 + var8 * areaWidth] = var10;
                    continue;
                }
                int var13 = var10;
                if (var10 == BiomeGenBase.desert.biomeID) {
                    var13 = BiomeGenBase.desertHills.biomeID;
                } else if (var10 == BiomeGenBase.forest.biomeID) {
                    var13 = BiomeGenBase.forestHills.biomeID;
                } else if (var10 == BiomeGenBase.birchForest.biomeID) {
                    var13 = BiomeGenBase.birchForestHills.biomeID;
                } else if (var10 == BiomeGenBase.roofedForest.biomeID) {
                    var13 = BiomeGenBase.plains.biomeID;
                } else if (var10 == BiomeGenBase.taiga.biomeID) {
                    var13 = BiomeGenBase.taigaHills.biomeID;
                } else if (var10 == BiomeGenBase.megaTaiga.biomeID) {
                    var13 = BiomeGenBase.megaTaigaHills.biomeID;
                } else if (var10 == BiomeGenBase.coldTaiga.biomeID) {
                    var13 = BiomeGenBase.coldTaigaHills.biomeID;
                } else if (var10 == BiomeGenBase.plains.biomeID) {
                    var13 = this.nextInt(3) == 0 ? BiomeGenBase.forestHills.biomeID : BiomeGenBase.forest.biomeID;
                } else if (var10 == BiomeGenBase.icePlains.biomeID) {
                    var13 = BiomeGenBase.iceMountains.biomeID;
                } else if (var10 == BiomeGenBase.jungle.biomeID) {
                    var13 = BiomeGenBase.jungleHills.biomeID;
                } else if (var10 == BiomeGenBase.ocean.biomeID) {
                    var13 = BiomeGenBase.deepOcean.biomeID;
                } else if (var10 == BiomeGenBase.extremeHills.biomeID) {
                    var13 = BiomeGenBase.extremeHillsPlus.biomeID;
                } else if (var10 == BiomeGenBase.savanna.biomeID) {
                    var13 = BiomeGenBase.savannaPlateau.biomeID;
                } else if (GenLayerHills.biomesEqualOrMesaPlateau(var10, BiomeGenBase.mesaPlateau_F.biomeID)) {
                    var13 = BiomeGenBase.mesa.biomeID;
                } else if (var10 == BiomeGenBase.deepOcean.biomeID && this.nextInt(3) == 0) {
                    var14 = this.nextInt(2);
                    var13 = var14 == 0 ? BiomeGenBase.plains.biomeID : BiomeGenBase.forest.biomeID;
                }
                if (var12 && var13 != var10) {
                    var13 = BiomeGenBase.getBiome(var13 + 128) != null ? (var13 += 128) : var10;
                }
                if (var13 == var10) {
                    var7[var9 + var8 * areaWidth] = var10;
                    continue;
                }
                var14 = var5[var9 + 1 + var8 * (areaWidth + 2)];
                int var15 = var5[var9 + 1 + 1 + (var8 + 1) * (areaWidth + 2)];
                int var16 = var5[var9 + (var8 + 1) * (areaWidth + 2)];
                int var17 = var5[var9 + 1 + (var8 + 1 + 1) * (areaWidth + 2)];
                int var18 = 0;
                if (GenLayerHills.biomesEqualOrMesaPlateau(var14, var10)) {
                    ++var18;
                }
                if (GenLayerHills.biomesEqualOrMesaPlateau(var15, var10)) {
                    ++var18;
                }
                if (GenLayerHills.biomesEqualOrMesaPlateau(var16, var10)) {
                    ++var18;
                }
                if (GenLayerHills.biomesEqualOrMesaPlateau(var17, var10)) {
                    ++var18;
                }
                var7[var9 + var8 * areaWidth] = var18 >= 3 ? var13 : var10;
            }
        }
        return var7;
    }
}

