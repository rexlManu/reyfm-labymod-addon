package de.rexlmanu.reyfm.gui;

import de.rexlmanu.reyfm.ReyFMAddon;
import de.rexlmanu.reyfm.sender.RadioSong;
import de.rexlmanu.reyfm.sender.RadioStation;
import de.rexlmanu.reyfm.utility.StringDrawUtils;
import lombok.Getter;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.awt.*;

public class StationSelectionListEntry implements GuiListExtended.IGuiListEntry {

    public static ResourceLocation BACKGROUND_RESOURCE = new ResourceLocation("labymod/textures/reyfm/background.png");
    public static ResourceLocation LISTENER_ICON_RESOURCE = new ResourceLocation("labymod/textures/reyfm/listener_icon.png");

    private final StationSelectionListGui parent;
    @Getter
    private final RadioStation radioStation;

    public StationSelectionListEntry(StationSelectionListGui parent, RadioStation radioStation) {
        this.parent = parent;
        this.radioStation = radioStation;
    }

    @Override
    public void setSelected(int i, int i1, int i2) {

    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isHovered) {
        DrawUtils drawUtils = LabyMod.getInstance().getDrawUtils();

        boolean isSelected = parent.getSelected() != null && parent.getSelected() == this;
        if (isSelected) {
            Gui.drawRect(listWidth + x + 1, slotHeight + y + 1, x - 1, y - 1, this.radioStation.getColor().getRGB());
        }
        Gui.drawRect(listWidth + x, slotHeight + y, x, y, Color.decode("#131317").getRGB());
        RadioSong currentSong = this.radioStation.getCurrentSong();
        drawUtils.drawImageUrl(currentSong.getCover(), x + 1, y + 1, 256, 256, 30, 30);

        String stationName = "#" + this.radioStation.getName().toUpperCase();
        StringDrawUtils.drawColorizedString(stationName, x + 34, y + 2, 0.6, this.radioStation.getColor().getRGB());
        String songName = currentSong.getTitle() /*+ " - " + this.radioStation.getCurrentSong().getTitle()*/;
        if (songName.length() > 55) {
            songName = songName.substring(0, 55) + "...";
        }
        StringDrawUtils.drawColorizedString(currentSong.getArtist(), x + 35, y + 8, 0.55, Color.decode("#727679").getRGB());
        StringDrawUtils.drawColorizedString(songName, x + 35, y + 13, 0.5, Color.decode("#727679").getRGB());
        long millis = System.currentTimeMillis();
        DrawUtils.drawRect(listWidth + x - 3, slotHeight + y - 3, x + 34, y + 24, Color.decode("#32323c").getRGB());
        StringDrawUtils.drawColorizedString(DurationFormatUtils.formatDuration((millis - currentSong.getStartTime()), "mm:ss"), x + 35, y + 19, 0.5, Color.decode("#4f4f5f").getRGB());
        StringDrawUtils.drawColorizedString(DurationFormatUtils.formatDuration(currentSong.getEndTime() - millis, "mm:ss"), x + 204, y + 19, 0.5, Color.decode("#4f4f5f").getRGB());

        double percent = (((double) millis - currentSong.getStartTime()) / ((double) currentSong.getEndTime() - currentSong.getStartTime()));
        if (percent > 1) percent = 1;
        DrawUtils.drawRect(x + 36, slotHeight + y - 5, (int) (x + 36 + (percent * (listWidth - 41))), y + 26, isSelected ? this.radioStation.getColor().getRGB() : Color.decode("#4f4f5f").getRGB());
        if (this.radioStation.isLive()) {
            String text = "LIVE";
            StringDrawUtils.drawColorizedString(text, x + listWidth - drawUtils.getStringWidth(text) + 7, y + 2, 0.6, Color.decode("#96031A").getRGB());
        } else {
            String text = this.radioStation.getListeners() + "";
            StringDrawUtils.drawColorizedString(text, x + listWidth - 10, y + 2, 0.5, Color.decode("#4f4f5f").getRGB());
            drawUtils.bindTexture(LISTENER_ICON_RESOURCE);
            drawUtils.drawTexture(x + 205, y + 1.5, 256, 256, 3.8, 3.8);
        }
    }

    @Override
    public boolean mousePressed(
            int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX,
            int relativeY
    ) {
        if (parent.getSelected() == this){
            parent.setSelected(null);
            ReyFMAddon.getAddon().getStationPlayer().stopPlaying();
            return false;
        }
        parent.setSelected(this);
        ReyFMAddon.getAddon().getStationPlayer().start(this.radioStation);
        return false;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

    }
}