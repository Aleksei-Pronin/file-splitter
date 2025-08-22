package ru.shiftproject.pronin.splitter.statistics;

import ru.shiftproject.pronin.splitter.config.Configuration;
import ru.shiftproject.pronin.splitter.datatype.DataType;
import ru.shiftproject.pronin.splitter.statistics.collector.StatisticsCollector;

import java.util.Map;

public class StatisticsUpdater {
    private final Configuration configuration;
    private final Map<DataType, StatisticsCollector> statisticsCollectorsByType;

    public StatisticsUpdater(
            Configuration configuration,
            Map<DataType, StatisticsCollector> statisticsCollectorsByType
    ) {
        this.configuration = configuration;
        this.statisticsCollectorsByType = statisticsCollectorsByType;
    }

    public void update(String line) {
        if (configuration.shouldPrintStatistics()) {
            DataType dataType = DataType.determineDataType(line);

            statisticsCollectorsByType.computeIfAbsent(
                    dataType,
                    type -> StatisticsCollectorCreator.create(
                            type,
                            configuration
                    )
            ).collect(line);
        }
    }
}