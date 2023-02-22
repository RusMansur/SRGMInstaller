package com.rusmansur.srgminstaller.controllers;

import com.rusmansur.srgminstaller.ModsDB;
import com.rusmansur.srgminstaller.PropertiesFile;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class PathsSettingController extends MainController {
    @FXML public TextField pathGameTextField = new TextField();
    @FXML public TextField pathBaseTextField = new TextField();
    @FXML public Button cancelButton;
    @FXML public Button applyButton;
    @FXML public Button gamePathButton;
    @FXML public Button basePathButton;
    static Stage modalStage = new Stage();

    @FXML
    void initialize() {
        pathGameTextField.setText(ModsDB.getGameModsPath());
        pathBaseTextField.setText(ModsDB.getBaseModsPath());
        FXMLLoader loader = new FXMLLoader(PathsSettingController.class.getClassLoader().getResource("PathsSettingWindow.fxml"));
        try {
            DialogPane dialogPane = loader.load();
            Scene scene = new Scene(dialogPane, 600, 100);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public static void changePaths() {
        modalStage.showAndWait();
    }

    public void onCancelButtonClicked() {
        modalStage = (Stage) cancelButton.getScene().getWindow();
        modalStage.close();
    }

    public void onApplyButtonClicked() {
        PropertiesFile.savePropertiesToFile(pathGameTextField.getText(), pathBaseTextField.getText());
        modalStage = (Stage) applyButton.getScene().getWindow();
        modalStage.close();
        ModsDB.getModsToList();
        modsListView.setItems(FXCollections.observableArrayList(ModsDB.getModList()));
    }

    public void onGamePathButtonClicked() {
        setPath("toGame");
    }

    public void onBasePathButtonClicked() {
        setPath("toBase");
    }

    private void setPath(String option) {
        DirectoryChooser dialog = new DirectoryChooser();
        dialog.setInitialDirectory(new File("C:\\"));
        File result = dialog.showDialog(modalStage);
        if (result != null) {
            if (option.equals("toGame")) {
                pathGameTextField.setText(result.getPath());
            } else if (option.equals("toBase")) {
                pathBaseTextField.setText(result.getPath());
            }
        }
        ModsDB.getModsToList();
    }
}

