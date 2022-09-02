package io.github.radixhomework.timelapsemaker.config;

import io.github.radixhomework.timelapsemaker.enums.EnumImageType;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.function.Predicate;

@Slf4j
public class ImagePredicate implements Predicate<File> {

    @Override
    public boolean test(File file) {
        return EnumImageType.isAllowed(file.toPath());
    }
}
