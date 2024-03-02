/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MobAppearance
extends EntityFX {
    private EntityLivingBase field_174844_a;
    private static final String __OBFID = "CL_00002594";

    protected MobAppearance(World worldIn, double p_i46283_2_, double p_i46283_4_, double p_i46283_6_) {
        super(worldIn, p_i46283_2_, p_i46283_4_, p_i46283_6_, 0.0, 0.0, 0.0);
        this.particleBlue = 1.0f;
        this.particleGreen = 1.0f;
        this.particleRed = 1.0f;
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
        this.particleGravity = 0.0f;
        this.particleMaxAge = 30;
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.field_174844_a == null) {
            EntityGuardian var1 = new EntityGuardian(this.worldObj);
            var1.func_175465_cm();
            this.field_174844_a = var1;
        }
    }

    @Override
    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        if (this.field_174844_a != null) {
            RenderManager var9 = Minecraft.getMinecraft().getRenderManager();
            var9.setRenderPosition(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
            float var10 = 0.42553192f;
            float var11 = ((float)this.particleAge + p_180434_3_) / (float)this.particleMaxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(770, 771);
            float var12 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var12, var12);
            GlStateManager.pushMatrix();
            float var13 = 0.05f + 0.5f * MathHelper.sin(var11 * (float)Math.PI);
            GlStateManager.color(1.0f, 1.0f, 1.0f, var13);
            GlStateManager.translate(0.0f, 1.8f, 0.0f);
            GlStateManager.rotate(180.0f - p_180434_2_.rotationYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(60.0f - 150.0f * var11 - p_180434_2_.rotationPitch, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.4f, -1.5f);
            GlStateManager.scale(var10, var10, var10);
            this.field_174844_a.prevRotationYaw = 0.0f;
            this.field_174844_a.rotationYaw = 0.0f;
            this.field_174844_a.prevRotationYawHead = 0.0f;
            this.field_174844_a.rotationYawHead = 0.0f;
            var9.renderEntityWithPosYaw(this.field_174844_a, 0.0, 0.0, 0.0, 0.0f, p_180434_3_, true);
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }

    public static class Factory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002593";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new MobAppearance(worldIn, p_178902_3_, p_178902_5_, p_178902_7_);
        }
    }
}

