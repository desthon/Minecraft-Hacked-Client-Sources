/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.ResourceLocation;

public class RenderSilverfish
extends RenderLiving {
    private static final ResourceLocation silverfishTextures = new ResourceLocation("textures/entity/silverfish.png");
    private static final String __OBFID = "CL_00001022";

    public RenderSilverfish(RenderManager p_i46144_1_) {
        super(p_i46144_1_, new ModelSilverfish(), 0.3f);
    }

    protected float func_180584_a(EntitySilverfish p_180584_1_) {
        return 180.0f;
    }

    protected ResourceLocation getEntityTexture(EntitySilverfish p_110775_1_) {
        return silverfishTextures;
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase p_77037_1_) {
        return this.func_180584_a((EntitySilverfish)p_77037_1_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntitySilverfish)p_110775_1_);
    }
}

