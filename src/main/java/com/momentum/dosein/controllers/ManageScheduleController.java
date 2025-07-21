package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Reminder;
import com.momentum.dosein.utils.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ManageScheduleController implements Initializable {
    @FXML private VBox alertsContainer;
    @FXML private Label pillNameLabel;
    @FXML private Label startDateLabel;
    @FXML private Label endDateLabel;
    @FXML private Label additionalInfoLabel;
    @FXML private HBox timesContainer;

    private Map<String, List<Reminder>> groupedByMedicine;
    private String currentMedicine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Reminder> all = FileManager.loadReminders();
        groupedByMedicine = all.stream()
                .collect(Collectors.groupingBy(Reminder::getMedicineName));

        // Populate left pane
        groupedByMedicine.keySet().forEach(name -> {
            Label lbl = new Label(name);
            lbl.getStyleClass().add("alert-item");
            lbl.setOnMouseClicked(e -> selectMedicine(name));
            alertsContainer.getChildren().add(lbl);
        });

        // Auto-select first
        if (!groupedByMedicine.isEmpty()) {
            selectMedicine(groupedByMedicine.keySet().iterator().next());
        }
    }

    private void selectMedicine(String medicine) {
        currentMedicine = medicine;

        // Highlight selection
        alertsContainer.getChildren().forEach(n ->
                n.getStyleClass().remove("alert-selected")
        );
        alertsContainer.getChildren().stream()
                .filter(n -> ((Label)n).getText().equals(medicine))
                .forEach(n -> n.getStyleClass().add("alert-selected"));

        // Show details
        List<Reminder> list = groupedByMedicine.get(medicine);
        Reminder first = list.get(0);

        pillNameLabel.setText(first.getMedicineName());
        startDateLabel.setText(first.getStartDate());
        endDateLabel.setText(first.getEndDate());
        additionalInfoLabel.setText(
                first.getAdditional() == null ? "" : first.getAdditional()
        );

        timesContainer.getChildren().clear();
        list.forEach(r -> {
            Button b = new Button(r.getTime());
            b.getStyleClass().add("time-card");
            timesContainer.getChildren().add(b);
        });
    }

    @FXML
    private void handleBack(ActionEvent e) throws IOException {
        Stage st = (Stage)((Node)e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/momentum/dosein/views/dashboard.fxml")
        );
        st.setScene(new Scene(root, 800, 500));
    }

    @FXML
    private void handleDelete(ActionEvent e) throws IOException {
        if (currentMedicine != null) {
            FileManager.deleteRemindersByMedicineName(currentMedicine);
            handleBack(e);
        }
    }
}
