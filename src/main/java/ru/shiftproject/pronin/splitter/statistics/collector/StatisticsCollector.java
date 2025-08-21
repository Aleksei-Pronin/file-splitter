package ru.shiftproject.pronin.splitter.statistics.collector;

import ru.shiftproject.pronin.splitter.config.Configuration;

public abstract class StatisticsCollector {
    protected static final String SEPARATOR = System.lineSeparator();

    protected final Configuration configuration;
    protected int elementsCount;

    protected StatisticsCollector(Configuration configuration) {
        this.configuration = configuration;
    }

    public void collect(String line) {
        elementsCount++;

        if (configuration.fullStatistics()) {
            collectAdditionalStatistics(line);
        }
    }

    public String buildStatistics() {
        StringBuilder stringBuilder = startBuilding(elementsCount);

        if (configuration.fullStatistics()) {
            appendAdditionalStatisticsTitle(stringBuilder);
            appendAdditionalStatistics(stringBuilder);
        }

        return stringBuilder.toString();
    }

    protected abstract void collectAdditionalStatistics(String line);

    protected abstract void appendAdditionalStatistics(StringBuilder stringBuilder);

    protected StringBuilder startBuilding(int elementsCount) {
        return new StringBuilder("\tКоличество элементов: ")
                .append(elementsCount);
    }

    protected void appendAdditionalStatisticsTitle(StringBuilder stringBuilder) {
        stringBuilder.append(SEPARATOR)
                .append("\t\tДополнительная статистика")
                .append(SEPARATOR);
    }
}