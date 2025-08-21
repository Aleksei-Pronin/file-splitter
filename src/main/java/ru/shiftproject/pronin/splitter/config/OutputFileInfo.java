package ru.shiftproject.pronin.splitter.config;

import ru.shiftproject.pronin.splitter.datatype.DataType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class OutputFileInfo {
    private OutputFileInfo() {
    }

    public static Path resolveOutputFilePath(Configuration configuration, DataType dataType) {
        return configuration.outputDirectory()
                .resolve(configuration.outputFilePrefix() + dataType.getOutputFileName())
                .normalize();
    }

    public static boolean needsNewLine(Configuration configuration, DataType dataType) throws IOException {
        Path outputFilePath = resolveOutputFilePath(configuration, dataType);

        return configuration.appendMode()
                && Files.exists(outputFilePath)
                && Files.size(outputFilePath) > 0;
    }
}