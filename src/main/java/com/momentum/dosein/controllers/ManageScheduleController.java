package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Reminder;
import com.momentum.dosein.utils.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
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
    @FXML private Label pillNameLabel, startDateLabel, endDateLabel, additionalInfoLabel;
    @FXML private HBox timesContainer;

    private Map<String, List<Reminder>> grouped;
    private String currentDesc;

    @Override
    public void initialize(URL loc, ResourceBundle res) {
        List<Reminder> all = FileManager.loadReminders();
        grouped = all.stream().collect(Collectors.groupingBy(Reminder::getDescription));

        // populate left
        grouped.keySet().forEach(desc -> {
            Label lbl = new Label(desc);
            lbl.getStyleClass().add("alert-item");
            lbl.setOnMouseClicked(e -> selectAlert(desc));
            alertsContainer.getChildren().add(lbl);
        });
        if (!grouped.isEmpty()) selectAlert(grouped.keySet().iterator().next());
    }

    private void selectAlert(String desc) {
        currentDesc = desc;
        // highlight
        alertsContainer.getChildren().forEach(n -> n.getStyleClass().remove("alert-selected"));
        alertsContainer.getChildren().stream()
                .filter(n -> ((Label)n).getText().equals(desc))
                .forEach(n -> n.getStyleClass().add("alert-selected"));

        List<Reminder> list = grouped.get(desc);
        Reminder first = list.get(0);
        pillNameLabel.setText(first.getPillName());
        startDateLabel.setText(first.getStartDate());
        endDateLabel.setText(first.getEndDate());
        additionalInfoLabel.setText(first.getAdditionalInfo());

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
        Parent root = FXMLLoader.load(getClass().getResource("/com/momentum/dosein/views/dashboard.fxml"));
        st.setScene(new Scene(root, 600, 400));
    }

    @FXML
    private void handleDelete(ActionEvent e) throws IOException {
        if (currentDesc != null) {
            FileManager.deleteRemindersByDescription(currentDesc);
            handleBack(e);
        }
    }
}
