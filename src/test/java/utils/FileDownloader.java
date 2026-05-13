package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class FileDownloader {

    private FileDownloader() {
    }

    public static void download(String url, Path destination) throws IOException {
        Files.createDirectories(destination.getParent());
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static boolean isNonEmpty(Path file) {
        try {
            return Files.exists(file) && Files.size(file) > 0;
        } catch (IOException e) {
            return false;
        }
    }
}