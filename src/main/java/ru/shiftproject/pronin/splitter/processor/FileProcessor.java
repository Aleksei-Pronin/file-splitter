package ru.shiftproject.pronin.splitter.processor;

import ru.shiftproject.pronin.splitter.config.Configuration;
import ru.shiftproject.pronin.splitter.datatype.DataType;
import ru.shiftproject.pronin.splitter.statistics.collector.StatisticsCollector;
import ru.shiftproject.pronin.splitter.statistics.StatisticsReportPrinter;
import ru.shiftproject.pronin.splitter.statistics.StatisticsUpdater;

import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class FileProcessor {
    public void process(Configuration configuration) throws IOException {
        Map<DataType, StatisticsCollector> statisticsCollectorsByType = new EnumMap<>(DataType.class);

        try (FileLineWriter lineWriter = new FileLineWriter(configuration)) {
            StatisticsUpdater statisticsUpdater = new StatisticsUpdater(
                    configuration,
                    statisticsCollectorsByType
            );

            for (Path inputFile : configuration.inputFiles()) {
                FileLineSplitter.splitByType(
                        inputFile,
                        lineWriter,
                        statisticsUpdater
                );
            }

            if (configuration.shouldPrintStatistics()
                    && !statisticsCollectorsByType.isEmpty()) {
                new StatisticsReportPrinter(System.out).print(
                        configuration,
                        statisticsCollectorsByType
                );
            }
        }
    }
}