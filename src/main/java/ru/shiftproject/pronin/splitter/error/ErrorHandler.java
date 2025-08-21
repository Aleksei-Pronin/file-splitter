package ru.shiftproject.pronin.splitter.error;

public interface ErrorHandler {
    int handleError(Exception e, ErrorCode errorCode);

    enum ErrorCode {
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
}