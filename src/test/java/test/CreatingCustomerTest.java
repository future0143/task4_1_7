package test;

import static db.DatabaseManager.selectCountOfLinesInTableCustomer;
import static method_call.CustomersRequestHandler.createCustomerResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static utils.GeneratorPhoneNumber.getPhoneNumber;
import static validator.database_validator.DatabaseValidation.validateTableIsEmpty;

import config.ApiConfigSetup;
import config.DataProvider;
import db.DatabaseManager;
import io.restassured.response.Response;
import jdk.jfr.Description;
import model.Customer;
import config.SerializingCustomer;
import model.Loyalty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test.base_tests.BaseTestCleanTable;
import utils.GeneratorPhoneNumber;
import config.EndPoints;
import validator.database_validator.DatabaseValidation;
import validator.response_validator.ResponseValidationNegative;
import validator.response_validator.ResponseValidationPositive;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Stream;

public class CreatingCustomerTest extends BaseTestCleanTable implements ApiConfigSetup {

    @ParameterizedTest(name = "Заполнение полей из файла: {0}")
    @DisplayName("Создание клиента с заполнением различных наборов полей")
    @Description("Создание клиента с заполнением полей")
    @MethodSource("argsProviderFactoryRequestBody")
    public void createNewCustomerWithArgumentFields(String argument) {
        String requestBody = DataProvider.getTestData(argument);
        String phoneNumber = getPhoneNumber();
        int statusCode = 201;

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);
        String bonusCardNumber = responseBody.then().extract().path("loyalty.bonusCardNumber");

        ResponseValidationPositive.validateFieldsFromResponse(requestBody, responseBody, statusCode, nowTime, phoneNumber);

        Map<String, Object> customerData = DatabaseManager.selectCustomerAsMap(phoneNumber);
        DatabaseValidation.validateCustomerAsMap(requestBody, customerData, phoneNumber);

