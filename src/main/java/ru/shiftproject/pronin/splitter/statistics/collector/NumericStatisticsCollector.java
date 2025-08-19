package ru.shiftproject.pronin.splitter.statistics.collector;

import ru.shiftproject.pronin.splitter.config.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumericStatisticsCollector extends StatisticsCollector {
    public enum NumericType {
        INTEGERS,
        FLOATS
    }

    private final NumericType numericType;

    private int elementsCount;
    private BigDecimal totalSum = BigDecimal.ZERO;

    private BigDecimal minimumValue;
    private String minimumOriginalLine;

    private BigDecimal maximumValue;
    private String maximumOriginalLine;

    public NumericStatisticsCollector(Configuration configuration, NumericType numericType) {
        super(configuration);
        this.numericType = numericType;
    }

    @Override
    public void process(String line) {
        if (!configuration.shouldPrintStatistics()) {
            return;
        }

        final BigDecimal value;

        try {
            value = (numericType == NumericType.FLOATS)
                    ? new BigDecimal(line.trim().replace(',', '.'))
                    : new BigDecimal(line.trim());
        } catch (NumberFormatException e) {
            System.err.printf("Пропущена некорректная числовая строка (%s)%n", line);
            return;
        }

        elementsCount++;

        if (configuration.isFullStatistics()) {
            totalSum = totalSum.add(value);

            if (minimumValue == null || value.compareTo(minimumValue) < 0) {
                minimumValue = value;
                minimumOriginalLine = line;
            }

            if (maximumValue == null || value.compareTo(maximumValue) > 0) {
                maximumValue = value;
                maximumOriginalLine = line;
            }
        }
    }

    @Override
    public int getElementsCount() {
        return elementsCount;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public BigDecimal calculateAverage() {
        return elementsCount == 0
                ? BigDecimal.ZERO
                : totalSum.divide(BigDecimal.valueOf(elementsCount), 10, RoundingMode.HALF_UP)
                .stripTrailingZeros();
    }

    public String getMinimumOriginalLine() {
        return minimumOriginalLine;
    }

    public String getMaximumOriginalLine() {
        return maximumOriginalLine;
    }
}