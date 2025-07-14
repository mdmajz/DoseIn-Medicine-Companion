package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Reminder;
import com.momentum.dosein.utils.FileManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardController {

    @FXML private Label usernameLabel;
    @FXML private Label clockLabel;
    @FXML private ListView<Reminder> reminderListView;

    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("hh:mm a");
    private ObservableList<Reminder> masterReminders;
    private FilteredList<Reminder> filteredReminders;

    @FXML
    public void initialize() {
        // 1) Show username
        usernameLabel.setText("user"); // TODO: replace with actual logged-in username

        // 2) Load all reminders from disk
        try {
            List<Reminder> loaded = FileManager.loadReminders();
            masterReminders = FXCollections.observableArrayList(loaded);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            masterReminders = FXCollections.observableArrayList();
        }

        // 3) Wrap in a filtered list (will remove past times)
        filteredReminders = new FilteredList<>(masterReminders, r -> true);

        // 4) Wire to the ListView & define how each cell is displayed
        reminderListView.setItems(filteredReminders);
        reminderListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Reminder item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTime().format(timeFmt)
                            + "    " + item.getMedicine()
                            + "\n" + item.getNotes());
                }
            }
        });

        // 5) Start the clock + filtering
        startClock();
    }

    private void startClock() {
        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    LocalTime now = LocalTime.now();
                    clockLabel.setText(now.format(timeFmt));
                    // filter out any reminders before 'now'
                    filteredReminders.setPredicate(r -> !r.getTime().isBefore(now));
                }),
                new KeyFrame(Duration.seconds(30))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    @FXML
    private void handleSetReminder(ActionEvent e) {
        try {
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/momentum/dosein/views/set_reminder.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/com/momentum/dosein/css/style.css")
                            .toExternalForm());
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleManage(ActionEvent e) {
        System.out.println("➜ Manage Reminders");
        // TODO: load Manage view
    }

    @FXML
    private void handleDoctors(ActionEvent e) {
        System.out.println("➜ View Doctors");
        // TODO: load Doctors view
    }

    @FXML
    private void handleEmergency(ActionEvent e) {
        System.out.println("➜ Emergency Contacts");
        // TODO: load Emergency view
    }

    @FXML
    private void handleExit(ActionEvent e) {
        Stage s = (Stage)((Node)e.getSource()).getScene().getWindow();
        s.close();
    }
}
