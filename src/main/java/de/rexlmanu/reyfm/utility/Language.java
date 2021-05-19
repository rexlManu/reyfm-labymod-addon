package de.rexlmanu.reyfm.utility;

import de.rexlmanu.reyfm.ReyFMAddon;

public class Language {

    public static String format(String key, Object... values) {
        return ReyFMAddon.getAddon().getLanguageProvider().format(key, values);
    }

}
