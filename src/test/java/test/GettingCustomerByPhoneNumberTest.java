package test;

import config.ApiConfigSetup;
import config.BaseTest;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.GeneratorPhoneNumber;
import validator.response_validator.ResponseValidationNegative;
import validator.response_validator.ResponseValidationPositive;

import java.util.stream.Stream;

import static method_call.CustomersRequestHandler.*;
import static utils.FormattingPhoneNumber.formatPhoneNumber;

public class GettingCustomerByPhoneNumberTest extends BaseTest implements ApiConfigSetup {

    @Test
    @DisplayName("Получение клиента по существующему номеру телефона")
    @Description("Получение созданного клиента по его номеру телефона")
    public void getCustomerByExistingPhoneNumber() {
        int statusCode = 200;

        Response responseBody = getCustomerByPhoneNumber(phoneNumber);

        ResponseValidationPositive.validateFields(requestBody, responseBody, statusCode, nowTime, phoneNumber);
    }

    @Test
    @DisplayName("Получение клиента по несуществующему номеру телефона")
    @Description("Удаление созданного клиента и получение его по тому же номеру телефона")
    public void getCustomerByNotExistingPhoneNumber() {
        deleteCustomer(id);
        int statusCode = 404;
        String errorMessage = "Customer not found";

        Response responseBody = getCustomerByPhoneNumber(phoneNumber);

        ResponseValidationNegative.validateOneField(responseBody, errorMessage, statusCode);
    }

    @ParameterizedTest(name = "Получение клиента с номером телефона {0}")
    @DisplayName("Получение клиента с параметризацией phoneNumber, негативные тесты")
    @Description("Получение клиента с параметризацией phoneNumber, негативные тесты")
    @MethodSource("argsProviderFactoryPhoneNumber")
    public void getNewCustomerWithParametersOfPhoneNumberNegative(String argument) {
        String errorMessage = "Customer not found";
        int statusCode = 404;

        Response responseBody = getCustomerByPhoneNumber(argument);

        ResponseValidationNegative.validateOneField(responseBody, errorMessage, statusCode);
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

        String formattedNumber = formatPhoneNumber(phoneNumber);

        Response responseBody = getCustomerByPhoneNumber(formattedNumber);

        ResponseValidationNegative.validateOneField(responseBody, errorMessage, statusCode);
    }
}