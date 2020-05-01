package radix.home.timelapsemaker.prefs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

@Slf4j
@Component
public class PreferencesManager {
    
    private static final String DEFAULT_VALUE = "";
    
    private static final Preferences preferences = Preferences.userNodeForPackage(PreferencesManager.class);
    
    /**
     * Method for initializing preferences only if the INITIATED preference does not exists
     */
    public void initPreferences() {
        if (preferences.get(EnumPreferences.INITIATED.getKey(), null) == null) {
            setDefaultPreferences();
        }
    }
    
    /**
     * Method for clearing all existing preferences
     */
    public void clearPreferences() {
        try {
            preferences.clear();
        } catch (BackingStoreException bse) {
            log.error("Error while clearing preferences", bse);
        }
    }
    
    /**
     * Method for initializing default preferences
     */
    public void setDefaultPreferences() {
        for (EnumPreferences pref : EnumPreferences.values()) {
            savePreference(pref, pref.getDefaultValue());
        }
    }
    
    /**
     * Method for updating preferences (only preferences that does not exists are added)
     */
    public void updatePreferences() {
        for (EnumPreferences pref : EnumPreferences.values()) {
            if (preferences.get(pref.getKey(), null) == null) {
                savePreference(pref, pref.getDefaultValue());
            }
        }
    }
    
    /**
     * Method for saving a preference value
     *
     * @param pref  Preference enum item
     * @param value The value to save
     */
    public void savePreference(EnumPreferences pref, String value) {
        preferences.put(pref.getKey(), value);
    }
    
    /**
     * Method for getting preference value
     *
     * @param pref Preference enum item
     * @return The preference value
     */
    public String getPreference(EnumPreferences pref) {
        return preferences.get(pref.getKey(), DEFAULT_VALUE);
    }
    
    /**
     * Method for getting value of preference or the default value if no preference exists
     *
     * @param pref Preference enum item
     * @return The preference value (or the default one if no preference value exists)
     */
    public String getPreferenceOrDefault(EnumPreferences pref) {
        String out = getPreference(pref);
        if (out.isEmpty()) {
            out = pref.getDefaultValue();
        }
        return out;
    }
    
}
