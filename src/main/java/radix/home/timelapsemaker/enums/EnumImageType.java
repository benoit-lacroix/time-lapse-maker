package radix.home.timelapsemaker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Getter
@AllArgsConstructor
public enum EnumImageType {
    JPG("image/jpeg"),
    PNG("image/png");

    private final String type;

    public static EnumImageType findByType(String type) {
        for (EnumImageType value : EnumImageType.values()) {
            if (value.matches(type)) {
                return value;
            }
        }
        return null;
    }

    public static EnumImageType findByType(Path path) {
        for (EnumImageType value : EnumImageType.values()) {
            if (value.matches(path)) {
                return value;
            }
        }
        return null;
    }

    public static boolean isAllowed(String type) {
        for (EnumImageType value : EnumImageType.values()) {
            if (value.matches(type)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllowed(Path path) {
        for (EnumImageType value : EnumImageType.values()) {
            if (value.matches(path)) {
                return true;
            }
        }
        return false;
    }

    public boolean matches(String type) {
        return type.equals(this.getType());
    }

    public boolean matches(Path path) {
        try {
            return type.equals(Files.probeContentType(path));
        } catch (IOException ioe) {
            log.error("Error while verifying image type", ioe);
        }
        return false;
    }
}
