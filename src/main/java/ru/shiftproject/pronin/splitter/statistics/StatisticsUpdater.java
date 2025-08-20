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

    public void update(DataType dataType, String line) {
        if (configuration.shouldPrintStatistics()) {
            if (!statisticsCollectorsByType.containsKey(dataType)) {
                statisticsCollectorsByType.put(
                        dataType,
                        StatisticsCollectorCreator.create(
                                dataType,
                                configuration
                        )
                );
            }

            statisticsCollectorsByType.get(dataType).collect(line);
        }
    }
}