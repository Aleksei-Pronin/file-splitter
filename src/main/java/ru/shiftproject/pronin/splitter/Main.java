package ru.shiftproject.pronin.splitter;

import ru.shiftproject.pronin.splitter.config.Configuration;
import ru.shiftproject.pronin.splitter.error.ErrorHandler;
import ru.shiftproject.pronin.splitter.processor.FileProcessor;
import ru.shiftproject.pronin.splitter.config.ArgumentParser;

import java.io.IOException;
import java.nio.file.FileSystemException;

public class Main {
    public static void main(String[] args) {
        try {
            Configuration configuration = new ArgumentParser().parseArgs(args);
            FileProcessor.process(configuration);
        } catch (IllegalArgumentException e) {
            ErrorHandler.handleError(e, ErrorHandler.ErrorCode.ARGUMENTS_ERROR);
        } catch (FileSystemException e) {
            ErrorHandler.handleError(e, ErrorHandler.ErrorCode.FILE_SYSTEM_ERROR);
        } catch (IOException e) {
            ErrorHandler.handleError(e, ErrorHandler.ErrorCode.IO_ERROR);
        } catch (Exception e) {
            ErrorHandler.handleError(e, ErrorHandler.ErrorCode.UNKNOWN_ERROR);
        }
    }
}