/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderBiped
extends RenderLiving {
    private static final ResourceLocation field_177118_j = new ResourceLocation("textures/entity/steve.png");
    protected ModelBiped modelBipedMain;
    protected float field_77070_b;
    private static final String __OBFID = "CL_00001001";

    public RenderBiped(RenderManager p_i46168_1_, ModelBiped p_i46168_2_, float p_i46168_3_) {
        this(p_i46168_1_, p_i46168_2_, p_i46168_3_, 1.0f);
        this.addLayer(new LayerHeldItem(this));
    }

    public RenderBiped(RenderManager p_i46169_1_, ModelBiped p_i46169_2_, float p_i46169_3_, float p_i46169_4_) {
        super(p_i46169_1_, p_i46169_2_, p_i46169_3_);
        this.modelBipedMain = p_i46169_2_;
        this.field_77070_b = p_i46169_4_;
        this.addLayer(new LayerCustomHead(p_i46169_2_.bipedHead));
    }

    protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_) {
        return field_177118_j;
    }

    @Override
    public void func_82422_c() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityLiving)p_110775_1_);
    }
}

