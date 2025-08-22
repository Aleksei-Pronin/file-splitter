package ru.shiftproject.pronin.splitter.statistics;

import ru.shiftproject.pronin.splitter.config.Configuration;
import ru.shiftproject.pronin.splitter.config.OutputFileInfo;
import ru.shiftproject.pronin.splitter.datatype.DataType;
import ru.shiftproject.pronin.splitter.statistics.collector.StatisticsCollector;

import java.io.PrintStream;
import java.util.Map;

public class StatisticsReportPrinter {
    private final PrintStream out;

    public StatisticsReportPrinter(PrintStream out) {
        this.out = out;
    }

    public void print(Configuration configuration,
                      Map<DataType, StatisticsCollector> statisticsCollectorsByType) {
        for (DataType dataType : DataType.values()) {
            StatisticsCollector statisticsCollector = statisticsCollectorsByType.get(dataType);

            if (statisticsCollector == null) {
                continue;
            }

            out.printf("Статистика для %s%n",
                    OutputFileInfo.resolveOutputFilePath(configuration, dataType).getFileName());
            out.println(statisticsCollector.buildStatistics());
        }
    }
}