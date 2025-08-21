package ru.shiftproject.pronin.splitter.error;

public class ConsoleErrorHandler implements ErrorHandler {
    @Override
    public int handleError(Exception e, ErrorCode errorCode) {
        String message = (e != null && e.getMessage() != null)
                ? e.getMessage()
                : "Неизвестная ошибка";

        System.err.println(errorCode.getDescription() + ": " + message);

        if (e instanceof IllegalArgumentException) {
            HelpPrinter.printHelp();
        }

        return errorCode.getExitCode();
    }
}