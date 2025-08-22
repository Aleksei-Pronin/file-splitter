package ru.shiftproject.pronin.splitter.config;

import java.nio.file.Path;
import java.util.List;

public record Configuration(
        Path outputDirectory,
        String outputFilePrefix,
        boolean appendMode,
        boolean shortStatistics,
        boolean fullStatistics,
        List<Path> inputFiles
) {
    public Configuration {
        inputFiles = List.copyOf(inputFiles);
    }

    public boolean shouldPrintStatistics() {
        return shortStatistics || fullStatistics;
    }
}