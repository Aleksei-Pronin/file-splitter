package ru.shiftproject.pronin.splitter.processor;

import ru.shiftproject.pronin.splitter.config.Configuration;
import ru.shiftproject.pronin.splitter.config.OutputFileInfo;
import ru.shiftproject.pronin.splitter.datatype.DataType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class FileLineWriter implements AutoCloseable {
    private final Configuration configuration;
    private final Map<DataType, BufferedWriter> writersByType = new EnumMap<>(DataType.class);
    private final Map<DataType, Boolean> needsNewLineByType = new EnumMap<>(DataType.class);

    public FileLineWriter(Configuration configuration) {
        this.configuration = configuration;
    }

    public void write(String line) throws IOException {
        DataType dataType = DataType.determineDataType(line);

        if (!writersByType.containsKey(dataType)) {
            writersByType.put(
                    dataType,
                    createWriter(
                            OutputFileInfo.resolveOutputFilePath(
                                    configuration,
                                    dataType
                            )
                    )
            );
            needsNewLineByType.put(
                    dataType,
                    OutputFileInfo.needsNewLine(
                            configuration,
                            dataType
                    )
            );
        }

        BufferedWriter writer = writersByType.get(dataType);

        if (needsNewLineByType.get(dataType)) {
            writer.newLine();
        }

        writer.write(line);
        needsNewLineByType.put(dataType, true);
    }

    private BufferedWriter createWriter(Path outputFile) throws IOException {
        return new BufferedWriter(
                new FileWriter(
                        outputFile.toFile(),
                        configuration.isAppendMode()
                )
        );
    }

    @Override
    public void close() throws IOException {
        for (BufferedWriter writer : writersByType.values()) {
            if (writer != null) {
                writer.close();
            }
        }
    }
}