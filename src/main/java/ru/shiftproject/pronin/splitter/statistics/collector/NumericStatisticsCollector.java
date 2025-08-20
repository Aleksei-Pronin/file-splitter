package ru.shiftproject.pronin.splitter.statistics.collector;

import ru.shiftproject.pronin.splitter.config.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumericStatisticsCollector extends StatisticsCollector {
    private final boolean isFloatType;

    private BigDecimal totalSum = BigDecimal.ZERO;

    private BigDecimal minimumValue;
    private String minimumOriginalLine;

    private BigDecimal maximumValue;
    private String maximumOriginalLine;

    public NumericStatisticsCollector(Configuration configuration, boolean isFloatType) {
        super(configuration);
        this.isFloatType = isFloatType;
    }

    @Override
    protected void collectAdditionalStatistics(String line) {
        final BigDecimal value = isFloatType
                ? new BigDecimal(line.trim().replace(',', '.'))
                : new BigDecimal(line.trim());

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

    @Override
    protected void appendAdditionalStatistics(StringBuilder stringBuilder) {
        stringBuilder.append("\t\t\tМинимальное значение: ")
                .append(minimumOriginalLine)
                .append(SEPARATOR)
                .append("\t\t\tМаксимальное значение: ")
                .append(maximumOriginalLine)
                .append(SEPARATOR)
                .append("\t\t\tСумма: ")
                .append(totalSum)
                .append(SEPARATOR)
                .append("\t\t\tСреднее значение: ")
                .append(calculateAverage());
    }

    private BigDecimal calculateAverage() {
        return totalSum.divide(BigDecimal.valueOf(elementsCount), 10, RoundingMode.HALF_UP)
                .stripTrailingZeros();
    }
}