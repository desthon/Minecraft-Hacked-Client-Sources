/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Mouse;

public class GuiClickableScrolledSelectionListProxy
extends GuiSlot {
    private final RealmsClickableScrolledSelectionList field_178046_u;
    private static final String __OBFID = "CL_00001939";

    public GuiClickableScrolledSelectionListProxy(RealmsClickableScrolledSelectionList p_i45526_1_, int p_i45526_2_, int p_i45526_3_, int p_i45526_4_, int p_i45526_5_, int p_i45526_6_) {
        super(Minecraft.getMinecraft(), p_i45526_2_, p_i45526_3_, p_i45526_4_, p_i45526_5_, p_i45526_6_);
        this.field_178046_u = p_i45526_1_;
    }

    @Override
    protected int getSize() {
        return this.field_178046_u.getItemCount();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        this.field_178046_u.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return this.field_178046_u.isSelectedItem(slotIndex);
    }

    @Override
    protected void drawBackground() {
        this.field_178046_u.renderBackground();
    }

    @Override
    protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_) {
        this.field_178046_u.renderItem(p_180791_1_, p_180791_2_, p_180791_3_, p_180791_4_, p_180791_5_, p_180791_6_);
    }

    public int func_178044_e() {
        return this.width;
    }

    public int func_178042_f() {
        return this.mouseY;
    }

    public int func_178045_g() {
        return this.mouseX;
    }

    @Override
    protected int getContentHeight() {
        return this.field_178046_u.getMaxPosition();
    }

    @Override
    protected int getScrollBarX() {
        return this.field_178046_u.getScrollbarPosition();
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (this.scrollMultiplier > 0.0f && Mouse.getEventButtonState()) {
            this.field_178046_u.customMouseEvent(this.top, this.bottom, this.headerPadding, this.amountScrolled, this.slotHeight);
        }
    }

    public void func_178043_a(int p_178043_1_, int p_178043_2_, int p_178043_3_, Tezzelator p_178043_4_) {
        this.field_178046_u.renderSelected(p_178043_1_, p_178043_2_, p_178043_3_, p_178043_4_);
    }

    @Override
    protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int p_148120_3_, int p_148120_4_) {
        int var5 = this.getSize();
        for (int var6 = 0; var6 < var5; ++var6) {
            int var7 = p_148120_2_ + var6 * this.slotHeight + this.headerPadding;
            int var8 = this.slotHeight - 4;
            if (var7 > this.bottom || var7 + var8 < this.top) {
                this.func_178040_a(var6, p_148120_1_, var7);
            }
            if (this.showSelectionBox && this.isSelected(var6)) {
                this.func_178043_a(this.width, var7, var8, Tezzelator.instance);
            }
            this.drawSlot(var6, p_148120_1_, var7, var8, p_148120_3_, p_148120_4_);
        }
    }
}

