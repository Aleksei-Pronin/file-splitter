package ru.shiftproject.pronin.splitter.processor;

import ru.shiftproject.pronin.splitter.datatype.DataType;
import ru.shiftproject.pronin.splitter.statistics.StatisticsUpdater;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileLineSplitter {
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

                DataType dataType = DataType.determineDataType(line);

                lineWriter.write(dataType, line);
                statisticsUpdater.update(dataType, line);
            }
        }
    }
}