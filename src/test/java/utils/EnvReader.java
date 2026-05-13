package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EnvReader {
    private final Map<String, String> values;

    private EnvReader(Map<String, String> values) {
        this.values = values;
    }

    public static EnvReader load(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            Map<String, String> values = new HashMap<>();
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int equalsIndex = trimmed.indexOf('=');
                if (equalsIndex < 0) {
                    continue;
                }
                String key = trimmed.substring(0, equalsIndex).trim();
                String value = trimmed.substring(equalsIndex + 1).trim();
                values.put(key, value);
            }
            return new EnvReader(values);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read .env file from " + path, e);
        }
    }

    public String get(String key) {
        return values.get(key);
    }
}
