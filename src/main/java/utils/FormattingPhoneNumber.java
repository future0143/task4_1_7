package utils;

public class FormattingPhoneNumber {
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
}