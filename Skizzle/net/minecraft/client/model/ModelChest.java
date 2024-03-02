/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.model;

import java.awt.Color;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import skizzle.modules.ModuleManager;
import skizzle.modules.render.ChestESP;
import skizzle.util.OutlineUtils;

public class ModelChest
extends ModelBase {
    public ModelRenderer chestLid = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
    public ModelRenderer chestBelow;
    public ModelRenderer chestKnob;
    private static final String __OBFID = "CL_00000834";

    public ModelChest() {
        this.chestLid.addBox(0.0f, -5.0f, -14.0f, 14, 5, 14, 0.0f);
        this.chestLid.rotationPointX = 1.0f;
        this.chestLid.rotationPointY = 7.0f;
        this.chestLid.rotationPointZ = 15.0f;
        this.chestKnob = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        this.chestKnob.addBox(-1.0f, -2.0f, -15.0f, 2, 4, 1, 0.0f);
        this.chestKnob.rotationPointX = 8.0f;
        this.chestKnob.rotationPointY = 7.0f;
        this.chestKnob.rotationPointZ = 15.0f;
        this.chestBelow = new ModelRenderer(this, 0, 19).setTextureSize(64, 64);
        this.chestBelow.addBox(0.0f, 0.0f, 0.0f, 14, 10, 14, 0.0f);
        this.chestBelow.rotationPointX = 1.0f;
        this.chestBelow.rotationPointY = 6.0f;
        this.chestBelow.rotationPointZ = 1.0f;
    }

    public void renderAll(BlockPos pos) {
        ChestESP chestESP = (ChestESP)ModuleManager.getModule("ChestESP");
        if (chestESP != null && chestESP.mode.getMode().equals("Outline") && chestESP.isEnabled()) {
            GL11.glBlendFunc((int)770, (int)771);
            this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
            this.chestLid.render(0.0625f);
            this.chestKnob.render(0.0625f);
            this.chestBelow.render(0.0625f);
            OutlineUtils.renderOne();
            this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
            this.chestLid.render(0.0625f);
            this.chestKnob.render(0.0625f);
            this.chestBelow.render(0.0625f);
            OutlineUtils.renderTwo();
            this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
            this.chestLid.render(0.0625f);
            this.chestKnob.render(0.0625f);
            this.chestBelow.render(0.0625f);
            OutlineUtils.renderThree();
            OutlineUtils.renderFour(new Color(0, 255, 255));
            this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
            this.chestLid.render(0.0625f);
            this.chestKnob.render(0.0625f);
            this.chestBelow.render(0.0625f);
            OutlineUtils.renderFive();
        } else {
            this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
            this.chestLid.render(0.0625f);
            this.chestKnob.render(0.0625f);
            this.chestBelow.render(0.0625f);
        }
    }
}
