package com.rusmansur.srgminstaller;

import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.io.FileUtils.getFile;

public class ModsDB {
    public static Set<Mod> modsList = new HashSet<>();
    static PropertiesFile propertiesFile = new PropertiesFile();
    static String gamePath = propertiesFile.loadProperties().getProperty("ModsInGamePath");
    static String gameModsPath = gamePath + "\\media_soviet\\workshop_wip\\";
    static String baseModsPath = propertiesFile.loadProperties().getProperty("ModsInBasePath") + File.separator;
    private static List<Mod> modsInBaseList = new ArrayList<>();
    private static List<Mod> modsInGameList = new ArrayList<>();

    public static String getGameModsPath() {
        return gamePath;
    }

    public static String getBaseModsPath() {
        return baseModsPath;
    }

    public static Set<Mod> getModList() {
        return modsList;
    }

    public static void getModsToList() {
        modsInGameList = parseModFiles(gameModsPath);
        modsInBaseList = parseModFiles(baseModsPath);
        Stream<Mod> modStream = Stream.concat(
                modsInGameList.stream(),
                modsInBaseList.stream()
        );
        modsList = modStream.collect(Collectors.toSet());
    }

    /*private static StringBuilder stringBuilder(StringBuilder sb, String name, String property) {
           sb
                   .append(name)
                   .append(" ")
                   .append(property)
                   .append("\n");
           return sb;
       }*/

    // Парсинг мод-файлов в списке модов
    public static List<Mod> parseModFiles(String path) {
        List<Mod> list = new ArrayList<>();
        File modDir = new File(path);
        File[] fileArray = modDir.listFiles();
        if (fileArray != null) {
            for (File file : fileArray) {
                String filePath = file.getPath();
                Properties properties = getWorkshopConfig(file);
                if (!properties.isEmpty()) {
                    Map<String, String> configsMap = convertToMap(properties);
                    Mod mod = new Mod(
                            configsMap.get("$ITEM_ID"), configsMap.get("$ITEM_NAME"),
                            filePath, new Image(filePath + File.separator + "previewimage.png"),
                            configsMap
                    );
                    list.add(mod);
                }
            }
        }
        return list;
    }

    // Строки создать в текст для сохранения в файл
/*    public String stringsToText(Mod mod) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> modProperties = mod.getPropertiesMap();
        for (Map.Entry<String, String> entry : modProperties.entrySet()) {
            String name = entry.getKey();
            String property = entry.getValue();
            if (!name.equals("$END")) {
                sb = stringBuilder(sb, name, property);
            }
        }
        sb = stringBuilder(sb, "$END", "");
        return sb.toString();
    }*/

    // Загрузить файл-описание мода
    public static Properties getWorkshopConfig(File file) {
        Properties properties = new Properties();
        file = getFile(file, "workshopconfig.ini");
        try {
            if (file.exists()) {
                properties.load(Files.newInputStream(file.toPath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    // Конвертировать Properties в Map
    static Map<String, String> convertToMap(Properties properties) {
        Map<String, String> propertiesMap = new HashMap<>();
        for (String name : properties.stringPropertyNames()) {
            propertiesMap.put(name, properties.getProperty(name));
        }
        return propertiesMap;
    }

    // Перемещение мода
    public void moveMod(Mod mod, boolean modToGame) {
        String path;
        if (modToGame) path = gameModsPath + mod.getId();
        else path = baseModsPath + mod.getId();

        try {
            FileUtils.moveDirectory(
                    new File(mod.getPath()),
                    new File(path)
            );
            mod.setPath(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkModsInGame() {
        for (Mod mod : modsInGameList) {
            mod.setSelected(true);
        }
    }
}
