package de.rexlmanu.reyfm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.labymod.utils.Material;

@AllArgsConstructor
@Getter
public enum Notification {

    LEVEL_BROADCAST(Material.EXP_BOTTLE, true),
    LIVE(Material.NETHER_STAR, true);

    private Material material;
    private boolean value;

}
