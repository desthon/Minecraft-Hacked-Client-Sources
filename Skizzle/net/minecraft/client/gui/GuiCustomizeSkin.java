/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class GuiCustomizeSkin
extends GuiScreen {
    private final GuiScreen field_175361_a;
    private String field_175360_f;
    private static final String __OBFID = "CL_00001932";

    public GuiCustomizeSkin(GuiScreen p_i45516_1_) {
        this.field_175361_a = p_i45516_1_;
    }

    @Override
    public void initGui() {
        int var1 = 0;
        this.field_175360_f = I18n.format("options.skinCustomisation.title", new Object[0]);
        for (EnumPlayerModelParts var5 : EnumPlayerModelParts.values()) {
            this.buttonList.add(new ButtonPart(var5.func_179328_b(), this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 + 24 * (var1 >> 1), 150, 20, var5, null));
            ++var1;
        }
        if (var1 % 2 == 1) {
            ++var1;
        }
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 24 * (var1 >> 1), I18n.format("gui.done", new Object[0])));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.field_175361_a);
            } else if (button instanceof ButtonPart) {
                EnumPlayerModelParts var2 = ((ButtonPart)button).field_175234_p;
                this.mc.gameSettings.func_178877_a(var2);
                button.displayString = this.func_175358_a(var2);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_175360_f, this.width / 2, 20.0f, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private String func_175358_a(EnumPlayerModelParts p_175358_1_) {
        String var2 = this.mc.gameSettings.func_178876_d().contains((Object)p_175358_1_) ? I18n.format("options.on", new Object[0]) : I18n.format("options.off", new Object[0]);
        return String.valueOf(p_175358_1_.func_179326_d().getFormattedText()) + ": " + var2;
    }

    class ButtonPart
    extends GuiButton {
        private final EnumPlayerModelParts field_175234_p;
        private static final String __OBFID = "CL_00001930";

        private ButtonPart(int p_i45514_2_, int p_i45514_3_, int p_i45514_4_, int p_i45514_5_, int p_i45514_6_, EnumPlayerModelParts p_i45514_7_) {
            super(p_i45514_2_, p_i45514_3_, p_i45514_4_, p_i45514_5_, p_i45514_6_, GuiCustomizeSkin.this.func_175358_a(p_i45514_7_));
            this.field_175234_p = p_i45514_7_;
        }

        ButtonPart(int p_i45515_2_, int p_i45515_3_, int p_i45515_4_, int p_i45515_5_, int p_i45515_6_, EnumPlayerModelParts p_i45515_7_, Object p_i45515_8_) {
            this(p_i45515_2_, p_i45515_3_, p_i45515_4_, p_i45515_5_, p_i45515_6_, p_i45515_7_);
        }
    }
}

