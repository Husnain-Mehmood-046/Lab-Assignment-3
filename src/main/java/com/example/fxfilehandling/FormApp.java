package com.example.fxfilehandling;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

public class FormApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // GridPane for form inputs
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(15));

        // Adding form elements
        Label fullNameLabel = new Label("Full Name:");
        TextField fullNameField = new TextField();
        gridPane.add(fullNameLabel, 0, 0);
        gridPane.add(fullNameField, 1, 0);

        Label idLabel = new Label("ID:");
        TextField idField = new TextField();
        gridPane.add(idLabel, 0, 1);
        gridPane.add(idField, 1, 1);

        Label genderLabel = new Label("Gender:");
        // Replacing TextField with RadioButtons for gender
        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Male");
        RadioButton femaleRadio = new RadioButton("Female");
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(10, maleRadio, femaleRadio);
        gridPane.add(genderLabel, 0, 2);
        gridPane.add(genderBox, 1, 2);

        Label homeProvinceLabel = new Label("Home Province:");
        TextField homeProvinceField = new TextField();
        gridPane.add(homeProvinceLabel, 0, 3);
        gridPane.add(homeProvinceField, 1, 3);

        // Replacing TextField for DOB with DatePicker
        Label dobLabel = new Label("DOB:");
        DatePicker dobPicker = new DatePicker();
        gridPane.add(dobLabel, 0, 4);
        gridPane.add(dobPicker, 1, 4);

        // TextField for output of found record
        Label resultLabel = new Label("Found Record:");
        TextField resultField = new TextField();
        resultField.setEditable(false);
        gridPane.add(resultLabel, 0, 5);
        gridPane.add(resultField, 1, 5);

        // VBox for buttons on the right side
        VBox buttonBox = new VBox(10);
        buttonBox.setPadding(new Insets(15));
        buttonBox.setAlignment(Pos.TOP_LEFT);

        Button newButton = new Button("New");
        Button deleteButton = new Button("Delete");
        Button findButton = new Button("Find");
        Button closeButton = new Button("Close");

        buttonBox.getChildren().addAll(newButton, deleteButton, findButton, closeButton);

        // Add event for close button
        closeButton.setOnAction(e -> primaryStage.close());

        // Add event for New button
        newButton.setOnAction(e -> {
            String fullName = fullNameField.getText();
            String id = idField.getText();
            String gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();
            String homeProvince = homeProvinceField.getText();
            String dob = dobPicker.getValue() != null ? dobPicker.getValue().toString() : "";

            String record = fullName + "," + id + "," + gender + "," + homeProvince + "," + dob + "~";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true))) {
                writer.write(record);
                writer.newLine();
            } catch (IOException ex) {
                // Handle file write error
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving record: " + ex.getMessage());
                alert.showAndWait();
            }

            // Clear fields after saving
            fullNameField.clear();
            idField.clear();
            genderGroup.selectToggle(null);
            homeProvinceField.clear();
            dobPicker.setValue(null);
        });

        // Add event for Find button
        findButton.setOnAction(e -> {
            String searchId = idField.getText();
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Remove the "~" at the end of each record (if present) and split by comma
                    line = line.replace("~", "");
                    String[] fields = line.split(",");

                    if (fields.length > 1 && fields[1].equals(searchId)) {
                        // If the ID matches, show the full record in the result field
                        String record = "Full Name: " + fields[0] + ", Gender: " + fields[2] + ", Home Province: " + fields[3] + ", DOB: " + fields[4];
                        resultField.setText(record);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    resultField.setText("Record not found.");
                }
            } catch (IOException ex) {
                // Handle file read error
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error reading file: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        // HBox for main layout
        HBox mainLayout = new HBox(20);
        mainLayout.getChildren().addAll(gridPane, buttonBox);

        Scene scene = new Scene(mainLayout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Data Entry Form");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
