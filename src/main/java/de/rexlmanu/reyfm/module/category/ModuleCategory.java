package de.rexlmanu.reyfm.module.category;

import net.labymod.settings.elements.ControlElement;
import net.minecraft.util.ResourceLocation;

public class ModuleCategory extends net.labymod.ingamegui.ModuleCategory {
    public ModuleCategory() {
        super("REYFM", true, new ControlElement.IconData(new ResourceLocation("labymod/textures/reyfm/logo.png")));
    }
}
