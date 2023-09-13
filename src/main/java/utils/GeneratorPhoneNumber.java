package utils;

import org.apache.commons.lang3.RandomStringUtils;

public class GeneratorPhoneNumber {

    public static String getPhoneNumber() {
        return "+7" + RandomStringUtils.randomNumeric(10);
    }
}
