package ru.shiftproject.pronin.splitter.processor;

import ru.shiftproject.pronin.splitter.statistics.StatisticsUpdater;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileLineSplitter {
    private FileLineSplitter() {
    }

    public static void splitByType(Path inputFile,
                                   FileLineWriter lineWriter,
                                   StatisticsUpdater statisticsUpdater
    ) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(inputFile)) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                lineWriter.write(line);
                statisticsUpdater.update(line);
            }
        }
    }
}