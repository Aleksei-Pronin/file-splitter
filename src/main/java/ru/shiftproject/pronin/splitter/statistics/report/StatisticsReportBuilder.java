package ru.shiftproject.pronin.splitter.statistics.report;

import ru.shiftproject.pronin.splitter.statistics.collector.NumericStatisticsCollector;
import ru.shiftproject.pronin.splitter.statistics.collector.StatisticsCollector;
import ru.shiftproject.pronin.splitter.statistics.collector.StringStatisticsCollector;

public class StatisticsReportBuilder {
    private static final String SEPARATOR = System.lineSeparator();

    public static String build(StatisticsCollector statisticsCollector) {
        return switch (statisticsCollector) {
            case NumericStatisticsCollector numericStatistics -> buildNumericStatistics(numericStatistics);
            case StringStatisticsCollector stringStatistics -> buildStringStatistics(stringStatistics);
            default -> throw new IllegalStateException(
                    "Не поддерживаемый тип коллектора: " + statisticsCollector
            );
        };
    }

    private static String buildNumericStatistics(NumericStatisticsCollector statisticsCollector) {
        StringBuilder stringBuilder = startBuilding(statisticsCollector.getElementsCount());

        if (statisticsCollector.isFullStatistics()) {
            appendAdditionalStatsTitle(stringBuilder);

            stringBuilder.append("\t\t\tМинимальное значение: ")
                    .append(statisticsCollector.getMinimumOriginalLine())
                    .append(SEPARATOR)
                    .append("\t\t\tМаксимальное значение: ")
                    .append(statisticsCollector.getMaximumOriginalLine())
                    .append(SEPARATOR)
                    .append("\t\t\tСумма: ")
                    .append(statisticsCollector.getTotalSum())
                    .append(SEPARATOR)
                    .append("\t\t\tСреднее значение: ")
                    .append(statisticsCollector.calculateAverage());
        }

        return stringBuilder.toString();
    }

    private static String buildStringStatistics(StringStatisticsCollector statisticsCollector) {
        StringBuilder stringBuilder = startBuilding(statisticsCollector.getElementsCount());

        if (statisticsCollector.isFullStatistics()) {
            appendAdditionalStatsTitle(stringBuilder);

            stringBuilder.append("\t\t\tМинимальная длина: ")
                    .append(statisticsCollector.getMinimumLength())
                    .append(SEPARATOR)
                    .append("\t\t\tМаксимальная длина: ")
                    .append(statisticsCollector.getMaximumLength());
        }

        return stringBuilder.toString();
    }

    private static StringBuilder startBuilding(int elementsCount) {
        return new StringBuilder("\tКоличество элементов: ")
                .append(elementsCount);
    }

    private static void appendAdditionalStatsTitle(StringBuilder builder) {
        builder.append(SEPARATOR)
                .append("\t\tДополнительная статистика")
                .append(SEPARATOR);
    }
}
