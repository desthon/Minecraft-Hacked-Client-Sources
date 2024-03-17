/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemReed
extends Item {
    private Block field_150935_a;
    private static final String __OBFID = "CL_00001773";

    public ItemReed(Block p_i45329_1_) {
        this.field_150935_a = p_i45329_1_;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState var11;
        IBlockState var9 = worldIn.getBlockState(pos);
        Block var10 = var9.getBlock();
        if (var10 == Blocks.snow_layer && (Integer)var9.getValue(BlockSnow.LAYERS_PROP) < 1) {
            side = EnumFacing.UP;
        } else if (!var10.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        if (!playerIn.func_175151_a(pos, side, stack)) {
            return false;
        }
        if (stack.stackSize == 0) {
            return false;
        }
        if (worldIn.canBlockBePlaced(this.field_150935_a, pos, false, side, null, stack) && worldIn.setBlockState(pos, var11 = this.field_150935_a.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, playerIn), 3)) {
            var11 = worldIn.getBlockState(pos);
            if (var11.getBlock() == this.field_150935_a) {
                ItemBlock.setTileEntityNBT(worldIn, pos, stack);
                var11.getBlock().onBlockPlacedBy(worldIn, pos, var11, playerIn, stack);
            }
            worldIn.playSoundEffect((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, this.field_150935_a.stepSound.getPlaceSound(), (this.field_150935_a.stepSound.getVolume() + 1.0f) / 2.0f, this.field_150935_a.stepSound.getFrequency() * 0.8f);
            --stack.stackSize;
            return true;
        }
        return false;
    }
}

