/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import skizzle.util.RenderUtil;

public class Gui {
    public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected float zLevel;
    private static final String __OBFID = "CL_00000662";

    protected void drawHorizontalLine(int startX, int endX, int y, int color) {
        if (endX < startX) {
            int var5 = startX;
            startX = endX;
            endX = var5;
        }
        Gui.drawRect(startX, y, endX + 1, y + 1, color);
    }

    protected void drawVerticalLine(int x, int startY, int endY, int color) {
        if (endY < startY) {
            int var5 = startY;
            startY = endY;
            endY = var5;
        }
        Gui.drawRect(x, startY + 1, x + 1, endY, color);
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtil.glColor(color);
        var10.startDrawingQuads();
        var10.addVertex(left, bottom, 0.0);
        var10.addVertex(right, bottom, 0.0);
        var10.addVertex(right, top, 0.0);
        var10.addVertex(left, top, 0.0);
        var9.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawStaticGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float var7 = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float var8 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float var9 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float var10 = (float)(startColor & 0xFF) / 255.0f;
        float var11 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float var12 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float var13 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float var14 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer var16 = var15.getWorldRenderer();
        var16.startDrawingQuads();
        var16.func_178960_a(var8, var9, var10, var7);
        var16.addVertex(right, top, 0.0);
        var16.func_178960_a(var12, var13, var14, var11);
        var16.addVertex(left, top, 0.0);
        var16.func_178960_a(var12, var13, var14, var11);
        var16.addVertex(left, bottom, 0.0);
        var16.func_178960_a(var8, var9, var10, var7);
        var16.addVertex(right, bottom, 0.0);
        var15.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawStaticGradientRect(int left, int top, int right, int bottom, int leftTopIn, int leftBottomIn, int rightTopIn, int rightBottomIn) {
        Color leftTop = new Color(leftTopIn, true);
        Color leftBottom = new Color(leftBottomIn, true);
        Color rightTop = new Color(rightTopIn, true);
        Color rightBottom = new Color(rightBottomIn, true);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer var16 = var15.getWorldRenderer();
        var16.startDrawingQuads();
        var16.func_178960_a((float)rightTop.getRed() / 255.0f, (float)rightTop.getGreen() / 255.0f, (float)rightTop.getBlue() / 255.0f, (float)rightTop.getAlpha() / 255.0f);
        var16.addVertex(right, top, 0.0);
        var16.func_178960_a((float)leftTop.getRed() / 255.0f, (float)leftTop.getGreen() / 255.0f, (float)leftTop.getBlue() / 255.0f, (float)leftTop.getAlpha() / 255.0f);
        var16.addVertex(left, top, 0.0);
        var16.func_178960_a((float)leftBottom.getRed() / 255.0f, (float)leftBottom.getGreen() / 255.0f, (float)leftBottom.getBlue() / 255.0f, (float)leftBottom.getAlpha() / 255.0f);
        var16.addVertex(left, bottom, 0.0);
        var16.func_178960_a((float)rightBottom.getRed() / 255.0f, (float)rightBottom.getGreen() / 255.0f, (float)rightBottom.getBlue() / 255.0f, (float)rightBottom.getAlpha() / 255.0f);
        var16.addVertex(right, bottom, 0.0);
        var15.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void drawStaticGradientRectVert(double left, double top, double right, double bottom, int startColor, int endColor) {
        float var7 = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float var8 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float var9 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float var10 = (float)(startColor & 0xFF) / 255.0f;
        float var11 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float var12 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float var13 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float var14 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer var16 = var15.getWorldRenderer();
        var16.startDrawingQuads();
        var16.func_178960_a(var8, var9, var10, var7);
        var16.addVertex(right, top, 0.0);
        var16.addVertex(left, top, 0.0);
        var16.func_178960_a(var12, var13, var14, var11);
        var16.addVertex(left, bottom, 0.0);
        var16.addVertex(right, bottom, 0.0);
        var15.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float var7 = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float var8 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float var9 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float var10 = (float)(startColor & 0xFF) / 255.0f;
        float var11 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float var12 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float var13 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float var14 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer var16 = var15.getWorldRenderer();
        var16.startDrawingQuads();
        var16.func_178960_a(var8, var9, var10, var7);
        var16.addVertex(right, top, this.zLevel);
        var16.addVertex(left, top, this.zLevel);
        var16.func_178960_a(var12, var13, var14, var11);
        var16.addVertex(left, bottom, this.zLevel);
        var16.addVertex(right, bottom, this.zLevel);
        var15.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, float f, float g, int color) {
        fontRendererIn.drawStringWithShadow(text, f - (float)(fontRendererIn.getStringWidth(text) / 2), g, color);
    }

    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawStringWithShadow(text, x, y, color);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float var7 = 0.00390625f;
        float var8 = 0.00390625f;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(x + 0, y + height, this.zLevel, (float)(textureX + 0) * var7, (float)(textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + height, this.zLevel, (float)(textureX + width) * var7, (float)(textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + 0, this.zLevel, (float)(textureX + width) * var7, (float)(textureY + 0) * var8);
        var10.addVertexWithUV(x + 0, y + 0, this.zLevel, (float)(textureX + 0) * var7, (float)(textureY + 0) * var8);
        var9.draw();
    }

    public void func_175174_a(float p_175174_1_, float p_175174_2_, int p_175174_3_, int p_175174_4_, int p_175174_5_, int p_175174_6_) {
        float var7 = 0.00390625f;
        float var8 = 0.00390625f;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(p_175174_1_ + 0.0f, p_175174_2_ + (float)p_175174_6_, this.zLevel, (float)(p_175174_3_ + 0) * var7, (float)(p_175174_4_ + p_175174_6_) * var8);
        var10.addVertexWithUV(p_175174_1_ + (float)p_175174_5_, p_175174_2_ + (float)p_175174_6_, this.zLevel, (float)(p_175174_3_ + p_175174_5_) * var7, (float)(p_175174_4_ + p_175174_6_) * var8);
        var10.addVertexWithUV(p_175174_1_ + (float)p_175174_5_, p_175174_2_ + 0.0f, this.zLevel, (float)(p_175174_3_ + p_175174_5_) * var7, (float)(p_175174_4_ + 0) * var8);
        var10.addVertexWithUV(p_175174_1_ + 0.0f, p_175174_2_ + 0.0f, this.zLevel, (float)(p_175174_3_ + 0) * var7, (float)(p_175174_4_ + 0) * var8);
        var9.draw();
    }

    public void func_175175_a(int p_175175_1_, int p_175175_2_, TextureAtlasSprite p_175175_3_, int p_175175_4_, int p_175175_5_) {
        Tessellator var6 = Tessellator.getInstance();
        WorldRenderer var7 = var6.getWorldRenderer();
        var7.startDrawingQuads();
        var7.addVertexWithUV(p_175175_1_ + 0, p_175175_2_ + p_175175_5_, this.zLevel, p_175175_3_.getMinU(), p_175175_3_.getMaxV());
        var7.addVertexWithUV(p_175175_1_ + p_175175_4_, p_175175_2_ + p_175175_5_, this.zLevel, p_175175_3_.getMaxU(), p_175175_3_.getMaxV());
        var7.addVertexWithUV(p_175175_1_ + p_175175_4_, p_175175_2_ + 0, this.zLevel, p_175175_3_.getMaxU(), p_175175_3_.getMinV());
        var7.addVertexWithUV(p_175175_1_ + 0, p_175175_2_ + 0, this.zLevel, p_175175_3_.getMinU(), p_175175_3_.getMinV());
        var6.draw();
    }

    public static void drawModalRectWithCustomSizedTexture(double x, double y, float u, float v, double width, double height, double textureWidth, double textureHeight) {
        double var8 = 1.0 / textureWidth;
        double var9 = 1.0 / textureHeight;
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        var11.startDrawingQuads();
        var11.addVertexWithUV(x, y + height, 0.0, (double)u * var8, (double)(v + (float)height) * var9);
        var11.addVertexWithUV(x + width, y + height, 0.0, (double)(u + (float)width) * var8, (double)(v + (float)height) * var9);
        var11.addVertexWithUV(x + width, y, 0.0, (double)(u + (float)width) * var8, (double)v * var9);
        var11.addVertexWithUV(x, y, 0.0, (double)u * var8, (double)v * var9);
        var10.draw();
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float var10 = 1.0f / tileWidth;
        float var11 = 1.0f / tileHeight;
        Tessellator var12 = Tessellator.getInstance();
        WorldRenderer var13 = var12.getWorldRenderer();
        var13.startDrawingQuads();
        var13.addVertexWithUV(x, y + height, 0.0, u * var10, (v + (float)vHeight) * var11);
        var13.addVertexWithUV(x + width, y + height, 0.0, (u + (float)uWidth) * var10, (v + (float)vHeight) * var11);
        var13.addVertexWithUV(x + width, y, 0.0, (u + (float)uWidth) * var10, v * var11);
        var13.addVertexWithUV(x, y, 0.0, u * var10, v * var11);
        var12.draw();
    }
}

