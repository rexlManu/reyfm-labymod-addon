package de.rexlmanu.reyfm;

import net.minecraft.client.Minecraft;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class LanguageProvider {

    private static final String FALLBACK_LOCALE = "en_US";

    private String currentLocale;
    private Properties cachedProperties;

    public LanguageProvider() {
        this.reloadLanguage();
    }

    private void reloadLanguage() {
        this.currentLocale = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
        this.loadProperties();
    }

    private boolean languageHasChanged() {
        return !this.currentLocale.equals(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode());
    }

    private void loadProperties() {
        try (InputStream inputStream = LanguageProvider.class.getClassLoader().getResourceAsStream("lang/" + this.currentLocale + ".properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            this.cachedProperties = properties;
        } catch (Throwable throwable) {
            System.out.println("Could not load language. Trying to load fallback language.");
            if (!this.currentLocale.equals(FALLBACK_LOCALE)) {
                this.currentLocale = FALLBACK_LOCALE;
                this.loadProperties();
            } else {
                System.out.println("Could even not found fallback language.");
            }
        }
    }

    private String getString(String key) {
        key = key.toLowerCase();
        if (this.languageHasChanged()) this.reloadLanguage();
        return new String(this.cachedProperties.getProperty(key, key).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String format(String key, Object... values) {
        if (values.length == 0) return this.getString(key);
        return String.format(this.getString(key), values);
    }
}
