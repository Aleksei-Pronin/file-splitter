package ru.shiftproject.pronin.splitter.statistics.collector;

import ru.shiftproject.pronin.splitter.config.Configuration;
import ru.shiftproject.pronin.splitter.datatype.DataType;

public class StatisticsCollectorCreator {
    public static StatisticsCollector create(DataType dataType, Configuration configuration) {
        return switch (dataType) {
            case INTEGERS -> new NumericStatisticsCollector(
                    configuration,
                    NumericStatisticsCollector.NumericType.INTEGERS
            );
            case FLOATS -> new NumericStatisticsCollector(
                    configuration,
                    NumericStatisticsCollector.NumericType.FLOATS
            );
            case STRINGS -> new StringStatisticsCollector(
                    configuration
            );
        };
    }
}