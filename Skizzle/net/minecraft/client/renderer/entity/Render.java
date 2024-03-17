/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.entity;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import optifine.Config;
import org.lwjgl.opengl.GL11;
import shadersmod.client.Shaders;
import skizzle.Client;
import skizzle.modules.ModuleManager;

public abstract class Render {
    private static final ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
    protected final RenderManager renderManager;
    protected float shadowSize;
    protected float shadowOpaque = 1.0f;

    protected Render(RenderManager p_i46179_1_) {
        this.renderManager = p_i46179_1_;
    }

    public boolean func_177071_a(Entity p_177071_1_, ICamera p_177071_2_, double p_177071_3_, double p_177071_5_, double p_177071_7_) {
        return p_177071_1_.isInRangeToRender3d(p_177071_3_, p_177071_5_, p_177071_7_) && (p_177071_1_.ignoreFrustumCheck || p_177071_2_.isBoundingBoxInFrustum(p_177071_1_.getEntityBoundingBox()));
    }

    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.func_177067_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
    }

    public void doRenderInteresting(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.func_177067_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
    }

    protected void func_177067_a(Entity p_177067_1_, double p_177067_2_, double p_177067_4_, double p_177067_6_) {
        if (this.func_177070_b(p_177067_1_)) {
            this.renderLivingLabel(p_177067_1_, p_177067_1_.getDisplayName().getFormattedText(), p_177067_2_, p_177067_4_, p_177067_6_, 64);
        }
    }

    protected boolean func_177070_b(Entity p_177070_1_) {
        return p_177070_1_.getAlwaysRenderNameTagForRender() && p_177070_1_.hasCustomName();
    }

    protected void func_177069_a(Entity p_177069_1_, double p_177069_2_, double p_177069_4_, double p_177069_6_, String p_177069_8_, float p_177069_9_, double p_177069_10_) {
        this.renderLivingLabel(p_177069_1_, p_177069_8_, p_177069_2_, p_177069_4_, p_177069_6_, 128);
    }

    protected abstract ResourceLocation getEntityTexture(Entity var1);

    protected boolean bindEntityTexture(Entity p_180548_1_) {
        ResourceLocation var2 = this.getEntityTexture(p_180548_1_);
        if (var2 == null) {
            return false;
        }
        this.bindTexture(var2);
        return true;
    }

    public void bindTexture(ResourceLocation p_110776_1_) {
        this.renderManager.renderEngine.bindTexture(p_110776_1_);
    }

    private void renderEntityOnFire(Entity p_76977_1_, double p_76977_2_, double p_76977_4_, double p_76977_6_, float p_76977_8_) {
        GlStateManager.disableLighting();
        TextureMap var9 = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite var10 = var9.getAtlasSprite("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite var11 = var9.getAtlasSprite("minecraft:blocks/fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_76977_2_, (float)p_76977_4_, (float)p_76977_6_);
        float var12 = p_76977_1_.width * 1.4f;
        GlStateManager.scale(var12, var12, var12);
        Tessellator var13 = Tessellator.getInstance();
        WorldRenderer var14 = var13.getWorldRenderer();
        float var15 = 0.5f;
        float var16 = 0.0f;
        float var17 = p_76977_1_.height / var12;
        float var18 = (float)(p_76977_1_.posY - p_76977_1_.getEntityBoundingBox().minY);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, -0.3f + (float)((int)var17) * 0.02f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float var19 = 0.0f;
        int var20 = 0;
        var14.startDrawingQuads();
        while (var17 > 0.0f) {
            TextureAtlasSprite var21 = var20 % 2 == 0 ? var10 : var11;
            this.bindTexture(TextureMap.locationBlocksTexture);
            float var22 = var21.getMinU();
            float var23 = var21.getMinV();
            float var24 = var21.getMaxU();
            float var25 = var21.getMaxV();
            if (var20 / 2 % 2 == 0) {
                float var26 = var24;
                var24 = var22;
                var22 = var26;
            }
            var14.addVertexWithUV(var15 - var16, 0.0f - var18, var19, var24, var25);
            var14.addVertexWithUV(-var15 - var16, 0.0f - var18, var19, var22, var25);
            var14.addVertexWithUV(-var15 - var16, 1.4f - var18, var19, var22, var23);
            var14.addVertexWithUV(var15 - var16, 1.4f - var18, var19, var24, var23);
            var17 -= 0.45f;
            var18 -= 0.45f;
            var15 *= 0.9f;
            var19 += 0.03f;
            ++var20;
        }
        var13.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    private void renderShadow(Entity p_76975_1_, double p_76975_2_, double p_76975_4_, double p_76975_6_, float p_76975_8_, float p_76975_9_) {
        if (!Config.isShaders() || !Shaders.shouldSkipDefaultShadow) {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            this.renderManager.renderEngine.bindTexture(shadowTextures);
            World var10 = this.getWorldFromRenderManager();
            GlStateManager.depthMask(false);
            float var11 = this.shadowSize;
            if (p_76975_1_ instanceof EntityLiving) {
                EntityLiving var35 = (EntityLiving)p_76975_1_;
                var11 *= var35.getRenderSizeModifier();
                if (var35.isChild()) {
                    var11 *= 0.5f;
                }
            }
            double var351 = p_76975_1_.lastTickPosX + (p_76975_1_.posX - p_76975_1_.lastTickPosX) * (double)p_76975_9_;
            double var14 = p_76975_1_.lastTickPosY + (p_76975_1_.posY - p_76975_1_.lastTickPosY) * (double)p_76975_9_;
            double var16 = p_76975_1_.lastTickPosZ + (p_76975_1_.posZ - p_76975_1_.lastTickPosZ) * (double)p_76975_9_;
            int var18 = MathHelper.floor_double(var351 - (double)var11);
            int var19 = MathHelper.floor_double(var351 + (double)var11);
            int var20 = MathHelper.floor_double(var14 - (double)var11);
            int var21 = MathHelper.floor_double(var14);
            int var22 = MathHelper.floor_double(var16 - (double)var11);
            int var23 = MathHelper.floor_double(var16 + (double)var11);
            double var24 = p_76975_2_ - var351;
            double var26 = p_76975_4_ - var14;
            double var28 = p_76975_6_ - var16;
            Tessellator var30 = Tessellator.getInstance();
            WorldRenderer var31 = var30.getWorldRenderer();
            var31.startDrawingQuads();
            for (BlockPos var33 : BlockPos.getAllInBox(new BlockPos(var18, var20, var22), new BlockPos(var19, var21, var23))) {
                Block var34 = var10.getBlockState(var33.offsetDown()).getBlock();
                if (var34.getRenderType() == -1 || var10.getLightFromNeighbors(var33) <= 3) continue;
                this.func_180549_a(var34, p_76975_2_, p_76975_4_, p_76975_6_, var33, p_76975_8_, var11, var24, var26, var28);
            }
            var30.draw();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
        }
    }

    private World getWorldFromRenderManager() {
        return this.renderManager.worldObj;
    }

    private void func_180549_a(Block p_180549_1_, double p_180549_2_, double p_180549_4_, double p_180549_6_, BlockPos p_180549_8_, float p_180549_9_, float p_180549_10_, double p_180549_11_, double p_180549_13_, double p_180549_15_) {
        if (p_180549_1_.isFullCube()) {
            Tessellator var17 = Tessellator.getInstance();
            WorldRenderer var18 = var17.getWorldRenderer();
            double var19 = ((double)p_180549_9_ - (p_180549_4_ - ((double)p_180549_8_.getY() + p_180549_13_)) / 2.0) * 0.5 * (double)this.getWorldFromRenderManager().getLightBrightness(p_180549_8_);
            if (var19 >= 0.0) {
                if (var19 > 1.0) {
                    var19 = 1.0;
                }
                var18.func_178960_a(1.0f, 1.0f, 1.0f, (float)var19);
                double var21 = (double)p_180549_8_.getX() + p_180549_1_.getBlockBoundsMinX() + p_180549_11_;
                double var23 = (double)p_180549_8_.getX() + p_180549_1_.getBlockBoundsMaxX() + p_180549_11_;
                double var25 = (double)p_180549_8_.getY() + p_180549_1_.getBlockBoundsMinY() + p_180549_13_ + 0.015625;
                double var27 = (double)p_180549_8_.getZ() + p_180549_1_.getBlockBoundsMinZ() + p_180549_15_;
                double var29 = (double)p_180549_8_.getZ() + p_180549_1_.getBlockBoundsMaxZ() + p_180549_15_;
                float var31 = (float)((p_180549_2_ - var21) / 2.0 / (double)p_180549_10_ + 0.5);
                float var32 = (float)((p_180549_2_ - var23) / 2.0 / (double)p_180549_10_ + 0.5);
                float var33 = (float)((p_180549_6_ - var27) / 2.0 / (double)p_180549_10_ + 0.5);
                float var34 = (float)((p_180549_6_ - var29) / 2.0 / (double)p_180549_10_ + 0.5);
                var18.addVertexWithUV(var21, var25, var27, var31, var33);
                var18.addVertexWithUV(var21, var25, var29, var31, var34);
                var18.addVertexWithUV(var23, var25, var29, var32, var34);
                var18.addVertexWithUV(var23, var25, var27, var32, var33);
            }
        }
    }

    public static void renderOffsetAABB(AxisAlignedBB p_76978_0_, double p_76978_1_, double p_76978_3_, double p_76978_5_) {
        GlStateManager.disableTexture2D();
        Tessellator var7 = Tessellator.getInstance();
        WorldRenderer var8 = var7.getWorldRenderer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        var8.startDrawingQuads();
        var8.setTranslation(p_76978_1_, p_76978_3_, p_76978_5_);
        var8.func_178980_d(0.0f, 0.0f, -1.0f);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.func_178980_d(0.0f, 0.0f, 1.0f);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.func_178980_d(0.0f, -1.0f, 0.0f);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.func_178980_d(0.0f, 1.0f, 0.0f);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.func_178980_d(-1.0f, 0.0f, 0.0f);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.func_178980_d(1.0f, 0.0f, 0.0f);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.setTranslation(0.0, 0.0, 0.0);
        var7.draw();
        GlStateManager.enableTexture2D();
    }

    public void doRenderShadowAndFire(Entity p_76979_1_, double p_76979_2_, double p_76979_4_, double p_76979_6_, float p_76979_8_, float p_76979_9_) {
        if (this.renderManager.options != null) {
            double var10;
            float var12;
            if (this.renderManager.options.fancyGraphics && this.shadowSize > 0.0f && !p_76979_1_.isInvisible() && this.renderManager.func_178627_a() && (var12 = (float)((1.0 - (var10 = this.renderManager.getDistanceToCamera(p_76979_1_.posX, p_76979_1_.posY, p_76979_1_.posZ)) / 256.0) * (double)this.shadowOpaque)) > 0.0f) {
                this.renderShadow(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, var12, p_76979_9_);
            }
            if (!(!p_76979_1_.canRenderOnFire() || p_76979_1_ instanceof EntityPlayer && ((EntityPlayer)p_76979_1_).func_175149_v())) {
                this.renderEntityOnFire(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, p_76979_9_);
            }
        }
    }

    public FontRenderer getFontRendererFromRenderManager() {
        return this.renderManager.getFontRenderer();
    }

    protected void renderLivingLabel(Entity entity, String displayName, double p_147906_3_, double p_147906_5_, double p_147906_7_, int p_147906_9_) {
        double var10 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
        Minecraft mc = Minecraft.getMinecraft();
        float distanceToEntity = mc.thePlayer.getDistanceToEntity(entity);
        if (ModuleManager.nameTags.isEnabled()) {
            var10 = 0.0;
        }
        if (var10 <= (double)(p_147906_9_ * p_147906_9_) && !entity.noTag) {
            FontRenderer var12;
            FontRenderer fr = var12 = this.getFontRendererFromRenderManager();
            float var13 = 1.6f;
            float var14 = 0.016666668f * var13;
            if (ModuleManager.nameTags.isEnabled() && entity instanceof EntityPlayer) {
                Double scale = 12.0 - ModuleManager.nameTags.scale.getValue();
                var14 = (float)((double)(0.16666669f * distanceToEntity) / (scale * 10.0)) + 0.014666668f * var13;
            }
            GlStateManager.pushMatrix();
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)2929);
            GlStateManager.translate((float)p_147906_3_ + 0.0f, (float)p_147906_5_ + entity.height + 0.5f, (float)p_147906_7_);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-var14, -var14, var14);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator var15 = Tessellator.getInstance();
            WorldRenderer var16 = var15.getWorldRenderer();
            int var17 = 0;
            if (ModuleManager.nameProtect.others.isEnabled() && ModuleManager.nameProtect.isEnabled()) {
                Minecraft.getMinecraft();
                Iterator iterator = Minecraft.theWorld.playerEntities.iterator();
                while (iterator.hasNext()) {
                    iterator.next();
                }
            }
            if (displayName.equals("deadmau5")) {
                var17 = -10;
            }
            GlStateManager.disableTexture2D();
            var16.startDrawingQuads();
            String name = displayName;
            int var18 = var12.getStringWidth(name) / 2;
            if (!Client.ghostMode && ModuleManager.nameTags.isEnabled()) {
                var18 = fr.getStringWidth(name) / 2;
            }
            if (!ModuleManager.nameTags.isEnabled()) {
                var16.func_178960_a(0.0f, 0.0f, 0.0f, 0.25f);
                var16.addVertex(-var18 - 1, -1 + var17, 0.0);
                var16.addVertex(-var18 - 1, 8 + var17, 0.0);
                var16.addVertex(var18 + 1, 8 + var17, 0.0);
                var16.addVertex(var18 + 1, -1 + var17, 0.0);
            }
            var15.draw();
            GlStateManager.enableTexture2D();
            if (Client.skizzleUsers.contains(entity.getName()) && entity instanceof EntityPlayer) {
                GlStateManager.scale(0.6, 0.6, 0.0);
                var12.drawStringNormal("Skizzle", -var12.getStringWidth("Skizzle") / 2, var17 - 30, -16740097);
                GlStateManager.scale(1.6, 1.6, 0.0);
            }
            if (ModuleManager.nameTags.isEnabled()) {
                ModuleManager.nameTags.renderNameTag(var17, entity, displayName);
            } else {
                var12.drawStringNormal(name, -var12.getStringWidth(name) / 2, var17, 0x20FFFFFF);
            }
            GlStateManager.enableDepth();
            if (!ModuleManager.nameTags.isEnabled()) {
                var12.drawStringNormal(name, -var12.getStringWidth(name) / 2, var17, -1);
            }
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }

    public RenderManager func_177068_d() {
        return this.renderManager;
    }
}

