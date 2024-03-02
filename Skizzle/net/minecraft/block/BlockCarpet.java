/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarpet
extends Block {
    public static final PropertyEnum field_176330_a = PropertyEnum.create("color", EnumDyeColor.class);
    private static final String __OBFID = "CL_00000338";

    protected BlockCarpet() {
        super(Material.carpet);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176330_a, (Comparable)((Object)EnumDyeColor.WHITE)));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBoundsFromMeta(0);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBoundsFromMeta(0);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.setBlockBoundsFromMeta(0);
    }

    protected void setBlockBoundsFromMeta(int meta) {
        int var2 = 0;
        float var3 = (float)(1 * (1 + var2)) / 16.0f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, var3, 1.0f);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.checkAndDropBlock(worldIn, pos, state);
    }

    private boolean checkAndDropBlock(World worldIn, BlockPos p_176328_2_, IBlockState p_176328_3_) {
        if (!this.canBlockStay(worldIn, p_176328_2_)) {
            this.dropBlockAsItem(worldIn, p_176328_2_, p_176328_3_, 0);
            worldIn.setBlockToAir(p_176328_2_);
            return false;
        }
        return true;
    }

    private boolean canBlockStay(World worldIn, BlockPos p_176329_2_) {
        return !worldIn.isAirBlock(p_176329_2_.offsetDown());
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumDyeColor)((Object)state.getValue(field_176330_a))).func_176765_a();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (int var4 = 0; var4 < 16; ++var4) {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176330_a, (Comparable)((Object)EnumDyeColor.func_176764_b(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumDyeColor)((Object)state.getValue(field_176330_a))).func_176765_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176330_a);
    }
}

