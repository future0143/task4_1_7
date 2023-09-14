package test;

import static utils.GeneratorPhoneNumber.getPhoneNumber;
import static method_call.call_create_customer.CreateCustomer.createCustomer;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import config.ApiConfig;
import config.DataProvider;
import io.restassured.response.Response;
import jdk.jfr.Description;
import model.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.GeneratorPhoneNumber;
import config.EndPoints;
import validator.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class CreatingCustomerTest {

    @BeforeAll
    public static void setUp() {
        ApiConfig.setUp();
    }

    @ParameterizedTest
    @DisplayName("Создание клиента с заполнением различных наборов полей")
    @Description("Создание клиента с заполнением полей")
    @MethodSource("argsProviderFactoryRequestBody")
    public void createNewCustomerWithArgumentFields(String argument) {
        String requestBody = DataProvider.getTestData(argument);
        String phoneNumber = getPhoneNumber();
        int statusCode = 201;

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = createCustomer(requestBody, phoneNumber);

        ResponseValidationPositive.validateFields(requestBody, responseBody, phoneNumber, nowTime, statusCode);
    }

    static Stream<String> argsProviderFactoryRequestBody() {
        return Stream.of("src/main/resources/request/post_request/create-customer-required-fields.json",
                "src/main/resources/request/post_request/create-customer-all-fields.json",
                "src/main/resources/request/post_request/create-customer-swapped-fields.json");
    }

    @ParameterizedTest
    @DisplayName("Создание клиента с параметризацией firstName")
    @Description("Создание клиента с параметризацией firstName")
    @MethodSource("argsProviderFactoryFirstName")
    public void createNewCustomerWithParametersOfFirstName(String argument) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        String phoneNumber = getPhoneNumber();
        int expectedStatusCode = 201;

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = createCustomer(requestBody.replace("Ivan", argument), phoneNumber);

        ResponseValidationParameterFirstName.validateFields(requestBody, responseBody, phoneNumber, nowTime, expectedStatusCode, argument);
    }

    static Stream<String> argsProviderFactoryFirstName() {
        return Stream.of("Ivan", "I", " Ivan", "Ivan-Petr", "Иван", "Ivan Petr", "Пётр", "Ivan Пётр");
    }

    @ParameterizedTest
    @DisplayName("Создание клиента с параметризацией lastName")
    @Description("Создание клиента с параметризацией lastName")
    @MethodSource("argsProviderFactoryLastName")
    public void createNewCustomerWithParametersOfLastName(String argument) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        String phoneNumber = getPhoneNumber();
        int expectedStatusCode = 201;

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = createCustomer(requestBody.replace("Petrov", argument), phoneNumber);

        ResponseValidationParameterLastName.validateFields(requestBody, responseBody, phoneNumber, nowTime, expectedStatusCode, argument);
    }

    static Stream<String> argsProviderFactoryLastName() {
        return Stream.of("Petrov", "P", " Petrov", "Petrov-Smirnov", "Петров", "Petrov Smirnov");
    }

    @Test
    @DisplayName("Создание клиента с неуникальным номером телефона")
    @Description("Создание клиента с уже существующим номером телефона")
    public void createNewCustomerWithNotUniqueId() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-all-fields.json");
        String phoneNumber = getPhoneNumber();
        String errorMessage = "The phone number is already registered";
        int statusCode = 400;

        createCustomer(requestBody, phoneNumber);

        Response responseBody = createCustomer(requestBody, phoneNumber);

        ResponseValidationNegativeOneLine.validateFields(responseBody, errorMessage, statusCode);
    }


    @ParameterizedTest
    @DisplayName("Создание клиента с пустым значением одного из обязательных полей")
    @Description("Создание клиента с пустым значением для полей: firstName, lastName")
    @MethodSource("argsProviderFactoryEmptyValue")
    public void createNewCustomerWithEmptyValueRequiredField(String argument, String errorMessage) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        String phoneNumber = getPhoneNumber();
        int statusCode = 400;

        Response responseBody = createCustomer(requestBody.replace(Customer.getCustomer(requestBody).get(argument).toString(), ""), phoneNumber);

        ResponseValidationNegativeOneLine.validateFields(responseBody, errorMessage, statusCode);
    }

    static Stream<Arguments> argsProviderFactoryEmptyValue() {
        return Stream.of(Arguments.of("firstName", "Mandatory field missing: firstName"),
                Arguments.of("lastName", "Mandatory field missing: lastName"),
                Arguments.of("phoneNumber", "Invalid phoneNumber: expected format +7XXXXXXXXXX"));
    }

    @ParameterizedTest
    @DisplayName("Создание клиента без одного из обязательных полей")
    @Description("Создание клиента без одного из полей: firstName, lastName, phoneNumber")
    @MethodSource("argsProviderFactoryEmptyField")
    public void createNewCustomerWithoutRequiredField(String argumentPath, String argumentErrorMessage) {
        String requestBody = DataProvider.getTestData(argumentPath);
        String phoneNumber = getPhoneNumber();
        String errorMessage = "Mandatory field missing: " + argumentErrorMessage;
        int statusCode = 400;

        Response responseBody = createCustomer(requestBody, phoneNumber);

        ResponseValidationNegativeOneLine.validateFields(responseBody, errorMessage, statusCode);
    }

    static Stream<Arguments> argsProviderFactoryEmptyField() {
        return Stream.of(Arguments.of("src/main/resources/request/post_request/create-customer-without-firstName.json", "firstName"),
                Arguments.of("src/main/resources/request/post_request/create-customer-without-lastName.json", "lastName"),
                Arguments.of("src/main/resources/request/post_request/create-customer-without-phoneNumber.json", "phoneNumber"));
    }

    @Test
    @DisplayName("Создание клиента со всеми полями, включая loyalty")
    @Description("Создание клиента с заполнением всех полей валидными значениями")
    public void createNewCustomerWithLoyalty() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-with-loyalty.json");
        String phoneNumber = getPhoneNumber();
        int expectedStatusCode = 201;

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = createCustomer(requestBody, phoneNumber);

        ResponseValidationPositive.validateFields(requestBody, responseBody, phoneNumber, nowTime, expectedStatusCode);
        responseBody.then()
                .assertThat()
                .body("loyalty.bonusCardNumber", not(equalTo("1234567")))
                .body("loyalty.status", not(equalTo("my status")))
                .body("loyalty.discountRate", not(equalTo(1234)));
    }

    @Test
    @DisplayName("Создание клиента с некорректным типом данных номера телефона")
    @Description("Номер телефона передается как число, а не строка")
    public void createNewCustomerWithNumericPhoneNumber() {
        String phoneNumber = getPhoneNumber();
        String requestBody = "{" +
                "\"firstName\":\"Maria\"," +
                "\"lastName\":\"Simpson\"," +
                "\"phoneNumber\": +78905927238" +
                "}";
        int expectedStatusCode = 400;
        String path = EndPoints.CREATE_CUSTOMERS.getPath();

        Response responseBody = createCustomer(requestBody, phoneNumber);
        String responseTime = responseBody.then().extract().path("timestamp").toString();

        ResponseValidationNegativeSeveralLines.validateFields(responseBody, expectedStatusCode, responseTime, path);
    }

    @Test
    //запрос возвращает статус-код 201, т.е. возможно создание клиента со строковым id, хотя тип данных у него integer
    @DisplayName("Создание клиента со строковым id")
    @Description("Создание клиента с id типа String")
    public void createNewCustomerWithStringId() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-with-String-id.json");
        String phoneNumber = getPhoneNumber();
        int expectedStatusCode = 400;
        String path = EndPoints.CREATE_CUSTOMERS.getPath();

        Response responseBody = createCustomer(requestBody, phoneNumber);
        String responseTime = responseBody.then().extract().path("timestamp").toString();

        ResponseValidationNegativeSeveralLines.validateFields(responseBody, expectedStatusCode, responseTime, path);
    }

    @ParameterizedTest
    @DisplayName("Создание клиента с параметризацией phoneNumber, негативные тесты")
    @Description("Создание клиента с параметризацией phoneNumber, негативные тесты")
    @MethodSource("argsProviderFactoryPhoneNumber")
    public void createNewCustomerWithParametersOfPhoneNumberNegative(String argument) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        String errorMessage = "Invalid phoneNumber: expected format +7XXXXXXXXXX";
        int statusCode = 400;

        Response responseBody = createCustomer(requestBody, argument);

        ResponseValidationNegativeOneLine.validateFields(responseBody, errorMessage, statusCode);
    }

    static Stream<String> argsProviderFactoryPhoneNumber() {
        return Stream.of("",
                GeneratorPhoneNumber.getPhoneNumber().replace("+7", "8"),
                GeneratorPhoneNumber.getPhoneNumber().substring(2));
    }

    @Test
    @DisplayName("Создание клиента со спецсимволами в поле lastName")
    @Description("Создание клиента со спецсимволами в поле lastName")
    public void createNewCustomerWithSpecSymbols() {
        String phoneNumber = getPhoneNumber();
        String requestBody = "{" +
                "\"firstName\": @*% ," +
                "\"lastName\":\"Simpson\"," +
                "\"phoneNumber\": \"+78905927238\"" +
                "}";
        String path = EndPoints.CREATE_CUSTOMERS.getPath();
        int expectedStatusCode = 400;

        Response responseBody = createCustomer(requestBody, phoneNumber);

        String responseTime = responseBody.then().extract().path("timestamp").toString();

        ResponseValidationNegativeSeveralLines.validateFields(responseBody, expectedStatusCode, responseTime, path);
    }
}