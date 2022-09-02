package io.github.radixhomework.timelapsemaker.config;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Comparator;

@Slf4j
public class ImageComparator {

    public Comparator<File> byFileName() {
        return Comparator.comparing(File::getName);
    }
}
