package ru.shiftproject.pronin.splitter.statistics.collector;

import ru.shiftproject.pronin.splitter.config.Configuration;

public abstract class StatisticsCollector {
    protected final Configuration configuration;

    protected StatisticsCollector(Configuration configuration) {
        this.configuration = configuration;
    }

    public abstract void process(String line);

    public abstract int getElementsCount();

    public boolean isFullStatistics() {
        return configuration.isFullStatistics();
    }
}