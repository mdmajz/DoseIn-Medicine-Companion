package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Doctor;
import com.momentum.dosein.utils.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
// trying more
import java.io.IOException;
import java.util.List;

public class AddDoctorController {

    @FXML private TextField nameField;
    @FXML private TextField specialtyField;
    @FXML private TextField locationField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField additionalField;

    @FXML
    private void handleSave(ActionEvent evt) throws IOException {
        Doctor d = new Doctor(
                nameField.getText(),
                specialtyField.getText(),
                locationField.getText(),
                phoneField.getText(),
                emailField.getText(),
                additionalField.getText()
        );

        List<Doctor> list = FileManager.loadDoctors();
        list.add(d);
        FileManager.saveDoctors(list);

        // back to contact list
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        Parent contacts = FXMLLoader.load(getClass()
                .getResource("/com/momentum/dosein/views/doctor_contacts.fxml"));
        st.setScene(new Scene(contacts, 600, 400));
    }

    @FXML
    private void handleCancel(ActionEvent evt) throws IOException {
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        Parent contacts = FXMLLoader.load(getClass()
                .getResource("/com/momentum/dosein/views/doctor_contacts.fxml"));
        st.setScene(new Scene(contacts, 600, 400));
    }
}
