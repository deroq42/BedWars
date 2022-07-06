package de.deroq.bedwars.config;

public abstract class Config {

    private final String fileName;

    public Config(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
