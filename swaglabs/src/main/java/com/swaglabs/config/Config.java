package com.swaglabs.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

public class Config {
    static Properties CONFIG = new Properties();
    static Properties CONSTANTS = new Properties();

    /**
     * Method to initialize configuration.
     *
     * @throws IOException
     *
     */
    public static Properties initializeConfig() throws IOException {
        if (System.getProperty("appName") == null) {
            return CONFIG = loadConfig("CVC");
        }
        switch (System.getProperty("appName")) {
            case "PF_APP": {
                CONFIG = loadConfig("PF");
                break;
            }
            case "CVC_APP": {
                CONFIG = loadConfig("CVC");
                break;
            }
            default:
                return CONFIG;
        }
        return CONFIG;
    }

    public static Properties loadConfig(String app) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(
                    System.getProperty("user.dir") + "/src/main/resources/config_%d.properties".replace("%d", app));
            CONFIG.load(fis);

            String platformProperty = System.getProperty("platform");

            String platform = (platformProperty != null) ? platformProperty : "Android_Run";
            /*
             * Loading the properties from Sessions.yml file. Note: If a config
             * parameter is not provided then default parameter will be picked
             * as Test_Pixel_2
             */
            Map<String, String> additonlaConfig = parseYaml("src/main/resources/Sessions_%d.yml".replace("%d", app),
                    platform);
            if (System.getProperty("device") != null) {
                additonlaConfig.replace("DEVICE_ID", System.getProperty("device"));
            }
            if (System.getProperty("buildVersion") != null) {
                additonlaConfig.replace("BUILD_VERSION", System.getProperty("buildVersion"));
            }
            if (System.getProperty("access_key") != null) {
                additonlaConfig.replace("ACCESS_KEY", System.getProperty("access_key"));
            }
            loadDeviceProperty(additonlaConfig);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return CONFIG;
    }

    /**
     * Method to load properties specific for the emulator/device
     *
     * @param: properties
     *             the map of emulator properties read from the Sessions.yml
     *             file
     *
     */

    public static void loadDeviceProperty(Map<String, String> properties) {
        for (Entry<String, String> config : properties.entrySet()) {
            CONFIG.setProperty(config.getKey(), config.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> parseYaml(String filename, String parameter) throws IOException {
        FileInputStream fis = null;
        Map<String, Object> platforms = null;
        Map<String, String> configs = null;
        try {
            fis = new FileInputStream(System.getProperty("user.dir") + "/" + filename);
            platforms = (Map<String, Object>) new Yaml().load(fis);
            for (Entry<String, Object> key : platforms.entrySet()) {
                if (key.getKey().equalsIgnoreCase(parameter)) {
                    configs = (Map<String, String>) key.getValue();
                    break;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return configs;
    }

}

