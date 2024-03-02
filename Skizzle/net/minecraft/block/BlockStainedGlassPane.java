/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockStainedGlassPane
extends BlockPane {
    public static final PropertyEnum field_176245_a = PropertyEnum.create("color", EnumDyeColor.class);
    private static final String __OBFID = "CL_00000313";

    public BlockStainedGlassPane() {
        super(Material.glass, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)).withProperty(field_176245_a, (Comparable)((Object)EnumDyeColor.WHITE)));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumDyeColor)((Object)state.getValue(field_176245_a))).func_176765_a();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (int var4 = 0; var4 < EnumDyeColor.values().length; ++var4) {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176245_a, (Comparable)((Object)EnumDyeColor.func_176764_b(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumDyeColor)((Object)state.getValue(field_176245_a))).func_176765_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, NORTH, EAST, WEST, SOUTH, field_176245_a);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.func_176450_d(worldIn, pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.func_176450_d(worldIn, pos);
        }
    }
}

