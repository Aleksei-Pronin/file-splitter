package ru.shiftproject.pronin.splitter.statistics;

import ru.shiftproject.pronin.splitter.config.Configuration;
import ru.shiftproject.pronin.splitter.datatype.DataType;
import ru.shiftproject.pronin.splitter.statistics.collector.NumericStatisticsCollector;
import ru.shiftproject.pronin.splitter.statistics.collector.StatisticsCollector;
import ru.shiftproject.pronin.splitter.statistics.collector.StringStatisticsCollector;

public final class StatisticsCollectorCreator {
    private StatisticsCollectorCreator() {
    }

    public static StatisticsCollector create(DataType dataType, Configuration configuration) {
        return switch (dataType) {
            case INTEGERS -> new NumericStatisticsCollector(
                    configuration,
                    false
            );
            case FLOATS -> new NumericStatisticsCollector(
                    configuration,
                    true
            );
            case STRINGS -> new StringStatisticsCollector(
                    configuration
            );
        };
    }
}