package com.momentum.dosein.utils;

import com.momentum.dosein.models.Doctor;
import com.momentum.dosein.models.Reminder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileManager {

    private static final String DATA_DIR       = "data";
    private static final String DOCTOR_FILE    = DATA_DIR + File.separator + "doctors.dat";
    private static final String REMINDER_FILE  = DATA_DIR + File.separator + "reminders.dat";

    // ——— Doctors ——————————————————————

    @SuppressWarnings("unchecked")
    public static List<Doctor> loadDoctors() {
        File f = new File(DOCTOR_FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                return (List<Doctor>) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void saveDoctors(List<Doctor> doctors) {
        ensureDataDir();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DOCTOR_FILE))) {
            out.writeObject(new ArrayList<>(doctors));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ——— Reminders ——————————————————————

    @SuppressWarnings("unchecked")
    public static List<Reminder> loadReminders() {
        File f = new File(REMINDER_FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                return (List<Reminder>) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void saveReminders(List<Reminder> reminders) {
        ensureDataDir();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(REMINDER_FILE))) {
            out.writeObject(new ArrayList<>(reminders));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Append a single reminder */
    public static void saveReminder(Reminder r) {
        List<Reminder> list = loadReminders();
        list.add(r);
        saveReminders(list);
    }

    /** Remove all reminders matching a medicine name */
    public static void deleteRemindersByMedicineName(String medicineName) {
        List<Reminder> list = loadReminders()
                .stream()
                .filter(r -> !r.getMedicineName().equals(medicineName))
                .collect(Collectors.toList());
        saveReminders(list);
    }

    // ——— Helpers ——————————————————————

    private static void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
