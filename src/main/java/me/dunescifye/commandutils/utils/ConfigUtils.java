package me.dunescifye.commandutils.utils;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.logging.Logger;

public class ConfigUtils {


    //Method for checking if is integer by Jonas K https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    //Int with min value
    public static int setupConfig(String path, FileConfiguration config, int defaultValue, int minValue) {
        Logger logger = Bukkit.getLogger();
        if (!config.isSet(path)) {
            config.set(path, defaultValue);
            return defaultValue;
        }

        String valueStr = config.getString(path);

        if (!isInteger(valueStr)) {
            logger.warning("[CustomItems] " + path + " is not a valid number. Must be a number greater than  " + minValue + ". Found " + valueStr + ". Using default value of " + defaultValue + ".");
            return defaultValue;
        }

        int value = Integer.parseInt(valueStr);

        if (value < minValue) {
            logger.warning("[CustomItems] " + path + " is not a valid number. Must be a number greater than  " + minValue + ". Found " + valueStr + ". Using default value of " + defaultValue + ".");
            return defaultValue;
        }

        return value;
    }

    //Integer with min value and comment
    public static int setupConfig(String path, FileConfiguration config, int defaultValue, int minValue, List<String> comments) {
        if (!config.isSet(path)) {
            int value =  setupConfig(path, config, defaultValue, minValue);
            config.setComments(path, comments);
            return value;
        } else {
            return setupConfig(path, config, defaultValue, minValue);
        }
    }
    //Double with min value
    public static double setupConfig(String path, FileConfiguration config, double defaultValue, double minValue) {
        Logger logger = Bukkit.getLogger();
        if (!config.isSet(path)) {
            config.set(path, defaultValue);
            return defaultValue;
        }

        String valueStr = config.getString(path);

        if (!NumberUtils.isCreatable(valueStr)) {
            logger.warning("[CustomItems] " + path + " is not a valid double. Must be a number greater than  " + minValue + ". Found " + valueStr + ". Using default value of " + defaultValue + ".");
            return defaultValue;
        }

        double value = Double.parseDouble(valueStr);

        if (value < minValue) {
            logger.warning("[CustomItems] " + path + " is not a valid double. Must be a number greater than  " + minValue + ". Found " + valueStr + ". Using default value of " + defaultValue + ".");
            return defaultValue;
        }

        return value;
    }

    //Double with min value and comment
    public static double setupConfig(String path, FileConfiguration config, double defaultValue, double minValue, List<String> comments) {
        if (!config.isSet(path)) {
            double value = setupConfig(path, config, defaultValue, minValue);
            config.setComments(path, comments);
            return value;
        } else {
            return setupConfig(path, config, defaultValue, minValue);
        }
    }
    //Int without bound
    public static int setupConfig(String path, FileConfiguration config, int defaultValue) {
        Logger logger = Bukkit.getLogger();
        if (!config.isSet(path)) {
            config.set(path, defaultValue);
            return defaultValue;
        }

        String valueStr = config.getString(path);

        if (!valueStr.matches("-?\\d+(\\.\\d+)?")) {
            logger.warning("[CustomItems] " + path + " is not a valid number. Found " + valueStr + ". Using default value of " + defaultValue + ".");
            return defaultValue;
        }

        return Integer.parseInt(valueStr);
    }
    //String
    public static String setupConfig(String path, FileConfiguration config, String defaultValue) {
        if (!config.isSet(path)) {
            config.set(path, defaultValue);
            return defaultValue;
        }
        return config.getString(path);
    }
    //String with comments
    public static String setupConfig(String path, FileConfiguration config, String defaultValue, List<String> comments) {
        if (!config.isSet(path)) {
            config.set(path, defaultValue);
            config.setComments(path, comments);
            return defaultValue;
        }
        return config.getString(path);
    }
    //List of strings
    public static List<String> setupConfig(String path, FileConfiguration config, List<String> defaultValue) {
        if (!config.isSet(path)) {
            config.set(path, defaultValue);
            return defaultValue;
        }
        return config.getStringList(path);
    }
    //List of strings with comments
    public static List<String> setupConfig(String path, FileConfiguration config, List<String> defaultValue, List<String> comments) {
        if (!config.isSet(path)) {
            config.set(path, defaultValue);
            config.setComments(path, comments);
            return defaultValue;
        }
        return config.getStringList(path);

    }
}
