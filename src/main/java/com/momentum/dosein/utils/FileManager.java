package com.momentum.dosein.utils;

import com.momentum.dosein.models.Reminder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final Path DATA_DIR       = Paths.get("data");
    private static final Path REMINDERS_FILE = DATA_DIR.resolve("reminders.txt");

    /**
     * Load all reminders from data/reminders.txt
     * Each line is expected as "time|description"
     */
    public static List<Reminder> loadReminders() {
        List<Reminder> list = new ArrayList<>();
        if (!Files.exists(REMINDERS_FILE)) {
            return list;
        }
        try {
            for (String line : Files.readAllLines(REMINDERS_FILE)) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    list.add(new Reminder(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Append one reminder to data/reminders.txt
     * Writes a new line "time|description"
     */
    public static void saveReminder(Reminder r) {
        try {
            if (!Files.exists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
            String entry = r.getTime() + "|" + r.getDescription() + System.lineSeparator();
            Files.write(
                    REMINDERS_FILE,
                    entry.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
