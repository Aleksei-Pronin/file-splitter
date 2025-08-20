package ru.shiftproject.pronin.splitter.config;

import java.io.IOException;
import java.nio.file.*;

public class ArgumentParser {
    private final Configuration configuration = new Configuration();

    public Configuration parseArgs(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException(
                    "не указаны аргументы, укажите хотя бы один входной файл"
            );
        }

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg.toLowerCase()) {
                case "-o" -> i = handleOutputPath(args, i);
                case "-p" -> i = handlePrefix(args, i);
                case "-a" -> configuration.enableAppendMode();
                case "-s" -> configuration.enableShortStatistics();
                case "-f" -> configuration.enableFullStatistics();
                default -> {
                    if (arg.startsWith("-")) {
                        throw new IllegalArgumentException(
                                String.format("неизвестная опция (%s)", arg)
                        );
                    }

                    handleInputFile(arg);
                }
            }
        }

        if (configuration.getInputFiles().isEmpty()) {
            throw new IllegalArgumentException(
                    "не указаны входные файлы"
            );
        }

        return configuration;
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

    private int handleOutputPath(String[] args, int index) throws IOException {
        Path outputPath = Paths.get(
                getNextArgument(args, index, "-o", "путь к выходной директории")
        );

        if (Files.exists(outputPath) && !Files.isDirectory(outputPath)) {
            throw new NotDirectoryException(
                    String.format("указанный путь существует, но не является директорией (%s)", outputPath)
            );
        }

        Files.createDirectories(outputPath);

        if (!Files.isWritable(outputPath)) {
            throw new AccessDeniedException(
                    String.format("нет прав на запись в директорию (%s)", outputPath)
            );
        }

        configuration.setOutputDirectory(outputPath);
        return index + 1;
    }

    private int handlePrefix(String[] args, int index) {
        configuration.setOutputFilePrefix(
                getNextArgument(args, index, "-p", "префикс имени файла")
        );
        return index + 1;
    }

    private void handleInputFile(String filePath) throws IOException {
        if (filePath.isBlank()) {
            throw new IllegalArgumentException(
                    "имя входного файла не может быть пустым"
            );
        }

        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new NoSuchFileException(
                    String.format("входной файл не найден (%s)", filePath)
            );
        }

        if (!Files.isRegularFile(path)) {
            throw new FileSystemException(
                    String.format("указанный путь существует, не является файлом (%s)", filePath)
            );
        }

        if (!Files.isReadable(path)) {
            throw new AccessDeniedException(
                    String.format("нет прав на чтение файла (%s)", filePath)
            );
        }

        configuration.addInputFile(path);
    }
}