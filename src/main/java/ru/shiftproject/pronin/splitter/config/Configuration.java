package ru.shiftproject.pronin.splitter.config;

import java.nio.file.Path;
import java.util.Set;

public record Configuration(
        Path outputDirectory,
        String outputFilePrefix,
        boolean appendMode,
        boolean shortStatistics,
        boolean fullStatistics,
        Set<Path> inputFiles
) {
    public Configuration {
        inputFiles = Set.copyOf(inputFiles);
    }

    public boolean shouldPrintStatistics() {
        return shortStatistics || fullStatistics;
    }
}