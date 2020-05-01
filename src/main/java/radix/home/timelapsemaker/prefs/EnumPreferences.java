package radix.home.timelapsemaker.prefs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public enum EnumPreferences {
    /**
     * Preferences for checking if preferences have been initialized
     */
    INITIATED("INITIATED", "122403a33581a8c8846674e086d00a22"),
    FRAME_RATE("FRAME_RATE", "24"),
    IMAGE_HEIGHT("IMAGE_HEIGHT", ""),
    IMAGE_WIDTH("IMAGE_WIDTH", ""),
    SOURCE_DIR("SOURCE_DIR", ""),
    DEST_FILE("DEST_FILE", "");

    private final String key;
    private final String defaultValue;
    
    public static EnumPreferences getByKey(String key) {
        for (EnumPreferences pref : EnumPreferences.values()) {
            if (pref.getKey().equals(key)) {
                return pref;
            }
        }
        return null;
    }
    
}
