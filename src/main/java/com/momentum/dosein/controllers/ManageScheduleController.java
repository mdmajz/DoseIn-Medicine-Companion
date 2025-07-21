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
    @FXML private HBox timesContainer;


    @Override
        List<Reminder> all = FileManager.loadReminders();

            lbl.getStyleClass().add("alert-item");
            alertsContainer.getChildren().add(lbl);
        });
    }

        alertsContainer.getChildren().stream()
                .forEach(n -> n.getStyleClass().add("alert-selected"));

        Reminder first = list.get(0);
        startDateLabel.setText(first.getStartDate());
        endDateLabel.setText(first.getEndDate());

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
    }

    @FXML
    private void handleDelete(ActionEvent e) throws IOException {
            handleBack(e);
        }
    }
}