        Map<String, Object> customerDataLoyalty = DatabaseManager.selectLoyalty(bonusCardNumber);
        DatabaseValidation.validateLoyaltyAsMap(customerDataLoyalty);
    }

    static Stream<String> argsProviderFactoryRequestBody() {
        return Stream.of("src/main/resources/request/post_request/create-customer-required-fields.json",
                "src/main/resources/request/post_request/create-customer-all-fields.json",
                "src/main/resources/request/post_request/create-customer-swapped-fields.json");
    }

    @ParameterizedTest(name = "Создание клиента с именем {0}")
    @DisplayName("Создание клиента с параметризацией firstName")
    @Description("Создание клиента с параметризацией firstName")
    @MethodSource("argsProviderFactoryFirstName")
    public void createNewCustomerWithParametersOfFirstName(String argument) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        String newRequest = requestBody.replace("Ivan", argument);
        String phoneNumber = getPhoneNumber();
        int statusCode = 201;

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = createCustomerResponse(newRequest, phoneNumber);
        String bonusCardNumber = responseBody.then().extract().path("loyalty.bonusCardNumber");

        ResponseValidationPositive.validateFieldsFromResponse(newRequest, responseBody, statusCode, nowTime, phoneNumber);

        Map<String, Object> customerData = DatabaseManager.selectCustomerAsMap(phoneNumber);
        DatabaseValidation.validateCustomerAsMap(newRequest, customerData, phoneNumber);

        Map<String, Object> customerDataLoyalty = DatabaseManager.selectLoyalty(bonusCardNumber);
        DatabaseValidation.validateLoyaltyAsMap(customerDataLoyalty);
    }

    static Stream<String> argsProviderFactoryFirstName() {
        return Stream.of("Ivan", "I", " Ivan", "Ivan-Petr", "Иван", "Ivan Petr", "Пётр", "Ivan Пётр");
    }

    @ParameterizedTest(name = "Создание клиента с фамилией {0}")
    @DisplayName("Создание клиента с параметризацией lastName")
    @Description("Создание клиента с параметризацией lastName")
    @MethodSource("argsProviderFactoryLastName")
    public void createNewCustomerWithParametersOfLastName(String argument) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        String newRequest = requestBody.replace("Petrov", argument);
        String phoneNumber = getPhoneNumber();
        int statusCode = 201;

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = createCustomerResponse(newRequest, phoneNumber);

        ResponseValidationPositive.validateFieldsFromResponse(newRequest, responseBody, statusCode, nowTime, phoneNumber);

        String bonusCardNumber = responseBody.then().extract().path("loyalty.bonusCardNumber");

        Map<String, Object> customerData = DatabaseManager.selectCustomerAsMap(phoneNumber);
        DatabaseValidation.validateCustomerAsMap(newRequest, customerData, phoneNumber);

        Map<String, Object> customerDataLoyalty = DatabaseManager.selectLoyalty(bonusCardNumber);
        DatabaseValidation.validateLoyaltyAsMap(customerDataLoyalty);
    }

    static Stream<String> argsProviderFactoryLastName() {
        return Stream.of("Petrov", "P", " Petrov", "Petrov-Smirnov", "Петров", "Petrov Smirnov");
    }

    @ParameterizedTest(name = "Создание клиента с email {0}")
    @DisplayName("Создание клиента с параметризацией email, позитивные тесты")
    @Description("Создание клиента с параметризацией email, позитивные тесты")
    @MethodSource("argsProviderFactoryEmailPositive")
    public void createNewCustomerWithParametersOfEmailPositive(String argument) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields-and-email.json");
        String newRequest = requestBody.replace("ivanpetr@mail.ru", argument);
        String phoneNumber = getPhoneNumber();
        int statusCode = 201;

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = createCustomerResponse(newRequest, phoneNumber);

        ResponseValidationPositive.validateFieldsFromResponse(newRequest, responseBody, statusCode, nowTime, phoneNumber);

        String bonusCardNumber = responseBody.then().extract().path("loyalty.bonusCardNumber");

        Map<String, Object> customerData = DatabaseManager.selectCustomerAsMap(phoneNumber);
        DatabaseValidation.validateCustomerAsMap(newRequest, customerData, phoneNumber);

        Map<String, Object> customerDataLoyalty = DatabaseManager.selectLoyalty(bonusCardNumber);
        DatabaseValidation.validateLoyaltyAsMap(customerDataLoyalty);
    }

    static Stream<String> argsProviderFactoryEmailPositive() {
        return Stream.of("ivan-petr@mail.ru", "ivan-petr@mail.ru", "i@mail.ru", "ivanpetr@mail.com", "ivanpetr@gmail.com");
    }

    @Test
    @DisplayName("Создание клиента со всеми полями, кроме дат")
    @Description("Создание клиента с заполнением всех полей, кроме дат, валидными значениями")
    public void createNewCustomerWithLoyalty() {
        String phoneNumber = getPhoneNumber();
        Loyalty loyalty = new Loyalty("12345", "notStatus", 98765);
        Customer customerRequest = new Customer(2, "Ivan", "Petrov", phoneNumber,
                "ivanpetr@mail.ru", "2012-09-11", loyalty, null);

        String requestBody = SerializingCustomer.getJson(customerRequest);

        int expectedStatusCode = 201;

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);
        Customer customer = SerializingCustomer.getCustomer(responseBody);

        ResponseValidationPositive.validateFieldsFromCustomer(requestBody, responseBody,customer, expectedStatusCode, nowTime, phoneNumber);

        Customer customerData = DatabaseManager.selectCustomer(phoneNumber);
        ResponseValidationPositive.validateFieldsFromCustomer(requestBody, responseBody,customerData, expectedStatusCode, nowTime, phoneNumber);

        Loyalty loyaltyData = customerData.getLoyalty();
        ResponseValidationPositive.validateLoyalty(loyaltyData);
    }

    @Test
    @DisplayName("Создание клиента с неуникальным номером телефона")
    @Description("Создание клиента с уже существующим номером телефона")
    public void createNewCustomerWithNotUniqueId() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-all-fields.json");
        String phoneNumber = getPhoneNumber();
        String errorMessage = "The phone number is already registered";
        int statusCode = 400;

        createCustomerResponse(requestBody, phoneNumber);

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);

        ResponseValidationNegative.validateOneField(responseBody, errorMessage, statusCode);

        int countLines = selectCountOfLinesInTableCustomer();
        assertEquals(1, countLines);
    }


    @ParameterizedTest(name = "Пустое значение поля {0}")
    @DisplayName("Создание клиента с пустым значением одного из обязательных полей")
    @Description("Создание клиента с пустым значением для полей: firstName, lastName, phoneNumber")
    @MethodSource("argsProviderFactoryEmptyValue")
    public void createNewCustomerWithEmptyValueRequiredField(String argument, String errorMessage) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        String phoneNumber = getPhoneNumber();
        int statusCode = 400;

        Response responseBody = createCustomerResponse(requestBody.replace(SerializingCustomer.getCustomerFromRequestBody(requestBody).get(argument), ""), phoneNumber);

        ResponseValidationNegative.validateOneField(responseBody, errorMessage, statusCode);

        int countLines = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(countLines);
    }

    static Stream<Arguments> argsProviderFactoryEmptyValue() {
        return Stream.of(Arguments.of("firstName", "Mandatory field missing: firstName"),
                Arguments.of("lastName", "Mandatory field missing: lastName"),
                Arguments.of("phoneNumber", "Invalid phoneNumber: expected format +7XXXXXXXXXX"));
    }

    @ParameterizedTest(name = "Отсутствие обязательного поля {0}")
    @DisplayName("Создание клиента без одного из обязательных полей")
    @Description("Создание клиента без одного из полей: firstName, lastName, phoneNumber")
    @MethodSource("argsProviderFactoryEmptyField")
    public void createNewCustomerWithoutRequiredField(String argumentPath, String argumentErrorMessage) {
        String requestBody = DataProvider.getTestData(argumentPath);
        String phoneNumber = getPhoneNumber();
        String errorMessage = "Mandatory field missing: " + argumentErrorMessage;
        int statusCode = 400;

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);

        ResponseValidationNegative.validateOneField(responseBody, errorMessage, statusCode);

        int countLines = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(countLines);
    }

    static Stream<Arguments> argsProviderFactoryEmptyField() {
        return Stream.of(Arguments.of("src/main/resources/request/post_request/create-customer-without-firstName.json", "firstName"),
                Arguments.of("src/main/resources/request/post_request/create-customer-without-lastName.json", "lastName"),
                Arguments.of("src/main/resources/request/post_request/create-customer-without-phoneNumber.json", "phoneNumber"));
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

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);
        String responseTime = responseBody.then().extract().path("timestamp").toString();

        ResponseValidationNegative.validateSeveralFields(responseBody, expectedStatusCode, responseTime, path);

        int countLines = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(countLines);
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

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);
        String responseTime = responseBody.then().extract().path("timestamp").toString();

        ResponseValidationNegative.validateSeveralFields(responseBody, expectedStatusCode, responseTime, path);

        int countLines = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(countLines);
    }

    @ParameterizedTest(name = "Создание клиента с номером телефона {0}")
    @DisplayName("Создание клиента с параметризацией phoneNumber, негативные тесты")
    @Description("Создание клиента с параметризацией phoneNumber, негативные тесты")
    @MethodSource("argsProviderFactoryPhoneNumber")
    public void createNewCustomerWithParametersOfPhoneNumberNegative(String argument) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        String errorMessage = "Invalid phoneNumber: expected format +7XXXXXXXXXX";
        int statusCode = 400;

        Response responseBody = createCustomerResponse(requestBody, argument);

        ResponseValidationNegative.validateOneField(responseBody, errorMessage, statusCode);

        int countLines = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(countLines);
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

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);

        String responseTime = responseBody.then().extract().path("timestamp").toString();

        ResponseValidationNegative.validateSeveralFields(responseBody, expectedStatusCode, responseTime, path);

        int countLines = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(countLines);
    }

    @Test
    @DisplayName("Запрос без тела")
    @Description("Запрос с пустым телом")
    public void createNewCustomerWithEmptyBody() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/empty-request.json");
        String phoneNumber = getPhoneNumber();
        int statusCode = 400;
        String path = EndPoints.CREATE_CUSTOMERS.getPath();

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);

        String responseTime = responseBody.then().extract().path("timestamp").toString();

        ResponseValidationNegative.validateSeveralFields(responseBody, statusCode, responseTime, path);

        int countLines = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(countLines);
    }

    @Test
    @DisplayName("Запрос с телом запроса в формате xml")
    @Description("Запрос с телом запроса в формате xml")
    public void createNewCustomerWithBodyXml() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/xml-body.xml");
        String phoneNumber = getPhoneNumber();
        int statusCode = 400;
        String path = EndPoints.CREATE_CUSTOMERS.getPath();

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);

        String responseTime = responseBody.then().extract().path("timestamp").toString();

        ResponseValidationNegative.validateSeveralFields(responseBody, statusCode, responseTime, path);

        int countLines = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(countLines);
    }

    @Test
    @DisplayName("Запрос с лишним полем")
    @Description("Запрос с обязательными параметрами и лишним полем")
    public void createNewCustomerWithExtraField() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-with-extra-field.json");
        String phoneNumber = getPhoneNumber();
        int statusCode = 201;

        LocalDateTime nowTime = LocalDateTime.now();

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);
        String responseBodyAsString = responseBody.getBody().asString();

        responseBody.then().statusCode(statusCode);
        assertFalse(responseBodyAsString.contains("eyeColor"));

        Customer customer = DatabaseManager.selectCustomer(phoneNumber);
        DatabaseValidation.checkThatTableNotHaveExtraColumn("eyeColor");

        ResponseValidationPositive.validateFieldsFromCustomer(requestBody,responseBody,customer,statusCode,nowTime,phoneNumber);
    }

    @ParameterizedTest(name = "Создание клиента с email {0}")
    //тесты с некорректными емэилами ("ivanpetr@mail", "ivanpetr@mail.hi", "ivanpetr@abcd.ru") возвращают код 201 вместо 400
    @DisplayName("Создание клиента с параметризацией email, негативные тесты")
    @Description("Создание клиента с параметризацией email, негативные тесты")
    @MethodSource("argsProviderFactoryEmailNegative")
    public void createNewCustomerWithParametersOfEmailNegative(String argument) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields-and-email.json");
        String phoneNumber = getPhoneNumber();
        int statusCode = 400;
        String errorMessage = "Incorrect email";

        Response responseBody = createCustomerResponse(requestBody.replace("ivanpetr@mail.ru", argument), phoneNumber);

        ResponseValidationNegative.validateOneField(responseBody, errorMessage, statusCode);

        int countLines = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(countLines);
    }

    static Stream<String> argsProviderFactoryEmailNegative() {
        return Stream.of("Email", "@mail.ru", "ivanpetr@mail", "ivanpetr@mail.hi", "ivanpetrmail.ru", "ivanpetr@abcd.ru");
    }

    @ParameterizedTest(name = "Создание клиента с датой рождения {0}")
    // даты рождения в далеком прошлом и в будущем успешно проходят
    @DisplayName("Создание клиента с параметризацией dateOfBirth, негативные тесты")
    @Description("Создание клиента с параметризацией dateOfBirth, негативные тесты")
    @MethodSource("argsProviderFactoryDateOfBirthNegative")
    public void createNewCustomerWithParametersOfDateOfBirthNegative(String argument) {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields-and-dateOfBirth.json");
        String phoneNumber = getPhoneNumber();
        int statusCode = 400;
        String path = EndPoints.CREATE_CUSTOMERS.getPath();

        Response responseBody = createCustomerResponse(requestBody.replace("2020-09-11", argument), phoneNumber);
        String responseTime = responseBody.then().extract().path("timestamp").toString();

        ResponseValidationNegative.validateSeveralFields(responseBody, statusCode, responseTime, path);

        int countLines = selectCountOfLinesInTableCustomer();
        validateTableIsEmpty(countLines);
    }

    static Stream<String> argsProviderFactoryDateOfBirthNegative() {
        return Stream.of("11-09-2020", "11 09 2020", "2025-09-11", "2020_09_11", "20-09-11", "2020-09-41", "2020-13-11", "1800-09-11", "09-11", "2020-11", "2020-26");
    }
}