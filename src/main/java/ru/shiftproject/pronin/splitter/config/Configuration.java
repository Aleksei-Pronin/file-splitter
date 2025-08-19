package ru.shiftproject.pronin.splitter.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

public class Configuration {
    private Path outputDirectory = Paths.get(".");
    private String outputFilePrefix = "";

    private boolean appendMode = false;
    private boolean shortStatistics = false;
    private boolean fullStatistics = false;

    private final Set<Path> inputFiles = new LinkedHashSet<>();

    public boolean isAppendMode() {
        return appendMode;
    }

    public void enableAppendMode() {
        appendMode = true;
    }

    public boolean isShortStatistics() {
        return shortStatistics;
    }

    public void enableShortStatistics() {
        shortStatistics = true;
    }

    public boolean isFullStatistics() {
        return fullStatistics;
    }

    public void enableFullStatistics() {
        fullStatistics = true;
    }

    public boolean shouldPrintStatistics() {
        return shortStatistics || fullStatistics;
    }

    public Set<Path> getInputFiles() {
        return new LinkedHashSet<>(inputFiles);
    }

    public void addInputFile(Path path) {
        if (path != null && !path.toString().isBlank()) {
            inputFiles.add(path.toAbsolutePath().normalize());
        }
    }

    public Path getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getOutputFilePrefix() {
        return outputFilePrefix;
    }

    public void setOutputFilePrefix(String outputFilePrefix) {
        this.outputFilePrefix = outputFilePrefix;
    }
}