/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockSand
extends BlockFalling {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
    private static final String __OBFID = "CL_00000303";

    public BlockSand() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.SAND)));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumType)((Object)state.getValue(VARIANT_PROP))).func_176688_a();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176688_a()));
        }
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return ((EnumType)((Object)state.getValue(VARIANT_PROP))).func_176687_c();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumType.func_176686_a(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType)((Object)state.getValue(VARIANT_PROP))).func_176688_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT_PROP);
    }

    public static enum EnumType implements IStringSerializable
    {
        SAND("SAND", 0, 0, "sand", "default", MapColor.sandColor),
        RED_SAND("RED_SAND", 1, 1, "red_sand", "red", MapColor.dirtColor);

        private static final EnumType[] field_176695_c;
        private final int field_176692_d;
        private final String field_176693_e;
        private final MapColor field_176690_f;
        private final String field_176691_g;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002069";

        static {
            field_176695_c = new EnumType[EnumType.values().length];
            $VALUES = new EnumType[]{SAND, RED_SAND};
            EnumType[] var0 = EnumType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumType var3;
                EnumType.field_176695_c[var3.func_176688_a()] = var3 = var0[var2];
            }
        }

        private EnumType(String p_i45687_1_, int p_i45687_2_, int p_i45687_3_, String p_i45687_4_, String p_i45687_5_, MapColor p_i45687_6_) {
            this.field_176692_d = p_i45687_3_;
            this.field_176693_e = p_i45687_4_;
            this.field_176690_f = p_i45687_6_;
            this.field_176691_g = p_i45687_5_;
        }

        public int func_176688_a() {
            return this.field_176692_d;
        }

        public String toString() {
            return this.field_176693_e;
        }

        public MapColor func_176687_c() {
            return this.field_176690_f;
        }

        public static EnumType func_176686_a(int p_176686_0_) {
            if (p_176686_0_ < 0 || p_176686_0_ >= field_176695_c.length) {
                p_176686_0_ = 0;
            }
            return field_176695_c[p_176686_0_];
        }

        @Override
        public String getName() {
            return this.field_176693_e;
        }

        public String func_176685_d() {
            return this.field_176691_g;
        }
    }
}

