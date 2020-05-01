package radix.home.timelapsemaker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumFrameRate {
    FR_12("12", 1, 12),
    FR_24("24", 1, 24);

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
}
