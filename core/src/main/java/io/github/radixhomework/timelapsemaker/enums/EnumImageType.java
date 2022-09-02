package io.github.radixhomework.timelapsemaker.enums;

import io.github.radixhomework.timelapsemaker.exception.EnumValueNotFoundException;
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

    public static final String ERROR_PREFIX = "Image type with value ";
    public static final String ERROR_SUFFIX = " not found";
    private final String type;

    public static EnumImageType findByType(String type) {
        for (EnumImageType value : EnumImageType.values()) {
            if (value.matches(type)) {
                return value;
            }
        }
        throw new EnumValueNotFoundException(ERROR_PREFIX + type + ERROR_SUFFIX);
    }

    public static EnumImageType findByType(Path path) {
        for (EnumImageType value : EnumImageType.values()) {
            if (value.matches(path)) {
                return value;
            }
        }
        throw new EnumValueNotFoundException(ERROR_PREFIX + path.toString() + ERROR_SUFFIX);
    }

    public static boolean isAllowed(String type) {
        for (EnumImageType value : EnumImageType.values()) {
            if (value.matches(type)) {
                return true;
            }
        }
        throw new EnumValueNotFoundException(ERROR_PREFIX + type + ERROR_SUFFIX);
    }

    public static boolean isAllowed(Path path) {
        for (EnumImageType value : EnumImageType.values()) {
            if (value.matches(path)) {
                return true;
            }
        }
        throw new EnumValueNotFoundException(ERROR_PREFIX + path.toString() + ERROR_SUFFIX);
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
