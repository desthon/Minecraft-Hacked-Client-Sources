/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.gui;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiListButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.IntHashMap;

public class GuiPageButtonList
extends GuiListExtended {
    private final List field_178074_u = Lists.newArrayList();
    private final IntHashMap field_178073_v = new IntHashMap();
    private final List field_178072_w = Lists.newArrayList();
    private final GuiListEntry[][] field_178078_x;
    private int field_178077_y;
    private GuiResponder field_178076_z;
    private Gui field_178075_A;
    private static final String __OBFID = "CL_00001950";

    public GuiPageButtonList(Minecraft mcIn, int p_i45536_2_, int p_i45536_3_, int p_i45536_4_, int p_i45536_5_, int p_i45536_6_, GuiResponder p_i45536_7_, GuiListEntry[] ... p_i45536_8_) {
        super(mcIn, p_i45536_2_, p_i45536_3_, p_i45536_4_, p_i45536_5_, p_i45536_6_);
        this.field_178076_z = p_i45536_7_;
        this.field_178078_x = p_i45536_8_;
        this.field_148163_i = false;
        this.func_178069_s();
        this.func_178055_t();
    }

    private void func_178069_s() {
        for (GuiListEntry[] var4 : this.field_178078_x) {
            for (int var5 = 0; var5 < var4.length; var5 += 2) {
                GuiListEntry var6 = var4[var5];
                GuiListEntry var7 = var5 < var4.length - 1 ? var4[var5 + 1] : null;
                Gui var8 = this.func_178058_a(var6, 0, var7 == null);
                Gui var9 = this.func_178058_a(var7, 160, var6 == null);
                GuiEntry var10 = new GuiEntry(var8, var9);
                this.field_178074_u.add(var10);
                if (var6 != null && var8 != null) {
                    this.field_178073_v.addKey(var6.func_178935_b(), var8);
                    if (var8 instanceof GuiTextField) {
                        this.field_178072_w.add((GuiTextField)var8);
                    }
                }
                if (var7 == null || var9 == null) continue;
                this.field_178073_v.addKey(var7.func_178935_b(), var9);
                if (!(var9 instanceof GuiTextField)) continue;
                this.field_178072_w.add((GuiTextField)var9);
            }
        }
    }

    private void func_178055_t() {
        this.field_178074_u.clear();
        for (int var1 = 0; var1 < this.field_178078_x[this.field_178077_y].length; var1 += 2) {
            GuiListEntry var2 = this.field_178078_x[this.field_178077_y][var1];
            GuiListEntry var3 = var1 < this.field_178078_x[this.field_178077_y].length - 1 ? this.field_178078_x[this.field_178077_y][var1 + 1] : null;
            Gui var4 = (Gui)this.field_178073_v.lookup(var2.func_178935_b());
            Gui var5 = var3 != null ? (Gui)this.field_178073_v.lookup(var3.func_178935_b()) : null;
            GuiEntry var6 = new GuiEntry(var4, var5);
            this.field_178074_u.add(var6);
        }
    }

    public int func_178059_e() {
        return this.field_178077_y;
    }

    public int func_178057_f() {
        return this.field_178078_x.length;
    }

    public Gui func_178056_g() {
        return this.field_178075_A;
    }

    public void func_178071_h() {
        if (this.field_178077_y > 0) {
            int var1 = this.field_178077_y--;
            this.func_178055_t();
            this.func_178060_e(var1, this.field_178077_y);
            this.amountScrolled = 0.0f;
        }
    }

    public void func_178064_i() {
        if (this.field_178077_y < this.field_178078_x.length - 1) {
            int var1 = this.field_178077_y++;
            this.func_178055_t();
            this.func_178060_e(var1, this.field_178077_y);
            this.amountScrolled = 0.0f;
        }
    }

    public Gui func_178061_c(int p_178061_1_) {
        return (Gui)this.field_178073_v.lookup(p_178061_1_);
    }

    private void func_178060_e(int p_178060_1_, int p_178060_2_) {
        for (GuiListEntry var6 : this.field_178078_x[p_178060_1_]) {
            if (var6 == null) continue;
            this.func_178066_a((Gui)this.field_178073_v.lookup(var6.func_178935_b()), false);
        }
        for (GuiListEntry var6 : this.field_178078_x[p_178060_2_]) {
            if (var6 == null) continue;
            this.func_178066_a((Gui)this.field_178073_v.lookup(var6.func_178935_b()), true);
        }
    }

    private void func_178066_a(Gui p_178066_1_, boolean p_178066_2_) {
        if (p_178066_1_ instanceof GuiButton) {
            ((GuiButton)p_178066_1_).visible = p_178066_2_;
        } else if (p_178066_1_ instanceof GuiTextField) {
            ((GuiTextField)p_178066_1_).setVisible(p_178066_2_);
        } else if (p_178066_1_ instanceof GuiLabel) {
            ((GuiLabel)p_178066_1_).visible = p_178066_2_;
        }
    }

    private Gui func_178058_a(GuiListEntry p_178058_1_, int p_178058_2_, boolean p_178058_3_) {
        return p_178058_1_ instanceof GuiSlideEntry ? this.func_178067_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiSlideEntry)p_178058_1_) : (p_178058_1_ instanceof GuiButtonEntry ? this.func_178065_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiButtonEntry)p_178058_1_) : (p_178058_1_ instanceof EditBoxEntry ? this.func_178068_a(this.width / 2 - 155 + p_178058_2_, 0, (EditBoxEntry)p_178058_1_) : (p_178058_1_ instanceof GuiLabelEntry ? this.func_178063_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiLabelEntry)p_178058_1_, p_178058_3_) : null)));
    }

    @Override
    public boolean mouseClicked(int p_148179_1_, int p_148179_2_, int p_148179_3_) {
        boolean var4 = super.mouseClicked(p_148179_1_, p_148179_2_, p_148179_3_);
        int var5 = this.getSlotIndexFromScreenCoords(p_148179_1_, p_148179_2_);
        if (var5 >= 0) {
            GuiEntry var6 = this.func_178070_d(var5);
            if (this.field_178075_A != var6.field_178028_d && this.field_178075_A != null && this.field_178075_A instanceof GuiTextField) {
                ((GuiTextField)this.field_178075_A).setFocused(false);
            }
            this.field_178075_A = var6.field_178028_d;
        }
        return var4;
    }

    private GuiSlider func_178067_a(int p_178067_1_, int p_178067_2_, GuiSlideEntry p_178067_3_) {
        GuiSlider var4 = new GuiSlider(this.field_178076_z, p_178067_3_.func_178935_b(), p_178067_1_, p_178067_2_, p_178067_3_.func_178936_c(), p_178067_3_.func_178943_e(), p_178067_3_.func_178944_f(), p_178067_3_.func_178942_g(), p_178067_3_.func_178945_a());
        var4.visible = p_178067_3_.func_178934_d();
        return var4;
    }

    private GuiListButton func_178065_a(int p_178065_1_, int p_178065_2_, GuiButtonEntry p_178065_3_) {
        GuiListButton var4 = new GuiListButton(this.field_178076_z, p_178065_3_.func_178935_b(), p_178065_1_, p_178065_2_, p_178065_3_.func_178936_c(), p_178065_3_.func_178940_a());
        var4.visible = p_178065_3_.func_178934_d();
        return var4;
    }

    private GuiTextField func_178068_a(int p_178068_1_, int p_178068_2_, EditBoxEntry p_178068_3_) {
        GuiTextField var4 = new GuiTextField(p_178068_3_.func_178935_b(), this.mc.fontRendererObj, p_178068_1_, p_178068_2_, 150, 20);
        var4.setText(p_178068_3_.func_178936_c());
        var4.func_175207_a(this.field_178076_z);
        var4.setVisible(p_178068_3_.func_178934_d());
        var4.func_175205_a(p_178068_3_.func_178950_a());
        return var4;
    }

    private GuiLabel func_178063_a(int p_178063_1_, int p_178063_2_, GuiLabelEntry p_178063_3_, boolean p_178063_4_) {
        GuiLabel var5 = p_178063_4_ ? new GuiLabel(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, this.width - p_178063_1_ * 2, 20, -1) : new GuiLabel(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, 150, 20, -1);
        var5.visible = p_178063_3_.func_178934_d();
        var5.func_175202_a(p_178063_3_.func_178936_c());
        var5.func_175203_a();
        return var5;
    }

    public void func_178062_a(char p_178062_1_, int p_178062_2_) {
        block1: {
            int var6;
            block2: {
                GuiTextField var3;
                block3: {
                    int var62;
                    block4: {
                        if (!(this.field_178075_A instanceof GuiTextField)) break block1;
                        var3 = (GuiTextField)this.field_178075_A;
                        if (GuiScreen.func_175279_e(p_178062_2_)) break block2;
                        if (p_178062_2_ != 15) break block3;
                        var3.setFocused(false);
                        int var12 = this.field_178072_w.indexOf(this.field_178075_A);
                        var12 = GuiScreen.isShiftKeyDown() ? (var12 == 0 ? this.field_178072_w.size() - 1 : --var12) : (var12 == this.field_178072_w.size() - 1 ? 0 : ++var12);
                        this.field_178075_A = (Gui)this.field_178072_w.get(var12);
                        var3 = (GuiTextField)this.field_178075_A;
                        var3.setFocused(true);
                        int var13 = var3.yPosition + this.slotHeight;
                        var62 = var3.yPosition;
                        if (var13 <= this.bottom) break block4;
                        this.amountScrolled += (float)(var13 - this.bottom);
                        break block1;
                    }
                    if (var62 >= this.top) break block1;
                    this.amountScrolled = var62;
                    break block1;
                }
                var3.textboxKeyTyped(p_178062_1_, p_178062_2_);
                break block1;
            }
            String var4 = GuiScreen.getClipboardString();
            String[] var5 = var4.split(";");
            int var7 = var6 = this.field_178072_w.indexOf(this.field_178075_A);
            String[] var8 = var5;
            int var9 = var5.length;
            for (int var10 = 0; var10 < var9; ++var10) {
                String var11 = var8[var10];
                ((GuiTextField)this.field_178072_w.get(var7)).setText(var11);
                var7 = var7 == this.field_178072_w.size() - 1 ? 0 : ++var7;
                if (var7 == var6) break;
            }
        }
    }

    public GuiEntry func_178070_d(int p_178070_1_) {
        return (GuiEntry)this.field_178074_u.get(p_178070_1_);
    }

    @Override
    public int getSize() {
        return this.field_178074_u.size();
    }

    @Override
    public int getListWidth() {
        return 400;
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }

    @Override
    public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_) {
        return this.func_178070_d(p_148180_1_);
    }

    public static class EditBoxEntry
    extends GuiListEntry {
        private final Predicate field_178951_a;
        private static final String __OBFID = "CL_00001948";

        public EditBoxEntry(int p_i45534_1_, String p_i45534_2_, boolean p_i45534_3_, Predicate p_i45534_4_) {
            super(p_i45534_1_, p_i45534_2_, p_i45534_3_);
            this.field_178951_a = (Predicate)Objects.firstNonNull((Object)p_i45534_4_, (Object)Predicates.alwaysTrue());
        }

        public Predicate func_178950_a() {
            return this.field_178951_a;
        }
    }

    public static class GuiButtonEntry
    extends GuiListEntry {
        private final boolean field_178941_a;
        private static final String __OBFID = "CL_00001949";

        public GuiButtonEntry(int p_i45535_1_, String p_i45535_2_, boolean p_i45535_3_, boolean p_i45535_4_) {
            super(p_i45535_1_, p_i45535_2_, p_i45535_3_);
            this.field_178941_a = p_i45535_4_;
        }

        public boolean func_178940_a() {
            return this.field_178941_a;
        }
    }

    public static class GuiEntry
    implements GuiListExtended.IGuiListEntry {
        private final Minecraft field_178031_a = Minecraft.getMinecraft();
        private final Gui field_178029_b;
        private final Gui field_178030_c;
        private Gui field_178028_d;
        private static final String __OBFID = "CL_00001947";

        public GuiEntry(Gui p_i45533_1_, Gui p_i45533_2_) {
            this.field_178029_b = p_i45533_1_;
            this.field_178030_c = p_i45533_2_;
        }

        public Gui func_178022_a() {
            return this.field_178029_b;
        }

        public Gui func_178021_b() {
            return this.field_178030_c;
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            this.func_178017_a(this.field_178029_b, y, mouseX, mouseY, false);
            this.func_178017_a(this.field_178030_c, y, mouseX, mouseY, false);
        }

        private void func_178017_a(Gui p_178017_1_, int p_178017_2_, int p_178017_3_, int p_178017_4_, boolean p_178017_5_) {
            if (p_178017_1_ != null) {
                if (p_178017_1_ instanceof GuiButton) {
                    this.func_178024_a((GuiButton)p_178017_1_, p_178017_2_, p_178017_3_, p_178017_4_, p_178017_5_);
                } else if (p_178017_1_ instanceof GuiTextField) {
                    this.func_178027_a((GuiTextField)p_178017_1_, p_178017_2_, p_178017_5_);
                } else if (p_178017_1_ instanceof GuiLabel) {
                    this.func_178025_a((GuiLabel)p_178017_1_, p_178017_2_, p_178017_3_, p_178017_4_, p_178017_5_);
                }
            }
        }

        private void func_178024_a(GuiButton p_178024_1_, int p_178024_2_, int p_178024_3_, int p_178024_4_, boolean p_178024_5_) {
            p_178024_1_.yPosition = p_178024_2_;
            if (!p_178024_5_) {
                p_178024_1_.drawButton(this.field_178031_a, p_178024_3_, p_178024_4_);
            }
        }

        private void func_178027_a(GuiTextField p_178027_1_, int p_178027_2_, boolean p_178027_3_) {
            p_178027_1_.yPosition = p_178027_2_;
            if (!p_178027_3_) {
                p_178027_1_.drawTextBox();
            }
        }

        private void func_178025_a(GuiLabel p_178025_1_, int p_178025_2_, int p_178025_3_, int p_178025_4_, boolean p_178025_5_) {
            p_178025_1_.field_146174_h = p_178025_2_;
            if (!p_178025_5_) {
                p_178025_1_.drawLabel(this.field_178031_a, p_178025_3_, p_178025_4_);
            }
        }

        @Override
        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
            this.func_178017_a(this.field_178029_b, p_178011_3_, 0, 0, true);
            this.func_178017_a(this.field_178030_c, p_178011_3_, 0, 0, true);
        }

        @Override
        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
            boolean var7 = this.func_178026_a(this.field_178029_b, p_148278_2_, p_148278_3_, p_148278_4_);
            boolean var8 = this.func_178026_a(this.field_178030_c, p_148278_2_, p_148278_3_, p_148278_4_);
            return var7 || var8;
        }

        private boolean func_178026_a(Gui p_178026_1_, int p_178026_2_, int p_178026_3_, int p_178026_4_) {
            if (p_178026_1_ == null) {
                return false;
            }
            if (p_178026_1_ instanceof GuiButton) {
                return this.func_178023_a((GuiButton)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
            }
            if (p_178026_1_ instanceof GuiTextField) {
                this.func_178018_a((GuiTextField)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
            }
            return false;
        }

        private boolean func_178023_a(GuiButton p_178023_1_, int p_178023_2_, int p_178023_3_, int p_178023_4_) {
            boolean var5 = p_178023_1_.mousePressed(this.field_178031_a, p_178023_2_, p_178023_3_);
            if (var5) {
                this.field_178028_d = p_178023_1_;
            }
            return var5;
        }

        private void func_178018_a(GuiTextField p_178018_1_, int p_178018_2_, int p_178018_3_, int p_178018_4_) {
            p_178018_1_.mouseClicked(p_178018_2_, p_178018_3_, p_178018_4_);
            if (p_178018_1_.isFocused()) {
                this.field_178028_d = p_178018_1_;
            }
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            this.func_178016_b(this.field_178029_b, x, y, mouseEvent);
            this.func_178016_b(this.field_178030_c, x, y, mouseEvent);
        }

        private void func_178016_b(Gui p_178016_1_, int p_178016_2_, int p_178016_3_, int p_178016_4_) {
            if (p_178016_1_ != null && p_178016_1_ instanceof GuiButton) {
                this.func_178019_b((GuiButton)p_178016_1_, p_178016_2_, p_178016_3_, p_178016_4_);
            }
        }

        private void func_178019_b(GuiButton p_178019_1_, int p_178019_2_, int p_178019_3_, int p_178019_4_) {
            p_178019_1_.mouseReleased(p_178019_2_, p_178019_3_);
        }
    }

    public static class GuiLabelEntry
    extends GuiListEntry {
        private static final String __OBFID = "CL_00001946";

        public GuiLabelEntry(int p_i45532_1_, String p_i45532_2_, boolean p_i45532_3_) {
            super(p_i45532_1_, p_i45532_2_, p_i45532_3_);
        }
    }

    public static class GuiListEntry {
        private final int field_178939_a;
        private final String field_178937_b;
        private final boolean field_178938_c;
        private static final String __OBFID = "CL_00001945";

        public GuiListEntry(int p_i45531_1_, String p_i45531_2_, boolean p_i45531_3_) {
            this.field_178939_a = p_i45531_1_;
            this.field_178937_b = p_i45531_2_;
            this.field_178938_c = p_i45531_3_;
        }

        public int func_178935_b() {
            return this.field_178939_a;
        }

        public String func_178936_c() {
            return this.field_178937_b;
        }

        public boolean func_178934_d() {
            return this.field_178938_c;
        }
    }

    public static interface GuiResponder {
        public void func_175321_a(int var1, boolean var2);

        public void func_175320_a(int var1, float var2);

        public void func_175319_a(int var1, String var2);
    }

    public static class GuiSlideEntry
    extends GuiListEntry {
        private final GuiSlider.FormatHelper field_178949_a;
        private final float field_178947_b;
        private final float field_178948_c;
        private final float field_178946_d;
        private static final String __OBFID = "CL_00001944";

        public GuiSlideEntry(int p_i45530_1_, String p_i45530_2_, boolean p_i45530_3_, GuiSlider.FormatHelper p_i45530_4_, float p_i45530_5_, float p_i45530_6_, float p_i45530_7_) {
            super(p_i45530_1_, p_i45530_2_, p_i45530_3_);
            this.field_178949_a = p_i45530_4_;
            this.field_178947_b = p_i45530_5_;
            this.field_178948_c = p_i45530_6_;
            this.field_178946_d = p_i45530_7_;
        }

        public GuiSlider.FormatHelper func_178945_a() {
            return this.field_178949_a;
        }

        public float func_178943_e() {
            return this.field_178947_b;
        }

        public float func_178944_f() {
            return this.field_178948_c;
        }

        public float func_178942_g() {
            return this.field_178946_d;
        }
    }
}

