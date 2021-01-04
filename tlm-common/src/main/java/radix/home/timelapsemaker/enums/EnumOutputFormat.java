package radix.home.timelapsemaker.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;

import java.util.Arrays;

@Slf4j
@Getter
@AllArgsConstructor
public enum EnumOutputFormat {
    MP4("MPEG4", ".mp4"),
    AVI("AVI", ".avi");

    @Getter(value = AccessLevel.NONE)
    public static final String DEFAULT_VALUE = MP4.getLabel();

    private final String label;
    private final String extension;

    public static EnumOutputFormat findByLabel(String label) {
        for (EnumOutputFormat value : EnumOutputFormat.values()) {
            if (value.matches(label)) {
                return value;
            }
        }
        return null;
    }

    public static List<String> getValues() {
        List<String> formats = new ArrayList<>();
        Arrays.stream(EnumOutputFormat.values()).forEach(value -> formats.add(value.getLabel()));
        return formats;
    }

    public boolean matches(String label) {
        return label.equals(this.getLabel());
    }
}
