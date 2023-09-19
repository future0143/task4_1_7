package test;

import config.ApiConfigSetup;
import config.DataProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import validator.response_validator.ResponseValidationNegativeOneLine;
import validator.response_validator.ResponseValidationNegativeSeveralLines;
import validator.response_validator.ResponseValidationPositive;

import java.time.LocalDateTime;

import static method_call.call_create_customer.CreateCustomer.createCustomerResponse;
import static method_call.call_delete_customer.DeleteCustomerById.deleteCustomer;
import static method_call.call_get_byId_customer.GetCustomerById.getCustomerById;
import static utils.GeneratorPhoneNumber.getPhoneNumber;

public class GettingCustomerByIdTest implements ApiConfigSetup {

    private static int id;
    private static String requestBody;
    private static String phoneNumber;
    private static LocalDateTime nowTime;

    @BeforeEach
    void creatingCustomer(TestInfo info) {
        if (!info.getDisplayName().equals("Получение клиента по одному из обязательных полей")) {
            requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
            phoneNumber = getPhoneNumber();

            nowTime = LocalDateTime.now();

            Response responseBody = createCustomerResponse(requestBody, phoneNumber);

            id = responseBody.then().extract().path("id");
        }
    }

    @Test
    @DisplayName("Получение клиента по существующему id")
    @Description("Получение клиента по существующему id")
    public void getCustomerByExistingId() {
        int statusCode = 200;

        Response responseBody = getCustomerById(id);

        ResponseValidationPositive.validateFields(requestBody, responseBody, statusCode, nowTime, phoneNumber);
    }

    @Test
    @DisplayName("Получение клиента по несуществующему id")
    @Description("Удаление клиента и получение его по тому же id")
    public void getCustomerByNotExistingId() {
        int statusCode = 404;
        String errorMessage = "Customer not found";

        deleteCustomer(id);

        Response response = getCustomerById(id);
        ResponseValidationNegativeOneLine.validateFields(response, errorMessage, statusCode);
    }

    @Test
    //запрос возвращает статус-код 201, т.е. возможно получение клиента со строковым id, хотя тип данных у него integer
    @DisplayName("Получение клиента по нечисловому id")
    @Description("Получение клиента по строковому значению id")
    public void getCustomerByStringId() {
        String strId = String.valueOf(id);
        int statusCode = 400;
        String path = "/customers/" + strId;

        Response response = RestAssured.get("/customers/" + strId);
        String responseTime = response.then().extract().path("timestamp").toString();

        ResponseValidationNegativeSeveralLines.validateFields(response, statusCode, responseTime, path);
    }

    @Test
    @DisplayName("Получение клиента по пустому полю id")
    @Description("null вместо id")
    public void getCustomerByEmptyId() {
        int statusCode = 400;
        String path = "/customers/" + "null";

        Response response = RestAssured.get("/customers/" + null);
        String responseTime = response.then().extract().path("timestamp").toString();

        ResponseValidationNegativeSeveralLines.validateFields(response, statusCode, responseTime, path);
    }

    @Test
    @DisplayName("Получение клиента по отрицательному id")
    @Description("Получение клиента по существующему id с добавлением минуса")
    public void getCustomerByNegativeId() {
        int requestId = -id;
        int statusCode = 404;
        String errorMessage = "Customer not found";

        Response response = RestAssured.get("/customers/" + requestId);

        ResponseValidationNegativeOneLine.validateFields(response, errorMessage, statusCode);
    }
}