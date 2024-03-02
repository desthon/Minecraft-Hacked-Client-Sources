/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;

public class ChunkProviderFlat
implements IChunkProvider {
    private World worldObj;
    private Random random;
    private final IBlockState[] cachedBlockIDs = new IBlockState[256];
    private final FlatGeneratorInfo flatWorldGenInfo;
    private final List structureGenerators = Lists.newArrayList();
    private final boolean hasDecoration;
    private final boolean hasDungeons;
    private WorldGenLakes waterLakeGenerator;
    private WorldGenLakes lavaLakeGenerator;
    private static final String __OBFID = "CL_00000391";

    public ChunkProviderFlat(World worldIn, long p_i2004_2_, boolean p_i2004_4_, String p_i2004_5_) {
        this.worldObj = worldIn;
        this.random = new Random(p_i2004_2_);
        this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(p_i2004_5_);
        if (p_i2004_4_) {
            Map var6 = this.flatWorldGenInfo.getWorldFeatures();
            if (var6.containsKey("village")) {
                Map var7 = (Map)var6.get("village");
                if (!var7.containsKey("size")) {
                    var7.put("size", "1");
                }
                this.structureGenerators.add(new MapGenVillage(var7));
            }
            if (var6.containsKey("biome_1")) {
                this.structureGenerators.add(new MapGenScatteredFeature((Map)var6.get("biome_1")));
            }
            if (var6.containsKey("mineshaft")) {
                this.structureGenerators.add(new MapGenMineshaft((Map)var6.get("mineshaft")));
            }
            if (var6.containsKey("stronghold")) {
                this.structureGenerators.add(new MapGenStronghold((Map)var6.get("stronghold")));
            }
            if (var6.containsKey("oceanmonument")) {
                this.structureGenerators.add(new StructureOceanMonument((Map)var6.get("oceanmonument")));
            }
        }
        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake")) {
            this.waterLakeGenerator = new WorldGenLakes(Blocks.water);
        }
        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake")) {
            this.lavaLakeGenerator = new WorldGenLakes(Blocks.lava);
        }
        this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
        boolean var11 = true;
        for (FlatLayerInfo var8 : this.flatWorldGenInfo.getFlatLayers()) {
            for (int var9 = var8.getMinY(); var9 < var8.getMinY() + var8.getLayerCount(); ++var9) {
                IBlockState var10 = var8.func_175900_c();
                if (var10.getBlock() == Blocks.air) continue;
                var11 = false;
                this.cachedBlockIDs[var9] = var10;
            }
        }
        this.hasDecoration = var11 ? false : this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration");
    }

    @Override
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        int var7;
        ChunkPrimer var3 = new ChunkPrimer();
        for (int var4 = 0; var4 < this.cachedBlockIDs.length; ++var4) {
            IBlockState var5 = this.cachedBlockIDs[var4];
            if (var5 == null) continue;
            for (int var6 = 0; var6 < 16; ++var6) {
                for (var7 = 0; var7 < 16; ++var7) {
                    var3.setBlockState(var6, var4, var7, var5);
                }
            }
        }
        for (MapGenBase var10 : this.structureGenerators) {
            var10.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        Chunk var9 = new Chunk(this.worldObj, var3, p_73154_1_, p_73154_2_);
        BiomeGenBase[] var11 = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        byte[] var12 = var9.getBiomeArray();
        for (var7 = 0; var7 < var12.length; ++var7) {
            var12[var7] = (byte)var11[var7].biomeID;
        }
        var9.generateSkylightMap();
        return var9;
    }

    @Override
    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        BlockPos var17;
        int var4 = p_73153_2_ * 16;
        int var5 = p_73153_3_ * 16;
        BlockPos var6 = new BlockPos(var4, 0, var5);
        BiomeGenBase var7 = this.worldObj.getBiomeGenForCoords(new BlockPos(var4 + 16, 0, var5 + 16));
        boolean var8 = false;
        this.random.setSeed(this.worldObj.getSeed());
        long var9 = this.random.nextLong() / 2L * 2L + 1L;
        long var11 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long)p_73153_2_ * var9 + (long)p_73153_3_ * var11 ^ this.worldObj.getSeed());
        ChunkCoordIntPair var13 = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
        for (MapGenStructure var15 : this.structureGenerators) {
            boolean var16 = var15.func_175794_a(this.worldObj, this.random, var13);
            if (!(var15 instanceof MapGenVillage)) continue;
            var8 |= var16;
        }
        if (this.waterLakeGenerator != null && !var8 && this.random.nextInt(4) == 0) {
            this.waterLakeGenerator.generate(this.worldObj, this.random, var6.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
        }
        if (!(this.lavaLakeGenerator == null || var8 || this.random.nextInt(8) != 0 || (var17 = var6.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8)).getY() >= 63 && this.random.nextInt(10) != 0)) {
            this.lavaLakeGenerator.generate(this.worldObj, this.random, var17);
        }
        if (this.hasDungeons) {
            for (int var18 = 0; var18 < 8; ++var18) {
                new WorldGenDungeons().generate(this.worldObj, this.random, var6.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
            }
        }
        if (this.hasDecoration) {
            var7.func_180624_a(this.worldObj, this.random, new BlockPos(var4, 0, var5));
        }
    }

    @Override
    public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
        return false;
    }

    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return true;
    }

    @Override
    public void saveExtraData() {
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return "FlatLevelSource";
    }

    @Override
    public List func_177458_a(EnumCreatureType p_177458_1_, BlockPos p_177458_2_) {
        BiomeGenBase var3 = this.worldObj.getBiomeGenForCoords(p_177458_2_);
        return var3.getSpawnableList(p_177458_1_);
    }

    @Override
    public BlockPos func_180513_a(World worldIn, String p_180513_2_, BlockPos p_180513_3_) {
        if ("Stronghold".equals(p_180513_2_)) {
            for (MapGenStructure var5 : this.structureGenerators) {
                if (!(var5 instanceof MapGenStronghold)) continue;
                return var5.func_180706_b(worldIn, p_180513_3_);
            }
        }
        return null;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void func_180514_a(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {
        for (MapGenStructure var5 : this.structureGenerators) {
            var5.func_175792_a(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
    }

    @Override
    public Chunk func_177459_a(BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
}

