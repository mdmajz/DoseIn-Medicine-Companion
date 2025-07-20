package com.momentum.dosein.utils;

import com.momentum.dosein.models.Reminder;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileManager {
    private static final Path DATA_DIR       = Paths.get("data");
    private static final Path REMINDERS_FILE = DATA_DIR.resolve("reminders.txt");

    /** Load reminders as 5‐part records */
    public static List<Reminder> loadReminders() {
        List<Reminder> list = new ArrayList<>();
        if (!Files.exists(REMINDERS_FILE)) return list;
        try {
            for (String line : Files.readAllLines(REMINDERS_FILE)) {
                String[] p = line.split("\\|", 5);
                if (p.length == 5) {
                    list.add(new Reminder(p[0], p[1], p[2], p[3], p[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Save one Reminder as time|pill|info|start|end */
    public static void saveReminder(Reminder r) {
        try {
            if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
            String entry = String.join("|",
                    r.getTime(),
                    r.getPillName(),
                    r.getAdditionalInfo(),
                    r.getStartDate(),
                    r.getEndDate()
            ) + System.lineSeparator();
            Files.write(REMINDERS_FILE,
                    entry.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Delete all reminders matching a description */
    public static void deleteRemindersByDescription(String description) {
        try {
            if (!Files.exists(REMINDERS_FILE)) return;
            List<String> kept = Files.readAllLines(REMINDERS_FILE).stream()
                    // split and check field #2 (pillName)
                    .filter(line -> {
                        String[] p = line.split("\\|", 5);
                        return p.length==5 && ! ( (p[1] + (p[2].isEmpty()?"":" – "+p[2])).equals(description) );
                    })
                    .collect(Collectors.toList());
            Files.write(REMINDERS_FILE, kept);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
