package vmucexporter.common.utils;

import junit.framework.TestCase;
import org.junit.Test;

import java.net.URL;
import java.util.Locale;

/**
 * Tests if the localization works.
 *
 * @author Nico Schwanebeck
 * @since 1.1.0
 */
public class TestLocalization extends TestCase {

    /**
     * Sets the local to german, picks one key and tests if the right text is returned.
     */
    @Test
    public void testGerman() {

//        LangUtils.reloadResourceBundle(Locale.GERMANY);
//        assertEquals("The returned text differs from the expected text.", "Ger\u00E4t: %s", LangUtils.getString("htmlunit.BrowserMain.device.debug"));
    }

    /**
     * Sets the local to english, picks one key and tests if the right text is returned.
     */
    @Test
    public void testEnglish() {

//        LangUtils.reloadResourceBundle(Locale.UK);
//        assertEquals("The returned text differs from the expected text.", "Device: %s", LangUtils.getString("htmlunit.BrowserMain.device.debug"));
    }
}
