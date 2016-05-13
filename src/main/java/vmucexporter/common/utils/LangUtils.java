package vmucexporter.common.utils;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Nico Schwanebeck
 * @since 1.1
 */
public final class LangUtils {
    private static ResourceBundle resourceBundle = loadResourceBundle(Locale.getDefault());

    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    static void reloadResourceBundle(Locale locale) {

        resourceBundle = loadResourceBundle(locale);
    }

    private static ResourceBundle loadResourceBundle(Locale locale) {

        return PropertyResourceBundle.getBundle("LangUtils", locale, ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
}
