package ru.shiftproject.pronin.splitter.config;

import java.io.IOException;
import java.nio.file.*;

public final class PathValidator {
    private PathValidator() {
    }

    static Path checkWritableDirectory(Path path) throws IOException {
        if (Files.exists(path) && !Files.isDirectory(path)) {
            throw new NotDirectoryException(
                    String.format("указанный путь существует, но не является директорией (%s)", path));
        }

        Files.createDirectories(path);

        if (!Files.isWritable(path)) {
            throw new AccessDeniedException(
                    String.format("нет прав на запись в директорию (%s)", path));
        }

        return path.toAbsolutePath().normalize();
    }

    static Path checkReadableFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new NoSuchFileException(
                    String.format("входной файл не найден (%s)", path)
            );
        }

        if (!Files.isRegularFile(path)) {
            throw new FileSystemException(
                    String.format("указанный путь существует, не является файлом (%s)", path)
            );
        }

        if (!Files.isReadable(path)) {
            throw new AccessDeniedException(
                    String.format("нет прав на чтение файла (%s)", path)
            );
        }

        return path.toAbsolutePath().normalize();
    }
}
