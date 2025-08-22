package ru.shiftproject.pronin.splitter.config;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ArgumentParser {
    public Configuration parseArgs(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException(
                    "не указаны аргументы, укажите хотя бы один входной файл"
            );
        }

        Path outputDirectory = Paths.get(".");
        String outputFilePrefix = "";
        boolean appendMode = false;
        boolean shortStatistics = false;
        boolean fullStatistics = false;
        Set<Path> inputFilesSet = new LinkedHashSet<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg.toLowerCase()) {
                case "-o" -> {
                    outputDirectory = parseOutputDirectory(args, i);
                    i++;
                }
                case "-p" -> {
                    outputFilePrefix = parsePrefix(args, i);
                    i++;
                }
                case "-a" -> appendMode = true;
                case "-s" -> shortStatistics = true;
                case "-f" -> fullStatistics = true;
                default -> {
                    if (arg.startsWith("-")) {
                        throw new IllegalArgumentException(
                                String.format("неизвестная опция (%s)", arg)
                        );
                    }

                    if (arg.isBlank()) {
                        throw new IllegalArgumentException(
                                "имя входного файла не может быть пустым"
                        );
                    }

                    inputFilesSet.add(PathValidator.ensureReadableFile(Path.of(arg)));
                }
            }
        }

        if (inputFilesSet.isEmpty()) {
            throw new IllegalArgumentException(
                    "не указаны входные файлы"
            );
        }

        if (shortStatistics && fullStatistics) {
            throw new IllegalArgumentException(
                    "опции -s и -f взаимоисключающие"
            );
        }

        List<Path> inputFiles = new ArrayList<>(inputFilesSet);

        return new Configuration(
                outputDirectory,
                outputFilePrefix,
                appendMode,
                shortStatistics,
                fullStatistics,
                inputFiles
        );
    }

    private String getNextArgument(String[] args, int index, String option, String argumentDescription) {
        int nextIndex = index + 1;

        if (nextIndex >= args.length || args[nextIndex].startsWith("-")) {
            throw new IllegalArgumentException(
                    String.format("требуется %s после %s", argumentDescription, option)
            );
        }

        if (args[nextIndex].isBlank()) {
            throw new IllegalArgumentException(
                    argumentDescription + " не может быть пустым"
            );
        }

        return args[nextIndex];
    }

    private Path parseOutputDirectory(String[] args, int index) throws IOException {
        return PathValidator.ensureWritableDirectory(
                Path.of(
                        getNextArgument(
                                args,
                                index,
                                "-o",
                                "путь к выходной директории"
                        )
                )
        );
    }

    private String parsePrefix(String[] args, int index) {
        return getNextArgument(args, index, "-p", "префикс имени файла");
    }
}