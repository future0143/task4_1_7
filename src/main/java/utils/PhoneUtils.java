package utils;

import org.apache.commons.lang3.RandomStringUtils;

public class PhoneUtils {

    public static String formatPhoneNumber(String phoneNumber) {
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        return "+" + digitsOnly.charAt(0) + // Добавляем +7
                "(" +
                digitsOnly.substring(1, 4) + // Добавляем код региона
                ")" +
                digitsOnly.substring(4, 7) + // Добавляем первую группу цифр
                "-" +
                digitsOnly.substring(7, 9) + // Добавляем вторую группу цифр
                "-" +
                digitsOnly.substring(9);
    }

    public static String getPhoneNumber() {
        return "+7" + RandomStringUtils.randomNumeric(10);
    }
}