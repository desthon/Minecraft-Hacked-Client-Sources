/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package optifine;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import optifine.Config;

public class GuiMessage
extends GuiScreen {
    private GuiScreen parentScreen;
    private String messageLine1;
    private String messageLine2;
    private final List listLines2 = Lists.newArrayList();
    protected String confirmButtonText;
    private int ticksUntilEnable;

    public GuiMessage(GuiScreen parentScreen, String line1, String line2) {
        this.parentScreen = parentScreen;
        this.messageLine1 = line1;
        this.messageLine2 = line2;
        this.confirmButtonText = I18n.format("gui.done", new Object[0]);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 74, this.height / 6 + 96, this.confirmButtonText));
        this.listLines2.clear();
        this.listLines2.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, this.width - 50));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        Config.getMinecraft().displayGuiScreen(this.parentScreen);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.messageLine1, this.width / 2, 70.0f, 0xFFFFFF);
        int var4 = 90;
        for (String var6 : this.listLines2) {
            this.drawCenteredString(this.fontRendererObj, var6, this.width / 2, var4, 0xFFFFFF);
            var4 += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void setButtonDelay(int ticksUntilEnable) {
        this.ticksUntilEnable = ticksUntilEnable;
        for (GuiButton var3 : this.buttonList) {
            var3.enabled = false;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (--this.ticksUntilEnable == 0) {
            for (GuiButton var2 : this.buttonList) {
                var2.enabled = true;
            }
        }
    }
}

