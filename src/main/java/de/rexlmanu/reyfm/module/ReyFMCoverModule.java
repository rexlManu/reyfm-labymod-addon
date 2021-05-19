package de.rexlmanu.reyfm.module;

import de.rexlmanu.reyfm.ReyFMAddon;
import de.rexlmanu.reyfm.music.StationPlayer;
import de.rexlmanu.reyfm.sender.RadioSong;
import de.rexlmanu.reyfm.sender.RadioStation;
import de.rexlmanu.reyfm.utility.Language;
import de.rexlmanu.reyfm.utility.StringDrawUtils;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.enums.EnumDisplayType;
import net.labymod.ingamegui.moduletypes.ResizeableModule;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.Material;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.awt.*;

public class ReyFMCoverModule extends ResizeableModule {
    public static ResourceLocation LISTENER_ICON_RESOURCE = new ResourceLocation("labymod/textures/reyfm/listener_icon.png");
    public static ResourceLocation LOGO_RESOURCE = new ResourceLocation("labymod/textures/reyfm/logo.png");
    public static ResourceLocation PLAY_RESOURCE = new ResourceLocation("labymod/textures/reyfm/play.png");
    public static ResourceLocation PAUSE_RESOURCE = new ResourceLocation("labymod/textures/reyfm/pause.png");

    private boolean hovering;

    public ReyFMCoverModule() {
        super((short) 200, (short) 50, (short) 170, (short) 40, (short) 250, (short) 60);

    }

    @Override
    public void drawModule(double x, double y, double rightX, double width, double height, double mouseX, double mouseY) {
        DrawUtils drawUtils = LabyMod.getInstance().getDrawUtils();

        this.hovering = false;

        if (!ReyFMAddon.getAddon().getStationPlayer().isPlaying()) {
//            Gui.drawRect((int) (width + x), (int) (height + y), (int) x, (int) y, Color.decode("#131317").getRGB());
//            drawUtils.bindTexture(LOGO_RESOURCE);
//            drawUtils.drawTexture( x + 1, y + 1, 240, 240, height - 3, height - 3);
            return;
        }
        ;
        RadioStation radioStation = ReyFMAddon.getAddon().getStationPlayer().getRadioStation();

        double fontScale = ((height - this.minHeight)) / 1.0;
        if (fontScale > 1.0) {
            fontScale = 1.0;
        }
        if (fontScale < 0.1) {
            fontScale = 0.9;
        }
        double fontHeight = fontScale * 10.0;

        Gui.drawRect((int) (width + x), (int) (height + y), (int) x, (int) y, Color.decode("#131317").getRGB());
        RadioSong currentSong = radioStation.getCurrentSong();
        drawUtils.drawImageUrl(currentSong.getCover(), x + 1, y + 1, 256, 256, height - 3, height - 3);

        String stationName = "#" + radioStation.getName().toUpperCase();
        double afterImage = x + height + 1;
        StringDrawUtils.drawColorizedString(stationName, (int) afterImage, (int) (y + 2), fontScale, radioStation.getColor().getRGB());
        String songName = currentSong.getTitle() /*+ " - " + this.radioStation.getCurrentSong().getTitle()*/;
        if (songName.length() > 30) {
            songName = songName.substring(0, 30) + "...";
        }
        StringDrawUtils.drawColorizedString(songName, (int) afterImage, (int) (y + (fontHeight * 1.3)), fontScale * 0.7, Color.decode("#727679").getRGB());
        StringDrawUtils.drawColorizedString(currentSong.getArtist(), (int) afterImage, (int) (y + ((fontHeight * 2))), fontScale * 0.7, Color.decode("#727679").getRGB());
        long millis = System.currentTimeMillis();
        DrawUtils.drawRect((int) (width + x - 3), (int) (height + y - 3), (int) afterImage, (int) (y + height - 8), Color.decode("#32323c").getRGB());
        StringDrawUtils.drawColorizedString(DurationFormatUtils.formatDuration((millis - currentSong.getStartTime()), "mm:ss"), (int) afterImage, (int) (y + height - 13), 0.5, Color.decode("#4f4f5f").getRGB());
        StringDrawUtils.drawColorizedString(DurationFormatUtils.formatDuration(currentSong.getEndTime() - millis, "mm:ss"), (int) (x + width - 16), (int) (y + height - 13), 0.5, Color.decode("#4f4f5f").getRGB());

        double percent = (((double) millis - currentSong.getStartTime()) / ((double) currentSong.getEndTime() - currentSong.getStartTime()));
        if (percent >= 1) percent = 1;
        DrawUtils.drawRect((int) (x + height + 2), (int) (height + y - 5), (int) (x + height + 2 + (percent * (width - height - 7))), (int) (y + height - 6), radioStation.getColor().getRGB());
        if (radioStation.isLive()) {
            String text = "LIVE";
            StringDrawUtils.drawColorizedString(text, (int) (x + width - drawUtils.getStringWidth(text) + 7), (int) (y + 2), 0.6, Color.decode("#96031A").getRGB());
        } else {
            String text = radioStation.getListeners() + "";
            StringDrawUtils.drawColorizedString(text, (int) (x + width - 21 + 10), (int) (y + 2), 0.5, Color.decode("#4f4f5f").getRGB());
            drawUtils.bindTexture(LISTENER_ICON_RESOURCE);
            drawUtils.drawTexture(x + width - 26 + 10, y + 1.5, 256, 256, 3.8, 3.8);
        }
        this.hovering = mouseX < height && mouseY < height && mouseX > 0 && mouseY > 0;
        drawUtils.bindTexture(ReyFMAddon.getAddon().getStationPlayer().isPaused() ? PLAY_RESOURCE : PAUSE_RESOURCE);
        drawUtils.drawTexture(x + (height / 4), y + (height / 4), 256, 256, (height / 2), (height / 2), this.hovering? 1f:0.4f);

    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
        super.onMouseClick(mouseX, mouseY, mouseButton);
        StationPlayer stationPlayer = ReyFMAddon.getAddon().getStationPlayer();
        if (stationPlayer.isPaused()) {
                stationPlayer.resume();
        }else {
            stationPlayer.pause();
        }
    }

    @Override
    public boolean isMovable(int mouseX, int mouseY) {
        return super.isMovable(mouseX, mouseY) && !this.hovering;
    }

    @Override
    public boolean isShown() {
        return ReyFMAddon.getAddon().getStationPlayer().isPlaying() && super.isShown();
    }

    @Override
    public String getControlName() {
        return Language.format("module.cover.name");
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.NOTE_BLOCK);
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public String getSettingName() {
        return "reyfm_cover";
    }

    @Override
    public String getDescription() {
        return Language.format("module.cover.description");
    }

    @Override
    public int getSortingId() {
        return 0;
    }

    @Override
    public ModuleCategory getCategory() {
        return ReyFMAddon.getAddon().getModuleCategory();
    }

    @Override
    public EnumDisplayType[] getDisplayTypes() {
        return new EnumDisplayType[]{EnumDisplayType.INGAME, EnumDisplayType.ESCAPE};
    }
}
