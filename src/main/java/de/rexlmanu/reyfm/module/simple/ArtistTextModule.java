package de.rexlmanu.reyfm.module.simple;

import de.rexlmanu.reyfm.ReyFMAddon;
import de.rexlmanu.reyfm.utility.Language;
import de.rexlmanu.reyfm.utility.Utils;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class ArtistTextModule extends SimpleModule {
    @Override
    public String getDisplayName() {
        return Language.format("module.simpleartist.displayname");
    }

    @Override
    public String getDisplayValue() {
        return ReyFMAddon.getAddon().getStationPlayer().isPlaying() ?
                Utils.substring(ReyFMAddon.getAddon().getStationPlayer().getRadioStation().getCurrentSong().getArtist(), 30)
                : this.getDefaultValue();
    }

    @Override
    public String getDefaultValue() {
        return Language.format("notplaying");
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.BOOK_AND_QUILL);
    }

    @Override
    public void loadSettings() {
    }

    @Override
    public String getControlName() {
        return Language.format("module.simpleartist.name");
    }

    @Override
    public String getSettingName() {
        return "reyfm_simpleartist";
    }

    @Override
    public String getDescription() {
        return Language.format("module.simpleartist.description");
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
