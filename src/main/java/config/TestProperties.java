package config;

import java.io.IOException;
import java.util.Properties;

public class TestProperties {

    public static String getValue(String propertyName) {
        Properties properties = new Properties();
        try {
            properties.load(TestProperties.class.getClassLoader().getResourceAsStream("test.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test properties");
        }

        return properties.getProperty(propertyName);
    }
}