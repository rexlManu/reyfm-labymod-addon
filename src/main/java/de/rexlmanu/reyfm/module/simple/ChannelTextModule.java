package de.rexlmanu.reyfm.module.simple;

import de.rexlmanu.reyfm.ReyFMAddon;
import de.rexlmanu.reyfm.utility.Language;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class ChannelTextModule extends SimpleModule {
    @Override
    public String getDisplayName() {
        return Language.format("module.simplechannel.displayname");
    }

    @Override
    public String getDisplayValue() {
        return ReyFMAddon.getAddon().getStationPlayer().isPlaying() ?
                "#" + ReyFMAddon.getAddon().getStationPlayer().getRadioStation().getName().toUpperCase()
                : this.getDefaultValue();
    }

    @Override
    public String getDefaultValue() {
        return Language.format("notplaying");
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.RAILS);
    }

    @Override
    public void loadSettings() {
    }

    @Override
    public String getControlName() {
        return Language.format("module.simplechannel.name");
    }

    @Override
    public String getSettingName() {
        return "reyfm_simplechannel";
    }

    @Override
    public String getDescription() {
        return Language.format("module.simplechannel.description");
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
