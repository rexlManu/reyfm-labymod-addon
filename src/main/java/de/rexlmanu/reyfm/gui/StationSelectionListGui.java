package de.rexlmanu.reyfm.gui;

import de.rexlmanu.reyfm.ReyFMAddon;
import de.rexlmanu.reyfm.sender.RadioStation;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class StationSelectionListGui extends GuiListExtended {
    @Getter
    private StationSelectionListEntry selected;
    private List<StationSelectionListEntry> entries;

    public StationSelectionListGui(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.entries = new ArrayList<>();
        for (RadioStation radioStation : ReyFMAddon.getAddon().getRadioStations()) {
            this.entries.add(new StationSelectionListEntry(
                    this,
                    radioStation
            ));
        }

        if (ReyFMAddon.getAddon().getStationPlayer().isPlaying()) {
            for (StationSelectionListEntry entry : this.entries) {
                if (ReyFMAddon.getAddon().getStationPlayer().isPlaying() && entry.getRadioStation().equals(ReyFMAddon.getAddon().getStationPlayer().getRadioStation())) {
                    this.selected = entry;
                }
            }
        }

    }

    public void setSelected(StationSelectionListEntry selected) {
        this.selected = selected;
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return entries.get(slotIndex) == selected;
    }

    public StationSelectionListEntry selected() {
        return selected;
    }

    @Override
    public IGuiListEntry getListEntry(int index) {
        return entries.get(index);
    }

    @Override
    protected int getSize() {
        return entries.size();
    }

    @Override
    protected void drawSelectionBox(int p_drawSelectionBox_1_, int p_drawSelectionBox_2_, int p_drawSelectionBox_3_, int p_drawSelectionBox_4_) {

        int i = this.getSize();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        for (int j = 0; j < i; ++j) {
            int k = p_drawSelectionBox_2_ + j * this.slotHeight + this.headerPadding;
            int l = this.slotHeight - 4;
            if (k > this.bottom || k + l < this.top) {
                this.func_178040_a(j, p_drawSelectionBox_1_, k);
            }

     /*       if (this.showSelectionBox && this.isSelected(j)) {
                int i1 = this.left + (this.width / 2 - this.getListWidth() / 2);
                int j1 = this.left + this.width / 2 + this.getListWidth() / 2;
                GlStateManager.color(1.0F, 20.0F, 1.0F, 1.0F);
                GlStateManager.disableTexture2D();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos((double)i1, (double)(k + l + 2), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double)j1, (double)(k + l + 2), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double)j1, (double)(k - 2), 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double)i1, (double)(k - 2), 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double)(i1 + 1), (double)(k + l + 1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos((double)(j1 - 1), (double)(k + l + 1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos((double)(j1 - 1), (double)(k - 1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos((double)(i1 + 1), (double)(k - 1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }*/

            this.drawSlot(j, p_drawSelectionBox_1_, k, l, p_drawSelectionBox_3_, p_drawSelectionBox_4_);
        }
    }

    @Override
    protected void drawBackground() {
        //LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(amountScrolled);
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        if (this.field_178041_q) {
            this.mouseX = p_drawScreen_1_;
            this.mouseY = p_drawScreen_2_;
            this.drawBackground();
            int i = this.getScrollBarX();
            int j = i + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            //this.drawContainerBackground(tessellator);
            int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int l = this.top + 4 - (int) this.amountScrolled;
            if (this.hasListHeader) {
                this.drawListHeader(k, l, tessellator);
            }

            this.drawSelectionBox(k, l, p_drawScreen_1_, p_drawScreen_2_);
            GlStateManager.disableDepth();
            int i1 = 4;
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos((double) this.left, (double) (this.top + i1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos((double) this.right, (double) (this.top + i1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos((double) this.right, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos((double) this.left, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos((double) this.left, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos((double) this.right, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos((double) this.right, (double) (this.bottom - i1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos((double) this.left, (double) (this.bottom - i1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            tessellator.draw();
            int j1 = this.func_148135_f();
            if (j1 > 0) {
                int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                k1 = MathHelper.clamp_int(k1, 32, this.bottom - this.top - 8);
                int l1 = (int) this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;
                if (l1 < this.top) {
                    l1 = this.top;
                }

                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos((double) i, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos((double) j, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos((double) j, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos((double) i, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos((double) i, (double) (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) j, (double) (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) j, (double) l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos((double) i, (double) (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos((double) (j - 1), (double) (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos((double) (j - 1), (double) l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }

            this.func_148142_b(p_drawScreen_1_, p_drawScreen_2_);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
}