package de.rexlmanu.reyfm.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Quality {

    HIGH("320"),
    MID("192"),
    LOW("128");

    private String url;

}
