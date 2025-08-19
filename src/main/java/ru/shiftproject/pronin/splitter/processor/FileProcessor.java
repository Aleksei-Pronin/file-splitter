package ru.shiftproject.pronin.splitter.processor;

import ru.shiftproject.pronin.splitter.config.Configuration;
import ru.shiftproject.pronin.splitter.datatype.DataType;
import ru.shiftproject.pronin.splitter.statistics.collector.StatisticsCollector;
import ru.shiftproject.pronin.splitter.statistics.report.StatisticsReportPrinter;
import ru.shiftproject.pronin.splitter.statistics.updater.StatisticsUpdater;

import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class FileProcessor {
    public static void process(Configuration configuration) throws IOException {
        Map<DataType, StatisticsCollector> statisticsCollectorsByType = new EnumMap<>(DataType.class);

        try (FileLineWriter lineWriter = new FileLineWriter(configuration)) {
            StatisticsUpdater statisticsProcessor = new StatisticsUpdater(
                    configuration,
                    statisticsCollectorsByType
            );

            for (Path inputFile : configuration.getInputFiles()) {
                FileLineSplitter.splitByType(
                        inputFile,
                        lineWriter,
                        statisticsProcessor
                );
            }

            if (configuration.shouldPrintStatistics()
                    && !statisticsCollectorsByType.isEmpty()) {
                StatisticsReportPrinter.print(
                        configuration,
                        statisticsCollectorsByType
                );
            }
        }
    }
}