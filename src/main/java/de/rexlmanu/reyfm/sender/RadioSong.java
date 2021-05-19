package de.rexlmanu.reyfm.sender;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class RadioSong {

    private String title, artist, cover;

    private long startTime, endTime;

}
