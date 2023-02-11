package au.levelupcars.admin.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

public class Settings {

    private final HashMap<String, String> settings;

    public Settings() throws IOException {
        File file = new File("admin.settings");

        if (!file.exists()) {
            Files.copy(
                Objects.requireNonNull(Settings.class.getResourceAsStream("/admin.settings")),
                Paths.get("admin.settings"),
                StandardCopyOption.REPLACE_EXISTING
            );
        }

        settings = new HashMap<>();

        try (Stream<String> lines = Files.lines(file.toPath())) {
            lines.forEach(line -> {
                String[] setting = line.split("=");

                if (setting.length != 2) return;

                settings.put(setting[0], setting[1]);
            });
        }

        String[] requiredSettings = {"host", "port", "username", "password", "database"};
        for (String setting : requiredSettings) {
            if (!settings.containsKey(setting))
                throw new IOException(String.format("Required setting '%s' not found", setting));
        }
    }

    public String getHost() {
        return settings.get("host");
    }

    public String getPort() {
        return settings.get("port");
    }

    public String getUsername() {
        return settings.get("username");
    }

    public String getPassword() {
        return settings.get("password");
    }

    public String getDatabase() {
        return settings.get("database");
    }
}
