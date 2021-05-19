package de.rexlmanu.reyfm.sender;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;

@Data
@AllArgsConstructor
public class RadioStation {

    private String name, description;
    private boolean live;
    private Color color;
    private int listeners;
    private RadioSong currentSong;
}
