package de.rexlmanu.reyfm.module.simple;

import de.rexlmanu.reyfm.ReyFMAddon;
import de.rexlmanu.reyfm.sender.RadioSong;
import de.rexlmanu.reyfm.utility.Language;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class DurationTextModule extends SimpleModule {
    @Override
    public String getDisplayName() {
        return Language.format("module.simpleduration.displayname");
    }

    @Override
    public String getDisplayValue() {
        if (!ReyFMAddon.getAddon().getStationPlayer().isPlaying()) return this.getDefaultValue();

        RadioSong currentSong = ReyFMAddon.getAddon().getStationPlayer().getRadioStation().getCurrentSong();
        long millis = System.currentTimeMillis();

        return DurationFormatUtils.formatDuration(currentSong.getEndTime() - millis, "mm:ss");
    }

    @Override
    public String getDefaultValue() {
        return Language.format("notplaying");
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.WATCH);
    }

    @Override
    public void loadSettings() {
    }

    @Override
    public String getControlName() {
        return Language.format("module.simpleduration.name");
    }

    @Override
    public String getSettingName() {
        return "reyfm_simpleduration";
    }

    @Override
    public String getDescription() {
        return Language.format("module.simpleduration.description");
    }

    @Override
    public int getSortingId() {
        return 0;
    }

    @Override
    public ModuleCategory getCategory() {
        return ReyFMAddon.getAddon().getModuleCategory();
    }
}
