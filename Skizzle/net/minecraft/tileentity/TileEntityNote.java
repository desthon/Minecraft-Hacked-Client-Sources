/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityNote
extends TileEntity {
    public byte note;
    public boolean previousRedstoneState;
    private static final String __OBFID = "CL_00000362";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("note", this.note);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.note = compound.getByte("note");
        this.note = (byte)MathHelper.clamp_int(this.note, 0, 24);
    }

    public void changePitch() {
        this.note = (byte)((this.note + 1) % 25);
        this.markDirty();
    }

    public void func_175108_a(World worldIn, BlockPos p_175108_2_) {
        if (worldIn.getBlockState(p_175108_2_.offsetUp()).getBlock().getMaterial() == Material.air) {
            Material var3 = worldIn.getBlockState(p_175108_2_.offsetDown()).getBlock().getMaterial();
            int var4 = 0;
            if (var3 == Material.rock) {
                var4 = 1;
            }
            if (var3 == Material.sand) {
                var4 = 2;
            }
            if (var3 == Material.glass) {
                var4 = 3;
            }
            if (var3 == Material.wood) {
                var4 = 4;
            }
            worldIn.addBlockEvent(p_175108_2_, Blocks.noteblock, var4, this.note);
        }
    }
}

