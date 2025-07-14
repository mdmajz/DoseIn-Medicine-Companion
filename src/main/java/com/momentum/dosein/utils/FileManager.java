package com.momentum.dosein.utils;

import com.momentum.dosein.models.User;
import com.momentum.dosein.models.Reminder;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

public class FileManager {
    private static final Path USER_FILE = Paths.get("users.dat");
    private static final Path REMINDER_FILE = Paths.get("reminders.dat");

    public static void saveUsers(List<User> users) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                Files.newOutputStream(USER_FILE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            out.writeObject(users);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() throws IOException, ClassNotFoundException {
        if (!Files.exists(USER_FILE)) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(USER_FILE))) {
            return (List<User>) in.readObject();
        }
    }

    public static void saveReminders(List<Reminder> rems) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                Files.newOutputStream(REMINDER_FILE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            out.writeObject(rems);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Reminder> loadReminders() throws IOException, ClassNotFoundException {
        if (!Files.exists(REMINDER_FILE)) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(REMINDER_FILE))) {
            return (List<Reminder>) in.readObject();
        }
    }
}
