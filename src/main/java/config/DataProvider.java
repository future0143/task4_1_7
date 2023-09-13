package config;

import java.io.*;

public class DataProvider {

    public static String getTestData(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder content = new StringBuilder();
            while (br.ready()) {
                content.append(br.readLine());
            }
            return content.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
