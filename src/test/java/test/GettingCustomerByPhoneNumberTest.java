package test;

import config.ApiConfig;
import config.DataProvider;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.GeneratorPhoneNumber;
import validator.ResponseValidationNegativeOneLine;
import validator.ResponseValidationPositive;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static method_call.call_create_customer.CreateCustomer.createCustomer;
import static method_call.call_delete_customer.DeleteCustomerById.deleteCustomer;
import static method_call.call_get_byPhoneNumber.GetCustomerByPhoneNumber.getCustomerByPhoneNumber;
import static utils.GeneratorPhoneNumber.getPhoneNumber;

public class GettingCustomerByPhoneNumberTest {

    private static int id;
    private static String requestBody;
    private static String phoneNumber;
    private static LocalDateTime nowTime;

    @BeforeAll
    public static void setUp() {
        ApiConfig.setUp();
    }

    @BeforeEach
    void creatingCustomer() {
        requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        phoneNumber = getPhoneNumber();

        nowTime = LocalDateTime.now();

        Response responseBody = createCustomer(requestBody, phoneNumber);

        id = responseBody.then().extract().path("id");
    }

    @Test
    @DisplayName("Получение клиента по существующему номеру телефона")
    @Description("Получение созданного клиента по его номеру телефона")
    public void getCustomerByExistingPhoneNumber() {
        int expectedStatusCode = 200;

        Response responseBody = getCustomerByPhoneNumber(phoneNumber);

        ResponseValidationPositive.validateFields(requestBody, responseBody, phoneNumber, nowTime, expectedStatusCode);
    }

    @Test
    @DisplayName("Получение клиента по несуществующему номеру телефона")
    @Description("Удаление созданного клиента и получение его по тому же номеру телефона")
    public void getCustomerByNotExistingPhoneNumber() {
        deleteCustomer(id);
        int statusCode = 404;
        String errorMessage = "Customer not found";

        Response responseBody = getCustomerByPhoneNumber(phoneNumber);

        ResponseValidationNegativeOneLine.validateFields(responseBody, errorMessage, statusCode);
    }

    @ParameterizedTest
    @DisplayName("Получение клиента с параметризацией phoneNumber, негативные тесты")
    @Description("Получение клиента с параметризацией phoneNumber, негативные тесты")
    @MethodSource("argsProviderFactoryPhoneNumber")
    public void getNewCustomerWithParametersOfPhoneNumberNegative(String argument) {
        String errorMessage = "Customer not found";
        int statusCode = 404;

        Response responseBody = getCustomerByPhoneNumber(argument);

        ResponseValidationNegativeOneLine.validateFields(responseBody, errorMessage, statusCode);
    }

    static Stream<String> argsProviderFactoryPhoneNumber() {
        return Stream.of("",
                GeneratorPhoneNumber.getPhoneNumber().replace("+7", "8"),
                GeneratorPhoneNumber.getPhoneNumber().substring(2));
    }

    @Test
    @DisplayName("Получение клиента по номеру телефона с другим форматом")
    @Description("Получение клиента по номеру телефона формата +7(YYY)XXX-XX-XX")
    public void getCustomerByPhoneNumberAnotherFormat() {
        int statusCode = 404;
        String errorMessage = "Customer not found";
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        StringBuilder formattedNumber = new StringBuilder("+");
        formattedNumber.append(digitsOnly.charAt(0)); // Добавляем +7
        formattedNumber.append("(");
        formattedNumber.append(digitsOnly, 1, 4); // Добавляем код региона
        formattedNumber.append(")");
        formattedNumber.append(digitsOnly, 4, 7); // Добавляем первую группу цифр
        formattedNumber.append("-");
        formattedNumber.append(digitsOnly, 7, 9); // Добавляем вторую группу цифр
        formattedNumber.append("-");
        formattedNumber.append(digitsOnly.substring(9)); // Добавляем оставшиеся цифры

        Response responseBody = getCustomerByPhoneNumber(formattedNumber.toString());

        ResponseValidationNegativeOneLine.validateFields(responseBody, errorMessage, statusCode);
    }
}