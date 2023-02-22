package com.rusmansur.srgminstaller.controllers;

import com.rusmansur.srgminstaller.Mod;
import com.rusmansur.srgminstaller.ModsDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainController {
    @FXML public ListView<Mod> modsListView = new ListView<>();
    @FXML public ImageView modImage;
    @FXML public Label itemIDLabel;
    @FXML public Label ownerIDLabel;
    @FXML public Label modNameLabel;
    @FXML public Label modDescLabel;
    @FXML public TextField searchTextField;
    @FXML public Button pathsButton;
    @FXML public MenuItem openDirMenu;

    ModsDB modsDB = new ModsDB();
    ObservableList<Mod> observableList;
    MultipleSelectionModel<Mod> selectionModel;
    Mod selectedMod;

    @FXML
    void initialize() {
        ModsDB.getModsToList();
        modsDB.checkModsInGame();
        observableList = FXCollections.observableArrayList(ModsDB.getModList());
        modsListView.setItems(observableList.sorted((Comparator.comparing(Mod::getName)))); // сортировать названия по ворастанию
//        Создаёт CheckList из ListView
        modsListView.setCellFactory(CheckBoxListCell.forListView(Mod::selectedProperty, new StringConverter<>() {
            @Override public String toString(Mod mod) {
                return mod.getName();
            }

            @Override public Mod fromString(String s) {
                return selectedMod;
            }
        }));
//       Если мод отмечен, то добавляется игру, иначе удаляется из каталога игры
        observableList.forEach(mod -> mod.selectedProperty().addListener((observable, wasSelected, isSelected) -> {
            if (isSelected) {modsDB.moveMod(mod, true);}
            if (wasSelected) {modsDB.moveMod(mod, false);}
        }));
//        Интерактивный поиск: отображение результата при вводе текста в строке поиска
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<Mod> list = new ArrayList<>();
                for (Mod mod : ModsDB.getModList()) {
                    if (mod.getName().toLowerCase().contains(newValue)) {
                        list.add(mod);
                    }
                }
                modsListView.setItems(
                        FXCollections
                                .observableArrayList(list)
                                .sorted(Comparator.comparing(Mod::getName)));
            } else {
                modsListView.setItems(observableList);
            }
        });
    }

    public void onModsListClicked() {
        selectionModel = modsListView.getSelectionModel();
        selectedMod = selectionModel.getSelectedItem();
        if (this.selectedMod != null) {printModProperties();}
    }

    // Вывод данные о моде
    private void printModProperties() {
        modImage.setImage(selectedMod.getPreviewImage());
        itemIDLabel.setText(selectedMod.getPropertiesMap().get("$ITEM_ID"));
        ownerIDLabel.setText(selectedMod.getPropertiesMap().get("$OWNER_ID"));
        modNameLabel.setText(selectedMod.getName());
        modDescLabel.setText(selectedMod.getPropertiesMap().get("$ITEM_DESC"));
    }

    public void onPathsButtonClicked() {
        PathsSettingController.changePaths();
    }

    public void onOpenDirMenuClicked() {
        try {
            Desktop.getDesktop().open(new File(selectedMod.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}