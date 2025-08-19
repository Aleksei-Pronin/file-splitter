package ru.shiftproject.pronin.splitter.error;

public class ErrorHandler {
    public enum ErrorCode {
        ARGUMENTS_ERROR(1, "Ошибка параметров"),
        FILE_SYSTEM_ERROR(2, "Ошибка работы с файловой системой"),
        IO_ERROR(3, "Ошибка ввода-вывода"),
        UNKNOWN_ERROR(4, "Неожиданная ошибка");

        private final int exitCode;
        private final String description;

        ErrorCode(int exitCode, String description) {
            this.exitCode = exitCode;
            this.description = description;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getDescription() {
            return description;
        }
    }

    public static void handleError(Exception e, ErrorCode errorCode) {
        String message = (e != null && e.getMessage() != null)
                ? e.getMessage()
                : "Неизвестная ошибка";
        System.err.println(errorCode.getDescription() + ": " + message);

        if (e instanceof IllegalArgumentException) {
            HelpPrinter.printHelp();
        }

        System.exit(errorCode.getExitCode());
    }
}