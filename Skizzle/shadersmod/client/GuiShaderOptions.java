/*
 * Decompiled with CFR 0.150.
 */
package shadersmod.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.Config;
import optifine.Lang;
import optifine.StrUtils;
import shadersmod.client.GuiButtonShaderOption;
import shadersmod.client.ShaderOption;
import shadersmod.client.ShaderOptionProfile;
import shadersmod.client.ShaderOptionScreen;
import shadersmod.client.Shaders;

public class GuiShaderOptions
extends GuiScreen {
    private GuiScreen prevScreen;
    protected String title = "Shader Options";
    private GameSettings settings;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;
    private String screenName = null;
    private String screenText = null;
    private boolean changed = false;
    public static final String OPTION_PROFILE = "<profile>";
    public static final String OPTION_EMPTY = "<empty>";
    public static final String OPTION_REST = "*";

    public GuiShaderOptions(GuiScreen guiscreen, GameSettings gamesettings) {
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }

    public GuiShaderOptions(GuiScreen guiscreen, GameSettings gamesettings, String screenName) {
        this(guiscreen, gamesettings);
        this.screenName = screenName;
        if (screenName != null) {
            this.screenText = Shaders.translate("screen." + screenName, screenName);
        }
    }

    @Override
    public void initGui() {
        this.title = I18n.format("of.options.shaderOptionsTitle", new Object[0]);
        int baseId = 100;
        int baseY = 30;
        int stepY = 20;
        int btnWidth = 120;
        int btnHeight = 20;
        int columns = 2;
        ShaderOption[] ops = Shaders.getShaderPackOptions(this.screenName);
        if (ops != null) {
            if (ops.length > 18) {
                columns = ops.length / 9 + 1;
            }
            for (int i = 0; i < ops.length; ++i) {
                ShaderOption so = ops[i];
                if (so == null || !so.isVisible()) continue;
                int col = i % columns;
                int row = i / columns;
                int colWidth = Math.min(this.width / columns, 200);
                int var21 = (this.width - colWidth * columns) / 2;
                int x = col * colWidth + 5 + var21;
                int y = baseY + row * stepY;
                int w = colWidth - 10;
                String text = this.getButtonText(so, w);
                GuiButtonShaderOption btn = new GuiButtonShaderOption(baseId + i, x, y, w, btnHeight, so, text);
                btn.enabled = so.isEnabled();
                this.buttonList.add(btn);
            }
        }
        this.buttonList.add(new GuiButton(201, this.width / 2 - btnWidth - 20, this.height / 6 + 168 + 11, btnWidth, btnHeight, I18n.format("controls.reset", new Object[0])));
        this.buttonList.add(new GuiButton(200, this.width / 2 + 20, this.height / 6 + 168 + 11, btnWidth, btnHeight, I18n.format("gui.done", new Object[0])));
    }

    private String getButtonText(ShaderOption so, int btnWidth) {
        String labelName = so.getNameText();
        if (so instanceof ShaderOptionScreen) {
            ShaderOptionScreen cfr_ignored_0 = (ShaderOptionScreen)so;
            return String.valueOf(labelName) + "...";
        }
        FontRenderer fr = Config.getMinecraft().fontRendererObj;
        int lenSuffix = fr.getStringWidth(": " + Lang.getOff()) + 5;
        while (fr.getStringWidth(labelName) + lenSuffix >= btnWidth && labelName.length() > 0) {
            labelName = labelName.substring(0, labelName.length() - 1);
        }
        String col = so.isChanged() ? so.getValueColor(so.getValue()) : "";
        String labelValue = so.getValueText(so.getValue());
        return String.valueOf(labelName) + ": " + col + labelValue;
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id < 200 && guibutton instanceof GuiButtonShaderOption) {
                GuiButtonShaderOption opts = (GuiButtonShaderOption)guibutton;
                ShaderOption i = opts.getShaderOption();
                if (i instanceof ShaderOptionScreen) {
                    String var8 = i.getName();
                    GuiShaderOptions scr = new GuiShaderOptions(this, this.settings, var8);
                    this.mc.displayGuiScreen(scr);
                    return;
                }
                i.nextValue();
                this.updateAllButtons();
                this.changed = true;
            }
            if (guibutton.id == 201) {
                ShaderOption[] var6 = Shaders.getChangedOptions(Shaders.getShaderPackOptions());
                for (int var7 = 0; var7 < var6.length; ++var7) {
                    ShaderOption opt = var6[var7];
                    opt.resetValue();
                    this.changed = true;
                }
                this.updateAllButtons();
            }
            if (guibutton.id == 200) {
                if (this.changed) {
                    Shaders.saveShaderPackOptions();
                    Shaders.uninit();
                }
                this.mc.displayGuiScreen(this.prevScreen);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        GuiButtonShaderOption btnSo;
        ShaderOption so;
        GuiButton btn;
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 1 && (btn = this.getSelectedButton(mouseX, mouseY)) instanceof GuiButtonShaderOption && (so = (btnSo = (GuiButtonShaderOption)btn).getShaderOption()).isChanged()) {
            btnSo.playPressSound(this.mc.getSoundHandler());
            so.resetValue();
            this.changed = true;
            this.updateAllButtons();
        }
    }

    private void updateAllButtons() {
        for (GuiButton btn : this.buttonList) {
            if (!(btn instanceof GuiButtonShaderOption)) continue;
            GuiButtonShaderOption gbso = (GuiButtonShaderOption)btn;
            ShaderOption opt = gbso.getShaderOption();
            if (opt instanceof ShaderOptionProfile) {
                ShaderOptionProfile optProf = (ShaderOptionProfile)opt;
                optProf.updateProfile();
            }
            gbso.displayString = this.getButtonText(opt, gbso.getButtonWidth());
        }
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        this.drawDefaultBackground();
        if (this.screenText != null) {
            this.drawCenteredString(this.fontRendererObj, this.screenText, this.width / 2, 15.0f, 0xFFFFFF);
        } else {
            this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15.0f, 0xFFFFFF);
        }
        super.drawScreen(x, y, f);
        if (Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5) {
            this.drawTooltips(x, y, this.buttonList);
        } else {
            this.lastMouseX = x;
            this.lastMouseY = y;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }

    private void drawTooltips(int x, int y, List buttonList) {
        int activateDelay = 700;
        if (System.currentTimeMillis() >= this.mouseStillTime + (long)activateDelay) {
            int x1 = this.width / 2 - 150;
            int y1 = this.height / 6 - 7;
            if (y <= y1 + 98) {
                y1 += 105;
            }
            int x2 = x1 + 150 + 150;
            int y2 = y1 + 84 + 10;
            GuiButton btn = this.getSelectedButton(x, y);
            if (btn instanceof GuiButtonShaderOption) {
                GuiButtonShaderOption btnSo = (GuiButtonShaderOption)btn;
                ShaderOption so = btnSo.getShaderOption();
                String[] lines = this.makeTooltipLines(so, x2 - x1);
                if (lines == null) {
                    return;
                }
                this.drawGradientRect(x1, y1, x2, y2, -536870912, -536870912);
                for (int i = 0; i < lines.length; ++i) {
                    String line = lines[i];
                    int col = 0xDDDDDD;
                    if (line.endsWith("!")) {
                        col = 0xFF2020;
                    }
                    this.fontRendererObj.drawStringWithShadow(line, x1 + 5, y1 + 5 + i * 11, col);
                }
            }
        }
    }

    private String[] makeTooltipLines(ShaderOption so, int width) {
        if (so instanceof ShaderOptionProfile) {
            return null;
        }
        String name = so.getNameText();
        String desc = Config.normalize(so.getDescriptionText()).trim();
        String[] descs = this.splitDescription(desc);
        String id = null;
        if (!name.equals(so.getName())) {
            id = String.valueOf(Lang.get("of.general.id")) + ": " + so.getName();
        }
        String source = null;
        if (so.getPaths() != null) {
            source = String.valueOf(Lang.get("of.general.from")) + ": " + Config.arrayToString(so.getPaths());
        }
        String def = null;
        if (so.getValueDefault() != null) {
            String list = so.isEnabled() ? so.getValueText(so.getValueDefault()) : Lang.get("of.general.ambiguous");
            def = String.valueOf(Lang.getDefault()) + ": " + list;
        }
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add(name);
        list1.addAll(Arrays.asList(descs));
        if (id != null) {
            list1.add(id);
        }
        if (source != null) {
            list1.add(source);
        }
        if (def != null) {
            list1.add(def);
        }
        String[] lines = this.makeTooltipLines(width, list1);
        return lines;
    }

    private String[] splitDescription(String desc) {
        if (desc.length() <= 0) {
            return new String[0];
        }
        desc = StrUtils.removePrefix(desc, "//");
        String[] descs = desc.split("\\. ");
        for (int i = 0; i < descs.length; ++i) {
            descs[i] = "- " + descs[i].trim();
            descs[i] = StrUtils.removeSuffix(descs[i], ".");
        }
        return descs;
    }

    private String[] makeTooltipLines(int width, List<String> args) {
        FontRenderer fr = Config.getMinecraft().fontRendererObj;
        ArrayList<String> list = new ArrayList<String>();
        for (int lines = 0; lines < args.size(); ++lines) {
            String arg = args.get(lines);
            if (arg == null || arg.length() <= 0) continue;
            List parts = fr.listFormattedStringToWidth(arg, width);
            for (String part : parts) {
                list.add(part);
            }
        }
        String[] var10 = list.toArray(new String[list.size()]);
        return var10;
    }

    private GuiButton getSelectedButton(int x, int y) {
        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton btn = (GuiButton)this.buttonList.get(i);
            int btnWidth = GuiVideoSettings.getButtonWidth(btn);
            int btnHeight = GuiVideoSettings.getButtonHeight(btn);
            if (x < btn.xPosition || y < btn.yPosition || x >= btn.xPosition + btnWidth || y >= btn.yPosition + btnHeight) continue;
            return btn;
        }
        return null;
    }
}

