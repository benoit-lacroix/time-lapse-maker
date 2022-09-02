package io.github.radixhomework.timelapsemaker.beans;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class Image {

    public Image(String path) {
        this.path = path;
        file = new File(path);
    }

    private String path;
    private File file;
}
