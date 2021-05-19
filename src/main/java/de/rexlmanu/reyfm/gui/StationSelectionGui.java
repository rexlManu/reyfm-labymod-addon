package de.rexlmanu.reyfm.gui;

import de.rexlmanu.reyfm.ReyFMAddon;
import de.rexlmanu.reyfm.utility.Language;
import net.labymod.gui.elements.Tabs;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.gui.*;

import java.io.IOException;

public class StationSelectionGui extends GuiScreen implements GuiPageButtonList.GuiResponder {

    private StationSelectionListGui stationSelectionListGui;
    private GuiSlider guiSlider;

    @Override
    public void initGui() {
        super.initGui();

        Tabs.initGuiScreen(this.buttonList, this);
        this.stationSelectionListGui = new StationSelectionListGui(
                mc,
                width,
                height,
                32,
                height - 32,
                36
        );

        this.guiSlider = new GuiSlider(
                this,
                0xFFFFFF,
                this.width / 2 - 74,
                this.height - 28,
                Language.format("volume"),
                0,
                100,
                ReyFMAddon.getAddon().getVolume(),
                new GuiSlider.FormatHelper() {
                    @Override
                    public String getText(int i, String s, float value) {
                        return String.valueOf((int) value);
                    }
                }
        );

        this.buttonList.add(this.guiSlider);
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        DrawUtils drawUtils = LabyMod.getInstance().getDrawUtils();
        drawUtils.drawAutoDimmedBackground(0);
        this.stationSelectionListGui.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.stationSelectionListGui.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) throws IOException {
        super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
        this.stationSelectionListGui.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
    }

    @Override
    protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
        super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
        this.stationSelectionListGui.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        Tabs.actionPerformedButton(button);
    }

    @Override
    public void func_175321_a(int i, boolean b) {

    }

    @Override
    public void onTick(int i, float v) {
        ReyFMAddon.getAddon().setVolume((int) v);
    }

    @Override
    public void func_175319_a(int i, String s) {

    }
}
