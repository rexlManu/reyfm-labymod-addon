package de.rexlmanu.reyfm.module.simple;

import de.rexlmanu.reyfm.ReyFMAddon;
import de.rexlmanu.reyfm.utility.Language;
import de.rexlmanu.reyfm.utility.Utils;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class TitleTextModule extends SimpleModule {
    @Override
    public String getDisplayName() {
        return Language.format("module.simpletitle.displayname");
    }

    @Override
    public String getDisplayValue() {
        return ReyFMAddon.getAddon().getStationPlayer().isPlaying() ?
                Utils.substring(ReyFMAddon.getAddon().getStationPlayer().getRadioStation().getCurrentSong().getTitle(), 30)
                : this.getDefaultValue();
    }

    @Override
    public String getDefaultValue() {
        return Language.format("notplaying");
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.PAPER);
    }

    @Override
    public void loadSettings() {
    }

    @Override
    public String getControlName() {
        return Language.format("module.simpletitle.name");
    }

    @Override
    public String getSettingName() {
        return "reyfm_simpleduration";
    }
    @Override
    public String getDescription() {
        return Language.format("module.simpletitle.description");
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
