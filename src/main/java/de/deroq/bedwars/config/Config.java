package de.deroq.bedwars.config;

import java.io.File;

/**
 * @author deroq
 * @since 06.07.2022
 */
public abstract class Config {

    private final String fileName;

    public Config(File file) {
        this.fileName = file.getName();
    }

    public String getFileName() {
        return fileName;
    }
}
