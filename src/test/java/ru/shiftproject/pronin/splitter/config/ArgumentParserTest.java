package ru.shiftproject.pronin.splitter.config;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentParserTest {
    private final ArgumentParser argumentParser = new ArgumentParser();

    private Path createTempFile(String name) throws IOException {
        Path path = Files.createTempFile(name, ".txt");
        path.toFile().deleteOnExit();
        return path;
    }

    private Path createTempDirectory(String name) throws IOException {
        Path dir = Files.createTempDirectory(name);
        dir.toFile().deleteOnExit();
        return dir;
    }

    @Test
    void shouldParseArgsWithFullStatsAndAppendMode() throws IOException {
        Path input1 = createTempFile("input1");
        Path input2 = createTempFile("input2");
        Path outputDirectory = createTempDirectory("res");

        Configuration configuration = argumentParser.parseArgs(new String[]{
                "-o", outputDirectory.toString(),
                "-p", "prefix_",
                "-f",
                "-a",
                input1.toString(), input2.toString()
        });

        assertEquals(outputDirectory, configuration.getOutputDirectory());
        assertEquals("prefix_", configuration.getOutputFilePrefix());
        assertFalse(configuration.isShortStatistics());
        assertTrue(configuration.isFullStatistics());
        assertTrue(configuration.isAppendMode());
        assertEquals(2, configuration.getInputFiles().size());
        assertTrue(configuration.getInputFiles().containsAll(Set.of(input1, input2)));
    }

    @Test
    void shouldParseArgsWithShortStatsAndUppercaseFlags() throws IOException {
        Path input1 = createTempFile("input1");
        Path input2 = createTempFile("input2");
        Path outputDirectory = createTempDirectory("res");

        Configuration configuration = argumentParser.parseArgs(new String[]{
                "-O", outputDirectory.toString(),
                "-P", "prefix_",
                "-S",
                input1.toString(), input2.toString()
        });

        assertEquals(outputDirectory, configuration.getOutputDirectory());
        assertEquals("prefix_", configuration.getOutputFilePrefix());
        assertTrue(configuration.isShortStatistics());
        assertFalse(configuration.isFullStatistics());
        assertFalse(configuration.isAppendMode());
        assertEquals(2, configuration.getInputFiles().size());
        assertTrue(configuration.getInputFiles().containsAll(Set.of(input1, input2)));
    }

    @Test
    void shouldCreateOutputDirectoryIfItDoesNotExist() throws IOException {
        Path input = createTempFile("input");

        Path tempDirectory = createTempDirectory("temp");
        Path newOutputDir = tempDirectory.resolve("newOutputDir");

        assertFalse(Files.exists(newOutputDir));

        Configuration configuration = argumentParser.parseArgs(new String[]{
                "-o", newOutputDir.toString(),
                input.toString()
        });

        assertTrue(Files.exists(newOutputDir));
        assertTrue(Files.isDirectory(newOutputDir));
        assertEquals(newOutputDir, configuration.getOutputDirectory());
        assertEquals(1, configuration.getInputFiles().size());
        assertTrue(configuration.getInputFiles().contains(input));
    }

    @Test
    void shouldThrowExceptionWhenNoArgumentsProvided() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> argumentParser.parseArgs(new String[]{})
        );

        assertTrue(ex.getMessage().contains("не указаны аргументы"));
    }

    @Test
    void shouldThrowExceptionWhenUnknownOptionProvided() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> argumentParser.parseArgs(new String[]{
                        "-unknown",
                        "input.txt"
                })
        );

        assertTrue(ex.getMessage().contains("неизвестная опция"));
    }

    @Test
    void shouldThrowExceptionWhenOutputDirIsMissing() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> argumentParser.parseArgs(new String[]{
                        "-o",
                        "-p", "prefix_",
                        "input.txt"
                })
        );

        assertTrue(ex.getMessage().contains("требуется путь к выходной директории после -o"));
    }

    @Test
    void shouldThrowExceptionWhenOutputDirNameIsEmpty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> argumentParser.parseArgs(new String[]{
                        "-o", "   ",
                        "input.txt"
                })
        );

        assertTrue(ex.getMessage().contains("путь к выходной директории не может быть пустым"));
    }

    @Test
    void shouldThrowExceptionWhenOutputDirIsNotDir() throws IOException {
        Path tempFile = createTempFile("temp");
        Path input = createTempFile("input");

        NotDirectoryException ex = assertThrows(
                NotDirectoryException.class,
                () -> argumentParser.parseArgs(new String[]{
                        "-o", tempFile.toString(),
                        input.toString()
                })
        );

        assertTrue(ex.getMessage().contains("не является директорией"));
    }

    @Test
    void shouldThrowExceptionWhenFilePrefixIsMissing() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> argumentParser.parseArgs(new String[]{
                        "-p",
                        "-f",
                        "input.txt"
                })
        );

        assertTrue(ex.getMessage().contains("требуется префикс имени файла после -p"));
    }

    @Test
    void shouldThrowExceptionWhenFilePrefixIsEmpty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> argumentParser.parseArgs(new String[]{
                        "-p", "   ",
                        "input.txt"
                })
        );

        assertTrue(ex.getMessage().contains("префикс имени файла не может быть пустым"));
    }

    @Test
    void shouldThrowExceptionWhenNoInputFilesProvided() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> argumentParser.parseArgs(new String[]{
                        "-f",
                        "-a"
                })
        );

        assertTrue(ex.getMessage().contains("не указаны входные файлы"));
    }

    @Test
    void shouldThrowExceptionWhenInputFileNameIsEmpty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> argumentParser.parseArgs(new String[]{" "})
        );

        assertTrue(ex.getMessage().contains("имя входного файла не может быть пустым"));
    }

    @Test
    void shouldThrowExceptionWhenInputFileDoesNotExist() {
        NoSuchFileException ex = assertThrows(
                NoSuchFileException.class,
                () -> argumentParser.parseArgs(new String[]{"missing_input.txt"})
        );

        assertTrue(ex.getMessage().contains("входной файл не найден"));
    }

    @Test
    void shouldThrowExceptionWhenInputFileIsNotFile() throws IOException {
        Path outputDirectory = createTempDirectory("output");

        FileSystemException ex = assertThrows(
                FileSystemException.class,
                () -> argumentParser.parseArgs(new String[]{
                        outputDirectory.toString()
                })
        );

        assertTrue(ex.getMessage().contains("не является файлом"));
    }
}