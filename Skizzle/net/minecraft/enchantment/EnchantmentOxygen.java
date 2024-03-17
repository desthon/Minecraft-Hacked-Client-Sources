/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentOxygen
extends Enchantment {
    private static final String __OBFID = "CL_00000120";

    public EnchantmentOxygen(int p_i45766_1_, ResourceLocation p_i45766_2_, int p_i45766_3_) {
        super(p_i45766_1_, p_i45766_2_, p_i45766_3_, EnumEnchantmentType.ARMOR_HEAD);
        this.setName("oxygen");
    }

    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return 10 * p_77321_1_;
    }

    @Override
    public int getMaxEnchantability(int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + 30;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}

