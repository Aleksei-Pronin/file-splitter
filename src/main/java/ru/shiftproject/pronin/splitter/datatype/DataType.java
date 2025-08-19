package ru.shiftproject.pronin.splitter.datatype;

import java.util.regex.Pattern;

public enum DataType {
    INTEGERS("integers.txt", "^[+-]?\\d+$"),
    FLOATS("floats.txt", "^[+-]?(?:\\d+[.,]\\d*|[.,]\\d+)(?:[eE][+-]?\\d+)?$"),
    STRINGS("strings.txt", ".*");

    private final String outputFileName;
    private final Pattern pattern;

    DataType(String outputFileName, String regex) {
        this.outputFileName = outputFileName;
        pattern = Pattern.compile(regex);
    }

    public boolean matches(String line) {
        return pattern.matcher(line).matches();
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public static DataType determineDataType(String line) {
        if (INTEGERS.matches(line)) {
            return INTEGERS;
        }

        if (FLOATS.matches(line)) {
            return FLOATS;
        }

        return STRINGS;
    }
}