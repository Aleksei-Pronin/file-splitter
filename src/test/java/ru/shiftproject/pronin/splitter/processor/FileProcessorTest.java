package ru.shiftproject.pronin.splitter.processor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shiftproject.pronin.splitter.config.ArgumentParser;
import ru.shiftproject.pronin.splitter.config.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FileProcessorTest {
    private final List<Path> inputFiles = new ArrayList<>();
    private final Map<String, Path> outputFiles = new HashMap<>();
    private Path outputDirectory;

    @BeforeEach
    void createOutputFiles() throws IOException {
        outputDirectory = Files.createTempDirectory("test-sorter-out-");

        outputFiles.put("integers", outputDirectory.resolve("test-integers.txt"));
        outputFiles.put("floats", outputDirectory.resolve("test-floats.txt"));
        outputFiles.put("strings", outputDirectory.resolve("test-strings.txt"));
    }

    @AfterEach
    void deleteTempFiles() throws IOException {
        for (Path inputFile : inputFiles) {
            Files.deleteIfExists(inputFile);
        }

        for (Path outputFile : outputFiles.values()) {
            Files.deleteIfExists(outputFile);
        }

        Files.deleteIfExists(outputDirectory);

        inputFiles.clear();
        outputFiles.clear();
    }

    private void createTempInputFile(List<String> lines) throws IOException {
        Path tempInputFile = Files.createTempFile("test-", ".txt");
        Files.write(tempInputFile, lines);
        inputFiles.add(tempInputFile);
    }

    private List<String> readLinesFromFileIfExists(Path file) throws IOException {
        return (file != null && Files.exists(file)) ? Files.readAllLines(file) : List.of();
    }

    private void processInputFiles(
            boolean appendMode,
            boolean shortStats,
            boolean fullStats
    ) throws IOException {
        List<String> args = new ArrayList<>();

        args.add("-o");
        args.add(outputDirectory.toString());
        args.add("-p");
        args.add("test-");

        if (appendMode) {
            args.add("-a");
        }

        if (shortStats) {
            args.add("-s");
        }

        if (fullStats) {
            args.add("-f");
        }

        for (Path file : inputFiles) {
            args.add(file.toString());
        }

        Configuration configuration = new ArgumentParser().parseArgs(args.toArray(new String[0]));

        new FileProcessor().process(configuration);
    }

    private void processInputFiles(boolean appendMode) throws IOException {
        processInputFiles(appendMode, false, false);
    }

    private void processInputFiles() throws IOException {
        processInputFiles(false);
    }

    private String processInputFilesAndCaptureStatistics(
            boolean shortStats,
            boolean fullStats
    ) throws IOException {
        ByteArrayOutputStream statistics = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(statistics));

        try {
            processInputFiles(false, shortStats, fullStats);
            return statistics.toString();
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void shouldDistributeLinesByType() throws IOException {
        createTempInputFile(
                List.of("one", "1", "два", "1.23", "три четыре", "-0.0001")
        );

        createTempInputFile(
                List.of("5,5.5", "SIX", "+7.89E-10", "123,45", "6", ".7")
        );

        processInputFiles();

        assertEquals(
                List.of("1", "6"),
                readLinesFromFileIfExists(outputFiles.get("integers"))
        );

        assertEquals(
                List.of("1.23", "-0.0001", "+7.89E-10", "123,45", ".7"),
                readLinesFromFileIfExists(outputFiles.get("floats"))
        );

        assertEquals(
                List.of("one", "два", "три четыре", "5,5.5", "SIX"),
                readLinesFromFileIfExists(outputFiles.get("strings"))
        );
    }

    @Test
    void shouldAppendToFilesWhenAppendModeEnabled() throws IOException {
        createTempInputFile(List.of("1", "2", "three"));

        processInputFiles(true);

        assertEquals(
                List.of("1", "2"),
                readLinesFromFileIfExists(outputFiles.get("integers"))
        );
        assertFalse(
                Files.exists(outputFiles.get("floats"))
        );
        assertEquals(
                List.of("three"),
                readLinesFromFileIfExists(outputFiles.get("strings"))
        );

        processInputFiles(true);

        assertEquals(
                List.of("1", "2", "1", "2"),
                readLinesFromFileIfExists(outputFiles.get("integers"))
        );
        assertFalse(
                Files.exists(outputFiles.get("floats"))
        );
        assertEquals(
                List.of("three", "three"),
                readLinesFromFileIfExists(outputFiles.get("strings"))
        );
    }

    @Test
    void shouldOverwriteFilesWhenAppendModeDisabled() throws IOException {
        createTempInputFile(List.of("1", "     ", "2", "three", ""));

        processInputFiles();

        assertEquals(
                List.of("1", "2"),
                readLinesFromFileIfExists(outputFiles.get("integers"))
        );
        assertFalse(
                Files.exists(outputFiles.get("floats"))
        );
        assertEquals(
                List.of("three"),
                readLinesFromFileIfExists(outputFiles.get("strings"))
        );

        processInputFiles();

        assertEquals(
                List.of("1", "2"),
                readLinesFromFileIfExists(outputFiles.get("integers"))

        );
        assertFalse(
                Files.exists(outputFiles.get("floats"))
        );
        assertEquals(
                List.of("three"),
                readLinesFromFileIfExists(outputFiles.get("strings"))
        );
    }

    @Test
    void shouldPrintShortStatisticsWhenShortStatsEnabled() throws IOException {
        createTempInputFile(List.of("one", "1", "два", "1.23", "три четыре", "-0.0001"));
        createTempInputFile(List.of("5,5.5", "SIX", "+7.89E-10", "123,45", "6"));

        String statistics = processInputFilesAndCaptureStatistics(true, false);

        assertTrue(statistics.contains("Статистика для " + outputFiles.get("integers").getFileName()));
        assertTrue(statistics.contains("Статистика для " + outputFiles.get("floats").getFileName()));
        assertTrue(statistics.contains("Статистика для " + outputFiles.get("strings").getFileName()));

        assertTrue(statistics.contains("Количество элементов: 2"));
        assertTrue(statistics.contains("Количество элементов: 4"));
        assertTrue(statistics.contains("Количество элементов: 5"));

        assertFalse(statistics.contains("Дополнительная статистика:"));

        assertFalse(statistics.contains("Минимальное значение:"));
        assertFalse(statistics.contains("Максимальное значение:"));
        assertFalse(statistics.contains("Сумма:"));
        assertFalse(statistics.contains("Среднее значение:"));

        assertFalse(statistics.contains("Минимальная длина:"));
        assertFalse(statistics.contains("Максимальная длина:"));
    }

    @Test
    void shouldPrintFullStatisticsWhenFullStatsEnabled() throws IOException {
        createTempInputFile(List.of("one", "1", "два", "1.23", "три четыре", "-0.0001"));
        createTempInputFile(List.of("5,5.5", "SIX", "+7.89E-10", "123,45", "6"));

        String statistics = processInputFilesAndCaptureStatistics(false, true);

        assertTrue(statistics.contains("Статистика для " + outputFiles.get("integers").getFileName()));
        assertTrue(statistics.contains("Статистика для " + outputFiles.get("floats").getFileName()));
        assertTrue(statistics.contains("Статистика для " + outputFiles.get("strings").getFileName()));

        assertTrue(statistics.contains("Количество элементов: 2"));
        assertTrue(statistics.contains("Количество элементов: 4"));
        assertTrue(statistics.contains("Количество элементов: 5"));

        assertTrue(statistics.contains("Дополнительная статистика"));

        assertTrue(statistics.contains("Минимальное значение: 1"));
        assertTrue(statistics.contains("Максимальное значение: 6"));
        assertTrue(statistics.contains("Сумма: 7"));
        assertTrue(statistics.contains("Среднее значение: 3.5"));

        assertTrue(statistics.contains("Минимальное значение: -0.0001"));
        assertTrue(statistics.contains("Максимальное значение: 123,45"));
        assertTrue(statistics.contains("Сумма: 124.6799"));
        assertTrue(statistics.contains("Среднее значение: 31.16997"));

        assertTrue(statistics.contains("Минимальная длина: 3"));
        assertTrue(statistics.contains("Максимальная длина: 10"));
    }

    @Test
    void shouldNotPrintStatisticsWhenStatsFlagsDisabled() throws IOException {
        createTempInputFile(List.of("one", "1", "два", "1.23", "три четыре", "-0.0001"));
        createTempInputFile(List.of("5,5.5", "SIX", "+7.89E-10", "123,45", "6"));

        String statistics = processInputFilesAndCaptureStatistics(false, false);

        assertFalse(statistics.contains("Статистика для"));
    }
}