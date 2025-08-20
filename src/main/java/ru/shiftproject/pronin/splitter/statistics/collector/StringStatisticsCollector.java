package ru.shiftproject.pronin.splitter.statistics.collector;

import ru.shiftproject.pronin.splitter.config.Configuration;

public class StringStatisticsCollector extends StatisticsCollector {
    private int minimumLength = Integer.MAX_VALUE;
    private int maximumLength;

    public StringStatisticsCollector(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected void collectAdditionalStatistics(String line) {
        int length = line.length();
        minimumLength = Math.min(minimumLength, length);
        maximumLength = Math.max(maximumLength, length);
    }

    @Override
    protected void appendAdditionalStatistics(StringBuilder stringBuilder) {
        stringBuilder.append("\t\t\tМинимальная длина: ")
                .append(minimumLength)
                .append(SEPARATOR)
                .append("\t\t\tМаксимальная длина: ")
                .append(maximumLength);
    }
}