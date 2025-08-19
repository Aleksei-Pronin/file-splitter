package ru.shiftproject.pronin.splitter.statistics.collector;

import ru.shiftproject.pronin.splitter.config.Configuration;

public class StringStatisticsCollector extends StatisticsCollector {
    private int elementsCount;
    private int minimumLength = Integer.MAX_VALUE;
    private int maximumLength;

    public StringStatisticsCollector(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void process(String line) {
        if (!configuration.shouldPrintStatistics()) {
            return;
        }

        elementsCount++;

        if (configuration.isFullStatistics()) {
            int length = line.length();

            if (length < minimumLength) {
                minimumLength = length;
            }

            if (length > maximumLength) {
                maximumLength = length;
            }
        }
    }

    @Override
    public int getElementsCount() {
        return elementsCount;
    }

    public int getMinimumLength() {
        return elementsCount == 0 ? 0 : minimumLength;
    }

    public int getMaximumLength() {
        return maximumLength;
    }
}