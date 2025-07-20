package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Doctor;
import com.momentum.dosein.utils.FileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorContactsController implements Initializable {

    @FXML private ListView<Doctor> doctorList;
    @FXML private Label nameLabel;
    @FXML private Label specialtyLabel;
    @FXML private Label locationLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label additionalLabel;

    private ObservableList<Doctor> doctors;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Doctor> loaded = FileManager.loadDoctors();
        doctors = FXCollections.observableArrayList(loaded);
        doctorList.setItems(doctors);

        doctorList.getSelectionModel().selectedItemProperty().addListener((obs, oldD, newD) -> {
            if (newD != null) {
                nameLabel.setText(newD.getName());
                specialtyLabel.setText(newD.getSpecialty());
                locationLabel.setText(newD.getLocation());
                phoneLabel.setText(newD.getPhone());
                emailLabel.setText(newD.getEmail());
                additionalLabel.setText(newD.getAdditional());
            }
        });
    }

    @FXML
    private void handleAdd(ActionEvent evt) throws IOException {
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        Parent add = FXMLLoader.load(getClass()
                .getResource("/com/momentum/dosein/views/add_doctor.fxml"));
        st.setScene(new Scene(add, 600, 400));
    }

    @FXML
    private void handleDelete(ActionEvent evt) {
        Doctor sel = doctorList.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Please select a doctor first.");
            a.setHeaderText(null);
            a.showAndWait();
            return;
        }
        doctors.remove(sel);
        FileManager.saveDoctors(doctors);
    }

    @FXML
    private void handleBack(ActionEvent evt) throws IOException {
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        Parent dash = FXMLLoader.load(getClass()
                .getResource("/com/momentum/dosein/views/dashboard.fxml"));
        st.setScene(new Scene(dash, 600, 400));
    }
}
