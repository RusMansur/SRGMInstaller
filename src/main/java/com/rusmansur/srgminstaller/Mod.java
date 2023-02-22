package com.rusmansur.srgminstaller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;

import java.util.Map;

public class Mod {
    private final String id;
    private final String name;
    private final Image previewImage;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private String path;
    private Map<String, String> propertiesMap;

    public Mod(String id, String name, String path, Image previewImage, Map<String, String> propertiesMap) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.previewImage = previewImage;
        this.propertiesMap = propertiesMap;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {this.path = path;}

    public Image getPreviewImage() {
        return previewImage;
    }

    public Map<String, String> getPropertiesMap() {
        return propertiesMap;
    }

    public void setPropertiesMap(Map<String, String> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}
