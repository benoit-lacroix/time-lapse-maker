package radix.home.timelapsemaker.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EnumFrameRate {
    FR_12("12", 1, 12),
    FR_24("24", 1, 24),
    FR_48("48", 1, 48),
    FR_96("96", 1, 96);

    @Getter(value = AccessLevel.NONE)
    public static final String DEFAULT_VALUE = "24";

    private final String label;
    private final int numerator;
    private final int denominator;

    public static EnumFrameRate getByLabel(String stringValue) {
        for (EnumFrameRate item : EnumFrameRate.values()) {
            if (item.getLabel().equals(stringValue)) {
                return item;
            }
        }
        return null;
    }

    public static List<String> getValues() {
        List<String> frameRates = new ArrayList<>();
        Arrays.stream(EnumFrameRate.values()).forEach(value -> frameRates.add(value.getLabel()));
        return frameRates;
    }
}
