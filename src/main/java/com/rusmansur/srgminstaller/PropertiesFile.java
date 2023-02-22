package com.rusmansur.srgminstaller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFile extends GetJarPath {
    static Properties properties = new Properties();
    static File PROPERTIESFILE = new File(GetJarPath.getPath() + "\\config.ini");

    public static void savePropertiesToFile(String gamePath, String basePath) {
        properties.setProperty("ModsInGamePath", gamePath);
        properties.setProperty("ModsInBasePath", basePath);
        try {
            properties.store(new FileWriter(PROPERTIESFILE), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties loadProperties() {
        try {
            if (!PROPERTIESFILE.exists()) {
                PROPERTIESFILE.createNewFile();
                savePropertiesToFile("C:\\Games\\Soviet Republic", "C:\\Mods");
                loadProperties();
            }
            properties.load(new FileReader(PROPERTIESFILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
