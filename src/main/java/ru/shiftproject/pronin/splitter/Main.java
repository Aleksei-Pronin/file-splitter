package ru.shiftproject.pronin.splitter;

import ru.shiftproject.pronin.splitter.config.Configuration;
import ru.shiftproject.pronin.splitter.error.ConsoleErrorHandler;
import ru.shiftproject.pronin.splitter.error.ErrorHandler;
import ru.shiftproject.pronin.splitter.processor.FileProcessor;
import ru.shiftproject.pronin.splitter.config.ArgumentParser;

import java.io.IOException;
import java.nio.file.FileSystemException;

public class Main {
    public static void main(String[] args) {
        try {
            Configuration configuration = new ArgumentParser().parseArgs(args);
            new FileProcessor().process(configuration);
        } catch (Exception e) {
            System.exit(handleException(e));
        }
    }

    private static int handleException(Exception e) {
        ErrorHandler errorHandler = new ConsoleErrorHandler();

        ErrorHandler.ErrorCode code = switch (e) {
            case IllegalArgumentException _ -> ErrorHandler.ErrorCode.ARGUMENTS_ERROR;
            case FileSystemException _ -> ErrorHandler.ErrorCode.FILE_SYSTEM_ERROR;
            case IOException _ -> ErrorHandler.ErrorCode.IO_ERROR;
            default -> ErrorHandler.ErrorCode.UNKNOWN_ERROR;
        };

        return errorHandler.handleError(e, code);
    }
}