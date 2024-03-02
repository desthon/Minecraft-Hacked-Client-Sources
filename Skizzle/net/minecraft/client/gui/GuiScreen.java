/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  tv.twitch.chat.ChatUserInfo
 */
package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityList;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import skizzle.ui.elements.Button;
import tv.twitch.chat.ChatUserInfo;

public abstract class GuiScreen
extends Gui
implements GuiYesNoCallback {
    private static final Logger field_175287_a = LogManager.getLogger();
    private static final Set field_175284_f = Sets.newHashSet((Object[])new String[]{"http", "https"});
    private static final Splitter field_175285_g = Splitter.on((char)'\n');
    protected Minecraft mc;
    protected RenderItem itemRender;
    public int width;
    public int height;
    protected List buttonList = Lists.newArrayList();
    public List<Button> sButtonList = new ArrayList<Button>();
    protected List labelList = Lists.newArrayList();
    public boolean allowUserInput;
    protected FontRenderer fontRendererObj;
    private GuiButton selectedButton;
    public int eventButton;
    private long lastMouseEvent;
    private int touchValue;
    private URI field_175286_t;
    private static final String __OBFID = "CL_00000710";

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int var4;
        for (var4 = 0; var4 < this.buttonList.size(); ++var4) {
            ((GuiButton)this.buttonList.get(var4)).drawButton(this.mc, mouseX, mouseY);
        }
        for (var4 = 0; var4 < this.labelList.size(); ++var4) {
            ((GuiLabel)this.labelList.get(var4)).drawLabel(this.mc, mouseX, mouseY);
        }
        for (Button button : this.sButtonList) {
            button.draw(mouseX, mouseY);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    public static String getClipboardString() {
        try {
            Transferable var0 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (var0 != null && var0.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String)var0.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (Exception exception) {}
        return "";
    }

    public static void setClipboardString(String copyText) {
        if (!StringUtils.isEmpty((CharSequence)copyText)) {
            try {
                StringSelection var1 = new StringSelection(copyText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(var1, null);
            }
            catch (Exception exception) {}
        }
    }

    protected void renderToolTip(ItemStack itemIn, int x, int y) {
        List var4 = itemIn.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
        for (int var5 = 0; var5 < var4.size(); ++var5) {
            if (var5 == 0) {
                var4.set(var5, (Object)((Object)itemIn.getRarity().rarityColor) + (String)var4.get(var5));
                continue;
            }
            var4.set(var5, (Object)((Object)EnumChatFormatting.GRAY) + (String)var4.get(var5));
        }
        this.drawHoveringText(var4, x, y);
    }

    protected void drawCreativeTabHoveringText(String tabName, int mouseX, int mouseY) {
        this.drawHoveringText(Arrays.asList(tabName), mouseX, mouseY);
    }

    protected void drawHoveringText(List textLines, int x, int y) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int var4 = 0;
            for (String var6 : textLines) {
                int var7 = this.fontRendererObj.getStringWidth(var6);
                if (var7 <= var4) continue;
                var4 = var7;
            }
            int var14 = x + 12;
            int var15 = y - 12;
            int var8 = 8;
            if (textLines.size() > 1) {
                var8 += 2 + (textLines.size() - 1) * 10;
            }
            if (var14 + var4 > this.width) {
                var14 -= 28 + var4;
            }
            if (var15 + var8 + 6 > this.height) {
                var15 = this.height - var8 - 6;
            }
            this.zLevel = 300.0f;
            this.itemRender.zLevel = 300.0f;
            int var9 = -267386864;
            this.drawGradientRect(var14 - 3, var15 - 4, var14 + var4 + 3, var15 - 3, var9, var9);
            this.drawGradientRect(var14 - 3, var15 + var8 + 3, var14 + var4 + 3, var15 + var8 + 4, var9, var9);
            this.drawGradientRect(var14 - 3, var15 - 3, var14 + var4 + 3, var15 + var8 + 3, var9, var9);
            this.drawGradientRect(var14 - 4, var15 - 3, var14 - 3, var15 + var8 + 3, var9, var9);
            this.drawGradientRect(var14 + var4 + 3, var15 - 3, var14 + var4 + 4, var15 + var8 + 3, var9, var9);
            int var10 = 0x505000FF;
            int var11 = (var10 & 0xFEFEFE) >> 1 | var10 & 0xFF000000;
            this.drawGradientRect(var14 - 3, var15 - 3 + 1, var14 - 3 + 1, var15 + var8 + 3 - 1, var10, var11);
            this.drawGradientRect(var14 + var4 + 2, var15 - 3 + 1, var14 + var4 + 3, var15 + var8 + 3 - 1, var10, var11);
            this.drawGradientRect(var14 - 3, var15 - 3, var14 + var4 + 3, var15 - 3 + 1, var10, var10);
            this.drawGradientRect(var14 - 3, var15 + var8 + 2, var14 + var4 + 3, var15 + var8 + 3, var11, var11);
            for (int var12 = 0; var12 < textLines.size(); ++var12) {
                String var13 = (String)textLines.get(var12);
                this.fontRendererObj.drawStringWithShadow(var13, var14, var15, -1);
                if (var12 == 0) {
                    var15 += 2;
                }
                var15 += 10;
            }
            this.zLevel = 0.0f;
            this.itemRender.zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    protected void func_175272_a(IChatComponent p_175272_1_, int p_175272_2_, int p_175272_3_) {
        if (p_175272_1_ != null && p_175272_1_.getChatStyle().getChatHoverEvent() != null) {
            block22: {
                HoverEvent var4 = p_175272_1_.getChatStyle().getChatHoverEvent();
                if (var4.getAction() == HoverEvent.Action.SHOW_ITEM) {
                    ItemStack var5 = null;
                    try {
                        NBTTagCompound var6 = JsonToNBT.func_180713_a(var4.getValue().getUnformattedText());
                        if (var6 instanceof NBTTagCompound) {
                            var5 = ItemStack.loadItemStackFromNBT(var6);
                        }
                    }
                    catch (NBTException nBTException) {}
                    if (var5 != null) {
                        this.renderToolTip(var5, p_175272_2_, p_175272_3_);
                    } else {
                        this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Item!", p_175272_2_, p_175272_3_);
                    }
                } else if (var4.getAction() == HoverEvent.Action.SHOW_ENTITY) {
                    if (this.mc.gameSettings.advancedItemTooltips) {
                        try {
                            NBTTagCompound var12 = JsonToNBT.func_180713_a(var4.getValue().getUnformattedText());
                            if (var12 instanceof NBTTagCompound) {
                                ArrayList var14 = Lists.newArrayList();
                                NBTTagCompound var7 = var12;
                                var14.add(var7.getString("name"));
                                if (var7.hasKey("type", 8)) {
                                    String var8 = var7.getString("type");
                                    var14.add("Type: " + var8 + " (" + EntityList.func_180122_a(var8) + ")");
                                }
                                var14.add(var7.getString("id"));
                                this.drawHoveringText(var14, p_175272_2_, p_175272_3_);
                                break block22;
                            }
                            this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Entity!", p_175272_2_, p_175272_3_);
                        }
                        catch (NBTException nBTException) {
                            this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Entity!", p_175272_2_, p_175272_3_);
                        }
                    }
                } else if (var4.getAction() == HoverEvent.Action.SHOW_TEXT) {
                    this.drawHoveringText(field_175285_g.splitToList((CharSequence)var4.getValue().getFormattedText()), p_175272_2_, p_175272_3_);
                } else if (var4.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
                    StatBase var13 = StatList.getOneShotStat(var4.getValue().getUnformattedText());
                    if (var13 != null) {
                        IChatComponent var15 = var13.getStatName();
                        ChatComponentTranslation var16 = new ChatComponentTranslation("stats.tooltip.type." + (var13.isAchievement() ? "achievement" : "statistic"), new Object[0]);
                        var16.getChatStyle().setItalic(true);
                        String var8 = var13 instanceof Achievement ? ((Achievement)var13).getDescription() : null;
                        ArrayList var9 = Lists.newArrayList((Object[])new String[]{var15.getFormattedText(), var16.getFormattedText()});
                        if (var8 != null) {
                            var9.addAll(this.fontRendererObj.listFormattedStringToWidth(var8, 150));
                        }
                        this.drawHoveringText(var9, p_175272_2_, p_175272_3_);
                    } else {
                        this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid statistic/achievement!", p_175272_2_, p_175272_3_);
                    }
                }
            }
            GlStateManager.disableLighting();
        }
    }

    protected void func_175274_a(String p_175274_1_, boolean p_175274_2_) {
    }

    protected boolean func_175276_a(IChatComponent p_175276_1_) {
        if (p_175276_1_ == null) {
            return false;
        }
        ClickEvent var2 = p_175276_1_.getChatStyle().getChatClickEvent();
        if (GuiScreen.isShiftKeyDown()) {
            if (p_175276_1_.getChatStyle().getInsertion() != null) {
                this.func_175274_a(p_175276_1_.getChatStyle().getInsertion(), false);
            }
        } else if (var2 != null) {
            block22: {
                if (var2.getAction() == ClickEvent.Action.OPEN_URL) {
                    if (!this.mc.gameSettings.chatLinks) {
                        return false;
                    }
                    try {
                        URI var3 = new URI(var2.getValue());
                        if (!field_175284_f.contains(var3.getScheme().toLowerCase())) {
                            throw new URISyntaxException(var2.getValue(), "Unsupported protocol: " + var3.getScheme().toLowerCase());
                        }
                        if (this.mc.gameSettings.chatLinksPrompt) {
                            this.field_175286_t = var3;
                            this.mc.displayGuiScreen(new GuiConfirmOpenLink((GuiYesNoCallback)this, var2.getValue(), 31102009, false));
                            break block22;
                        }
                        this.openWebLink(var3);
                    }
                    catch (URISyntaxException var4) {
                        field_175287_a.error("Can't open url for " + var2, (Throwable)var4);
                    }
                } else if (var2.getAction() == ClickEvent.Action.OPEN_FILE) {
                    URI var3 = new File(var2.getValue()).toURI();
                    this.openWebLink(var3);
                } else if (var2.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                    this.func_175274_a(var2.getValue(), true);
                } else if (var2.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    this.func_175281_b(var2.getValue(), false);
                } else if (var2.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
                    ChatUserInfo var5 = this.mc.getTwitchStream().func_152926_a(var2.getValue());
                    if (var5 != null) {
                        this.mc.displayGuiScreen(new GuiTwitchUserMode(this.mc.getTwitchStream(), var5));
                    } else {
                        field_175287_a.error("Tried to handle twitch user but couldn't find them!");
                    }
                } else {
                    field_175287_a.error("Don't know how to handle " + var2);
                }
            }
            return true;
        }
        return false;
    }

    public void func_175275_f(String p_175275_1_) {
        this.func_175281_b(p_175275_1_, true);
    }

    public void func_175281_b(String p_175281_1_, boolean p_175281_2_) {
        if (p_175281_2_) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(p_175281_1_);
        }
        this.mc.thePlayer.sendChatMessage(p_175281_1_);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (int var4 = 0; var4 < this.buttonList.size(); ++var4) {
                GuiButton var5 = (GuiButton)this.buttonList.get(var4);
                if (!var5.mousePressed(this.mc, mouseX, mouseY)) continue;
                this.selectedButton = var5;
                var5.playPressSound(this.mc.getSoundHandler());
                this.actionPerformed(var5);
            }
            for (Button button : this.sButtonList) {
                if (!button.enabled || !button.isHovering(mouseX, mouseY)) continue;
                this.sActionPerformed(button);
                button.click();
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.selectedButton != null && state == 0) {
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    }

    protected void actionPerformed(GuiButton button) throws IOException {
    }

    protected void sActionPerformed(GuiButton button) throws IOException {
    }

    public void sActionPerformed(Button button) throws IOException {
    }

    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        this.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRendererObj = mc.fontRendererObj;
        this.width = width;
        this.height = height;
        this.buttonList.clear();
        this.sButtonList.clear();
        this.initGui();
    }

    public void initGui() {
    }

    public void handleInput() throws IOException {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                this.handleMouseInput();
            }
        }
        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                this.handleKeyboardInput();
            }
        }
    }

    public void handleMouseInput() throws IOException {
        int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int var3 = Mouse.getEventButton();
        if (Mouse.getEventButtonState()) {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }
            this.eventButton = var3;
            this.lastMouseEvent = Minecraft.getSystemTime();
            this.mouseClicked(var1, var2, this.eventButton);
        } else if (var3 != -1) {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }
            this.eventButton = -1;
            this.mouseReleased(var1, var2, var3);
        } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            long var4 = Minecraft.getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(var1, var2, this.eventButton, var4);
        }
    }

    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKeyState()) {
            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }
        this.mc.dispatchKeypresses();
    }

    public void updateScreen() {
    }

    public void onGuiClosed() {
    }

    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }

    public void drawWorldBackground(int tint) {
        if (Minecraft.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.drawBackground(tint);
        }
    }

    public void drawBackground(int tint) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator var2 = Tessellator.getInstance();
        WorldRenderer var3 = var2.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(optionsBackground);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float var4 = 32.0f;
        var3.startDrawingQuads();
        var3.func_178991_c(0x404040);
        var3.addVertexWithUV(0.0, this.height, 0.0, 0.0, (float)this.height / var4 + (float)tint);
        var3.addVertexWithUV(this.width, this.height, 0.0, (float)this.width / var4, (float)this.height / var4 + (float)tint);
        var3.addVertexWithUV(this.width, 0.0, 0.0, (float)this.width / var4, tint);
        var3.addVertexWithUV(0.0, 0.0, 0.0, 0.0, tint);
        var2.draw();
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (id == 31102009) {
            if (result) {
                this.openWebLink(this.field_175286_t);
            }
            this.field_175286_t = null;
            this.mc.displayGuiScreen(this);
        }
    }

    public void openWebLink(URI p_175282_1_) {
        try {
            Class<?> var2 = Class.forName("java.awt.Desktop");
            Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            var2.getMethod("browse", URI.class).invoke(var3, p_175282_1_);
        }
        catch (Throwable var4) {
            field_175287_a.error("Couldn't open link", var4);
        }
    }

    public static boolean isCtrlKeyDown() {
        return Minecraft.isRunningOnMac ? Keyboard.isKeyDown((int)219) || Keyboard.isKeyDown((int)220) : Keyboard.isKeyDown((int)29) || Keyboard.isKeyDown((int)157);
    }

    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown((int)42) || Keyboard.isKeyDown((int)54);
    }

    public static boolean func_175283_s() {
        return Keyboard.isKeyDown((int)56) || Keyboard.isKeyDown((int)184);
    }

    public static boolean func_175277_d(int p_175277_0_) {
        return p_175277_0_ == 45 && GuiScreen.isCtrlKeyDown();
    }

    public static boolean func_175279_e(int p_175279_0_) {
        return p_175279_0_ == 47 && GuiScreen.isCtrlKeyDown();
    }

    public static boolean func_175280_f(int p_175280_0_) {
        return p_175280_0_ == 46 && GuiScreen.isCtrlKeyDown();
    }

    public static boolean func_175278_g(int p_175278_0_) {
        return p_175278_0_ == 30 && GuiScreen.isCtrlKeyDown();
    }

    public void func_175273_b(Minecraft mcIn, int p_175273_2_, int p_175273_3_) {
        this.setWorldAndResolution(mcIn, p_175273_2_, p_175273_3_);
    }
}

