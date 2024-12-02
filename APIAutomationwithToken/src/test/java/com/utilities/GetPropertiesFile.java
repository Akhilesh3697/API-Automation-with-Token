package com.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetPropertiesFile {
    public static Properties properties;

    public void ConfigReader() {
        properties = new Properties();
        String dir = System.getProperty("user.dir");
        try {
            FileInputStream fis = new FileInputStream(dir+"\\src\\test\\resources\\ApiDetails.properties");
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getProperty(String key) {
        if (properties == null) {
            ConfigReader();
        }
        return properties.getProperty(key);
    }
}
