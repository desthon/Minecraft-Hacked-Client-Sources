/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.text.ArabicShaping
 *  com.ibm.icu.text.ArabicShapingException
 *  com.ibm.icu.text.Bidi
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.gui;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomColors;
import optifine.FontUtils;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import skizzle.friends.Friend;
import skizzle.friends.FriendManager;
import skizzle.modules.ModuleManager;

public class FontRenderer
implements IResourceManagerReloadListener {
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    private float[] charWidth = new float[256];
    public int FONT_HEIGHT = 9;
    public Random fontRandom = new Random();
    private byte[] glyphWidth = new byte[65536];
    private int[] colorCode = new int[32];
    private ResourceLocation locationFontTexture;
    private final TextureManager renderEngine;
    private float posX;
    private float posY;
    private boolean unicodeFlag;
    private boolean bidiFlag;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private int textColor;
    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;
    private static final String __OBFID = "CL_00000660";
    public GameSettings gameSettings;
    public ResourceLocation locationFontTextureBase;
    public boolean enabled = true;
    public float offsetBold = 1.0f;

    public FontRenderer(GameSettings p_i1035_1_, ResourceLocation p_i1035_2_, TextureManager p_i1035_3_, boolean p_i1035_4_) {
        this.gameSettings = p_i1035_1_;
        this.locationFontTextureBase = p_i1035_2_;
        this.locationFontTexture = p_i1035_2_;
        this.renderEngine = p_i1035_3_;
        this.unicodeFlag = p_i1035_4_;
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        this.bindTexture(this.locationFontTexture);
        for (int var5 = 0; var5 < 32; ++var5) {
            int var6 = (var5 >> 3 & 1) * 85;
            int var7 = (var5 >> 2 & 1) * 170 + var6;
            int var8 = (var5 >> 1 & 1) * 170 + var6;
            int var9 = (var5 >> 0 & 1) * 170 + var6;
            if (var5 == 6) {
                var7 += 85;
            }
            if (p_i1035_1_.anaglyph) {
                int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
                int var11 = (var7 * 30 + var8 * 70) / 100;
                int var12 = (var7 * 30 + var9 * 70) / 100;
                var7 = var10;
                var8 = var11;
                var9 = var12;
            }
            if (var5 >= 16) {
                var7 /= 4;
                var8 /= 4;
                var9 /= 4;
            }
            this.colorCode[var5] = (var7 & 0xFF) << 16 | (var8 & 0xFF) << 8 | var9 & 0xFF;
        }
        this.readGlyphSizes();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        for (int i = 0; i < unicodePageLocations.length; ++i) {
            FontRenderer.unicodePageLocations[i] = null;
        }
        this.readFontTexture();
        this.readGlyphSizes();
    }

    private void readFontTexture() {
        BufferedImage bufferedimage;
        try {
            bufferedimage = TextureUtil.func_177053_a(this.getResourceInputStream(this.locationFontTexture));
        }
        catch (IOException var21) {
            throw new RuntimeException(var21);
        }
        Properties props = FontUtils.readFontProperties(this.locationFontTexture);
        int imgWidth = bufferedimage.getWidth();
        int imgHeight = bufferedimage.getHeight();
        int charW = imgWidth / 16;
        int charH = imgHeight / 16;
        float kx = (float)imgWidth / 128.0f;
        float boldScaleFactor = Config.limit(kx, 1.0f, 2.0f);
        this.offsetBold = 1.0f / boldScaleFactor;
        float offsetBoldConfig = FontUtils.readFloat(props, "offsetBold", -1.0f);
        if (offsetBoldConfig >= 0.0f) {
            this.offsetBold = offsetBoldConfig;
        }
        int[] ai = new int[imgWidth * imgHeight];
        bufferedimage.getRGB(0, 0, imgWidth, imgHeight, ai, 0, imgWidth);
        for (int k = 0; k < 256; ++k) {
            int var22;
            int cx = k % 16;
            int cy = k / 16;
            boolean px = false;
            for (var22 = charW - 1; var22 >= 0; --var22) {
                int x = cx * charW + var22;
                boolean flag = true;
                for (int py = 0; py < charH && flag; ++py) {
                    int ypos = (cy * charH + py) * imgWidth;
                    int col = ai[x + ypos];
                    int al = col >> 24 & 0xFF;
                    if (al <= 16) continue;
                    flag = false;
                }
                if (!flag) break;
            }
            if (k == 32) {
                var22 = charW <= 8 ? (int)(2.0f * kx) : (int)(1.5f * kx);
            }
            this.charWidth[k] = (float)(var22 + 1) / kx + 1.0f;
        }
        FontUtils.readCustomCharWidths(props, this.charWidth);
    }

    private void readGlyphSizes() {
        InputStream var1 = null;
        try {
            try {
                var1 = this.getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
                var1.read(this.glyphWidth);
            }
            catch (IOException var6) {
                throw new RuntimeException(var6);
            }
        }
        catch (Throwable throwable) {
            IOUtils.closeQuietly(var1);
            throw throwable;
        }
        IOUtils.closeQuietly((InputStream)var1);
    }

    private float renderCharAtPos(int p_78278_1_, char p_78278_2_, boolean p_78278_3_) {
        return p_78278_2_ == ' ' ? (!this.unicodeFlag ? this.charWidth[p_78278_2_] : 4.0f) : (p_78278_2_ == ' ' ? 4.0f : ("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(p_78278_2_) != -1 && !this.unicodeFlag ? this.renderDefaultChar(p_78278_1_, p_78278_3_) : this.renderUnicodeChar(p_78278_2_, p_78278_3_)));
    }

    private float renderDefaultChar(int p_78266_1_, boolean p_78266_2_) {
        float var3 = p_78266_1_ % 16 * 8;
        float var4 = p_78266_1_ / 16 * 8;
        float var5 = p_78266_2_ ? 1.0f : 0.0f;
        this.bindTexture(this.locationFontTexture);
        float var6 = 7.99f;
        GL11.glBegin((int)5);
        GL11.glTexCoord2f((float)(var3 / 128.0f), (float)(var4 / 128.0f));
        GL11.glVertex3f((float)(this.posX + var5), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)(var3 / 128.0f), (float)((var4 + 7.99f) / 128.0f));
        GL11.glVertex3f((float)(this.posX - var5), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glTexCoord2f((float)((var3 + var6 - 1.0f) / 128.0f), (float)(var4 / 128.0f));
        GL11.glVertex3f((float)(this.posX + var6 - 1.0f + var5), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)((var3 + var6 - 1.0f) / 128.0f), (float)((var4 + 7.99f) / 128.0f));
        GL11.glVertex3f((float)(this.posX + var6 - 1.0f - var5), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glEnd();
        return this.charWidth[p_78266_1_];
    }

    private ResourceLocation getUnicodePageLocation(int p_111271_1_) {
        if (unicodePageLocations[p_111271_1_] == null) {
            FontRenderer.unicodePageLocations[p_111271_1_] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", p_111271_1_));
            FontRenderer.unicodePageLocations[p_111271_1_] = FontUtils.getHdFontLocation(unicodePageLocations[p_111271_1_]);
        }
        return unicodePageLocations[p_111271_1_];
    }

    private void loadGlyphTexture(int p_78257_1_) {
        this.bindTexture(this.getUnicodePageLocation(p_78257_1_));
    }

    private float renderUnicodeChar(char p_78277_1_, boolean p_78277_2_) {
        if (this.glyphWidth[p_78277_1_] == 0) {
            return 0.0f;
        }
        int var3 = p_78277_1_ / 256;
        this.loadGlyphTexture(var3);
        int var4 = this.glyphWidth[p_78277_1_] >>> 4;
        int var5 = this.glyphWidth[p_78277_1_] & 0xF;
        float var6 = var4 &= 0xF;
        float var7 = var5 + 1;
        float var8 = (float)(p_78277_1_ % 16 * 16) + var6;
        float var9 = (p_78277_1_ & 0xFF) / 16 * 16;
        float var10 = var7 - var6 - 0.02f;
        float var11 = p_78277_2_ ? 1.0f : 0.0f;
        GL11.glBegin((int)5);
        GL11.glTexCoord2f((float)(var8 / 256.0f), (float)(var9 / 256.0f));
        GL11.glVertex3f((float)(this.posX + var11), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)(var8 / 256.0f), (float)((var9 + 15.98f) / 256.0f));
        GL11.glVertex3f((float)(this.posX - var11), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glTexCoord2f((float)((var8 + var10) / 256.0f), (float)(var9 / 256.0f));
        GL11.glVertex3f((float)(this.posX + var10 / 2.0f + var11), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)((var8 + var10) / 256.0f), (float)((var9 + 15.98f) / 256.0f));
        GL11.glVertex3f((float)(this.posX + var10 / 2.0f - var11), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glEnd();
        return (var7 - var6) / 2.0f + 1.0f;
    }

    public int drawStringWithShadow(String text, float d, float g, int color) {
        return this.func_175065_a(text, d, g, color, true);
    }

    public int drawString(String text, float d, float g, int color, boolean shadow) {
        return this.func_175065_a(text, d, g, color, shadow);
    }

    public int drawStringNormal(String text, float d, float g, int color) {
        return this.func_175065_a(text, d, g, color, true);
    }

    public int func_175065_a(String text, float p_175065_2_, float p_175065_3_, int p_175065_4_, boolean p_175065_5_) {
        int var6;
        String p_175065_1_ = text;
        if (ModuleManager.nameProtect.isEnabled() && Minecraft.getMinecraft().thePlayer != null) {
            p_175065_1_ = text.replace(Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText(), "\u00a7aUser");
            if (FriendManager.friends != null) {
                for (Friend f : FriendManager.friends) {
                    p_175065_1_ = p_175065_1_.replace(f.getName(), "\u00a79Friend");
                }
            }
        }
        if (Minecraft.getMinecraft().thePlayer != null && FriendManager.friends != null) {
            for (Friend f : FriendManager.friends) {
                text.contains(f.getName());
                p_175065_1_ = p_175065_1_.replace(f.getName(), "\u00a79" + f.getNickname());
            }
        }
        this.enableAlpha();
        this.resetStyles();
        if (p_175065_5_) {
            var6 = this.func_180455_b(p_175065_1_, p_175065_2_ + 1.0f, p_175065_3_ + 1.0f, p_175065_4_, true);
            var6 = Math.max(var6, this.func_180455_b(p_175065_1_, p_175065_2_, p_175065_3_, p_175065_4_, false));
        } else {
            var6 = this.func_180455_b(p_175065_1_, p_175065_2_, p_175065_3_, p_175065_4_, false);
        }
        return var6;
    }

    private String bidiReorder(String p_147647_1_) {
        try {
            Bidi var3 = new Bidi(new ArabicShaping(8).shape(p_147647_1_), 127);
            var3.setReorderingMode(0);
            return var3.writeReordered(2);
        }
        catch (ArabicShapingException var31) {
            return p_147647_1_;
        }
    }

    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private void renderStringAtPos(String p_78255_1_, boolean p_78255_2_) {
        for (int var3 = 0; var3 < p_78255_1_.length(); ++var3) {
            WorldRenderer var10;
            Tessellator var9;
            boolean var7;
            int var6;
            int var5;
            char var4 = p_78255_1_.charAt(var3);
            if (var4 == '\u00a7' && var3 + 1 < p_78255_1_.length()) {
                var5 = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase().charAt(var3 + 1));
                if (var5 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (var5 < 0 || var5 > 15) {
                        var5 = 15;
                    }
                    if (p_78255_2_) {
                        var5 += 16;
                    }
                    var6 = this.colorCode[var5];
                    if (Config.isCustomColors()) {
                        var6 = CustomColors.getTextColor(var5, var6);
                    }
                    this.textColor = var6;
                    this.setColor((float)(var6 >> 16) / 255.0f, (float)(var6 >> 8 & 0xFF) / 255.0f, (float)(var6 & 0xFF) / 255.0f, this.alpha);
                } else if (var5 == 16) {
                    this.randomStyle = true;
                } else if (var5 == 17) {
                    this.boldStyle = true;
                } else if (var5 == 18) {
                    this.strikethroughStyle = true;
                } else if (var5 == 19) {
                    this.underlineStyle = true;
                } else if (var5 == 20) {
                    this.italicStyle = true;
                } else if (var5 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }
                ++var3;
                continue;
            }
            var5 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(var4);
            if (this.randomStyle && var5 != -1) {
                while ((int)this.charWidth[var5] != (int)this.charWidth[var6 = this.fontRandom.nextInt(this.charWidth.length)]) {
                }
                var5 = var6;
            }
            float var12 = var5 != -1 && !this.unicodeFlag ? this.offsetBold : 0.5f;
            boolean bl = var7 = (var4 == '\u0000' || var5 == -1 || this.unicodeFlag) && p_78255_2_;
            if (var7) {
                this.posX -= var12;
                this.posY -= var12;
            }
            float var8 = this.renderCharAtPos(var5, var4, this.italicStyle);
            if (var7) {
                this.posX += var12;
                this.posY += var12;
            }
            if (this.boldStyle) {
                this.posX += var12;
                if (var7) {
                    this.posX -= var12;
                    this.posY -= var12;
                }
                this.renderCharAtPos(var5, var4, this.italicStyle);
                this.posX -= var12;
                if (var7) {
                    this.posX += var12;
                    this.posY += var12;
                }
                var8 += var12;
            }
            if (this.strikethroughStyle) {
                var9 = Tessellator.getInstance();
                var10 = var9.getWorldRenderer();
                GlStateManager.disableTexture2D();
                var10.startDrawingQuads();
                var10.addVertex(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0);
                var10.addVertex(this.posX + var8, this.posY + (float)(this.FONT_HEIGHT / 2), 0.0);
                var10.addVertex(this.posX + var8, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f, 0.0);
                var10.addVertex(this.posX, this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f, 0.0);
                var9.draw();
                GlStateManager.enableTexture2D();
            }
            if (this.underlineStyle) {
                var9 = Tessellator.getInstance();
                var10 = var9.getWorldRenderer();
                GlStateManager.disableTexture2D();
                var10.startDrawingQuads();
                int var11 = this.underlineStyle ? -1 : 0;
                var10.addVertex(this.posX + (float)var11, this.posY + (float)this.FONT_HEIGHT, 0.0);
                var10.addVertex(this.posX + var8, this.posY + (float)this.FONT_HEIGHT, 0.0);
                var10.addVertex(this.posX + var8, this.posY + (float)this.FONT_HEIGHT - 1.0f, 0.0);
                var10.addVertex(this.posX + (float)var11, this.posY + (float)this.FONT_HEIGHT - 1.0f, 0.0);
                var9.draw();
                GlStateManager.enableTexture2D();
            }
            this.posX += var8;
        }
    }

    private int renderStringAligned(String p_78274_1_, int p_78274_2_, int p_78274_3_, int p_78274_4_, int p_78274_5_, boolean p_78274_6_) {
        if (this.bidiFlag) {
            int var7 = this.getStringWidth(this.bidiReorder(p_78274_1_));
            p_78274_2_ = p_78274_2_ + p_78274_4_ - var7;
        }
        return this.func_180455_b(p_78274_1_, p_78274_2_, p_78274_3_, p_78274_5_, p_78274_6_);
    }

    private int func_180455_b(String p_180455_1_, float p_180455_2_, float p_180455_3_, int p_180455_4_, boolean p_180455_5_) {
        if (p_180455_1_ == null) {
            return 0;
        }
        if (this.bidiFlag) {
            p_180455_1_ = this.bidiReorder(p_180455_1_);
        }
        if ((p_180455_4_ & 0xFC000000) == 0) {
            p_180455_4_ |= 0xFF000000;
        }
        if (p_180455_5_) {
            p_180455_4_ = (p_180455_4_ & 0xFCFCFC) >> 2 | p_180455_4_ & 0xFF000000;
        }
        this.red = (float)(p_180455_4_ >> 16 & 0xFF) / 255.0f;
        this.blue = (float)(p_180455_4_ >> 8 & 0xFF) / 255.0f;
        this.green = (float)(p_180455_4_ & 0xFF) / 255.0f;
        this.alpha = (float)(p_180455_4_ >> 24 & 0xFF) / 255.0f;
        this.setColor(this.red, this.blue, this.green, this.alpha);
        this.posX = p_180455_2_;
        this.posY = p_180455_3_;
        this.renderStringAtPos(p_180455_1_, p_180455_5_);
        return (int)this.posX;
    }

    public int getStringWidth(String textIn) {
        String text = textIn;
        for (Friend f : FriendManager.friends) {
            text = text.replace(f.getName(), (Object)((Object)EnumChatFormatting.BLUE) + f.getNickname());
        }
        if (text == null) {
            return 0;
        }
        float var2 = 0.0f;
        boolean var3 = false;
        for (int var4 = 0; var4 < text.length(); ++var4) {
            char var5 = text.charAt(var4);
            float var6 = this.getCharWidthFloat(var5);
            if (var6 < 0.0f && var4 < text.length() - 1) {
                if ((var5 = text.charAt(++var4)) != 'l' && var5 != 'L') {
                    if (var5 == 'r' || var5 == 'R') {
                        var3 = false;
                    }
                } else {
                    var3 = true;
                }
                var6 = 0.0f;
            }
            var2 += var6;
            if (!var3 || !(var6 > 0.0f)) continue;
            var2 += this.unicodeFlag ? 1.0f : this.offsetBold;
        }
        return (int)var2;
    }

    public int getCharWidth(char par1) {
        return Math.round(this.getCharWidthFloat(par1));
    }

    private float getCharWidthFloat(char p_78263_1_) {
        if (p_78263_1_ == '\u00a7') {
            return -1.0f;
        }
        if (p_78263_1_ == ' ') {
            return this.charWidth[32];
        }
        int var2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(p_78263_1_);
        if (p_78263_1_ > '\u0000' && var2 != -1 && !this.unicodeFlag) {
            return this.charWidth[var2];
        }
        if (this.glyphWidth[p_78263_1_] != 0) {
            int var3 = this.glyphWidth[p_78263_1_] >>> 4;
            int var4 = this.glyphWidth[p_78263_1_] & 0xF;
            return (++var4 - (var3 &= 0xF)) / 2 + 1;
        }
        return 0.0f;
    }

    public String trimStringToWidth(String p_78269_1_, int p_78269_2_) {
        return this.trimStringToWidth(p_78269_1_, p_78269_2_, false);
    }

    public String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_) {
        StringBuilder var4 = new StringBuilder();
        float var5 = 0.0f;
        int var6 = p_78262_3_ ? p_78262_1_.length() - 1 : 0;
        int var7 = p_78262_3_ ? -1 : 1;
        boolean var8 = false;
        boolean var9 = false;
        for (int var10 = var6; var10 >= 0 && var10 < p_78262_1_.length() && var5 < (float)p_78262_2_; var10 += var7) {
            char var11 = p_78262_1_.charAt(var10);
            float var12 = this.getCharWidthFloat(var11);
            if (var8) {
                var8 = false;
                if (var11 != 'l' && var11 != 'L') {
                    if (var11 == 'r' || var11 == 'R') {
                        var9 = false;
                    }
                } else {
                    var9 = true;
                }
            } else if (var12 < 0.0f) {
                var8 = true;
            } else {
                var5 += var12;
                if (var9) {
                    var5 += 1.0f;
                }
            }
            if (var5 > (float)p_78262_2_) break;
            if (p_78262_3_) {
                var4.insert(0, var11);
                continue;
            }
            var4.append(var11);
        }
        return var4.toString();
    }

    private String trimStringNewline(String p_78273_1_) {
        while (p_78273_1_ != null && p_78273_1_.endsWith("\n")) {
            p_78273_1_ = p_78273_1_.substring(0, p_78273_1_.length() - 1);
        }
        return p_78273_1_;
    }

    public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
        this.resetStyles();
        this.textColor = textColor;
        str = this.trimStringNewline(str);
        this.renderSplitString(str, x, y, wrapWidth, false);
    }

    private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
        List var6 = this.listFormattedStringToWidth(str, wrapWidth);
        for (String var8 : var6) {
            this.renderStringAligned(var8, x, y, wrapWidth, this.textColor, addShadow);
            y += this.FONT_HEIGHT;
        }
    }

    public int splitStringWidth(String p_78267_1_, int p_78267_2_) {
        return this.FONT_HEIGHT * this.listFormattedStringToWidth(p_78267_1_, p_78267_2_).size();
    }

    public void setUnicodeFlag(boolean p_78264_1_) {
        this.unicodeFlag = p_78264_1_;
    }

    public boolean getUnicodeFlag() {
        return this.unicodeFlag;
    }

    public void setBidiFlag(boolean p_78275_1_) {
        this.bidiFlag = p_78275_1_;
    }

    public List listFormattedStringToWidth(String str, int wrapWidth) {
        return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }

    String wrapFormattedStringToWidth(String str, int wrapWidth) {
        int var3 = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= var3) {
            return str;
        }
        String var4 = str.substring(0, var3);
        char var5 = str.charAt(var3);
        boolean var6 = var5 == ' ' || var5 == '\n';
        String var7 = String.valueOf(FontRenderer.getFormatFromString(var4)) + str.substring(var3 + (var6 ? 1 : 0));
        return String.valueOf(var4) + "\n" + this.wrapFormattedStringToWidth(var7, wrapWidth);
    }

    private int sizeStringToWidth(String str, int wrapWidth) {
        int var5;
        int var3 = str.length();
        float var4 = 0.0f;
        int var6 = -1;
        boolean var7 = false;
        for (var5 = 0; var5 < var3; ++var5) {
            char var8 = str.charAt(var5);
            switch (var8) {
                case '\n': {
                    --var5;
                    break;
                }
                case '\u00a7': {
                    char var9;
                    if (var5 >= var3 - 1) break;
                    if ((var9 = str.charAt(++var5)) != 'l' && var9 != 'L') {
                        if (var9 != 'r' && var9 != 'R' && !FontRenderer.isFormatColor(var9)) break;
                        var7 = false;
                        break;
                    }
                    var7 = true;
                    break;
                }
                case ' ': {
                    var6 = var5;
                }
                default: {
                    var4 += this.getCharWidthFloat(var8);
                    if (!var7) break;
                    var4 += 1.0f;
                }
            }
            if (var8 == '\n') {
                var6 = ++var5;
                break;
            }
            if (var4 > (float)wrapWidth) break;
        }
        return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
    }

    private static boolean isFormatColor(char colorChar) {
        return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
    }

    private static boolean isFormatSpecial(char formatChar) {
        return formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R';
    }

    public static String getFormatFromString(String p_78282_0_) {
        String var1 = "";
        int var2 = -1;
        int var3 = p_78282_0_.length();
        while ((var2 = p_78282_0_.indexOf(167, var2 + 1)) != -1) {
            if (var2 >= var3 - 1) continue;
            char var4 = p_78282_0_.charAt(var2 + 1);
            if (FontRenderer.isFormatColor(var4)) {
                var1 = "\u00a7" + var4;
                continue;
            }
            if (!FontRenderer.isFormatSpecial(var4)) continue;
            var1 = String.valueOf(var1) + "\u00a7" + var4;
        }
        return var1;
    }

    public boolean getBidiFlag() {
        return this.bidiFlag;
    }

    public int func_175064_b(char p_175064_1_) {
        int index = "0123456789abcdef".indexOf(p_175064_1_);
        if (index >= 0 && index < this.colorCode.length) {
            int color = this.colorCode[index];
            if (Config.isCustomColors()) {
                color = CustomColors.getTextColor(index, color);
            }
            return color;
        }
        return 0xFFFFFF;
    }

    protected void setColor(float r, float g, float b, float a) {
        GlStateManager.color(r, g, b, a);
    }

    protected void enableAlpha() {
        GlStateManager.enableAlpha();
    }

    protected void bindTexture(ResourceLocation location) {
        this.renderEngine.bindTexture(location);
    }

    protected InputStream getResourceInputStream(ResourceLocation location) throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
    }
}

