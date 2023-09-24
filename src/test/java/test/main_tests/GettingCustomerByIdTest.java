package test.main_tests;

import config.ApiConfigSetup;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import test.base_tests.BaseTestCreatingCustomer;
import validator.response_validator.ResponseValidationNegative;
import validator.response_validator.ResponseValidationPositive;

import static method_call.CustomersRequestHandler.*;

public class GettingCustomerByIdTest extends BaseTestCreatingCustomer implements ApiConfigSetup {

    @BeforeEach
    void creatingCustomer(TestInfo info) {
        if (!info.getDisplayName().equals("Получение клиента по одному из обязательных полей")) {
            super.creatingCustomer();
        }
    }

    @Test
    @DisplayName("Получение клиента по существующему id")
    @Description("Получение клиента по существующему id")
    public void getCustomerByExistingId() {
        int statusCode = 200;

        Response responseBody = getCustomerById(id);

        ResponseValidationPositive.validateFieldsFromResponse(requestBody, responseBody, statusCode, nowTime, phoneNumber);
    }

    @Test
    @DisplayName("Получение клиента по несуществующему id")
    @Description("Удаление клиента и получение его по тому же id")
    public void getCustomerByNotExistingId() {
        int statusCode = 404;
        String errorMessage = "Customer not found";

        deleteCustomer(id);

        Response response = getCustomerById(id);
        ResponseValidationNegative.validateOneField(response, errorMessage, statusCode);
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

        ResponseValidationNegative.validateSeveralFields(response, statusCode, responseTime, path);
    }

    @Test
    @DisplayName("Получение клиента по пустому полю id")
    @Description("null вместо id")
    public void getCustomerByEmptyId() {
        int statusCode = 400;
        String path = "/customers/" + "null";

        Response response = RestAssured.get("/customers/" + null);
        String responseTime = response.then().extract().path("timestamp").toString();

        ResponseValidationNegative.validateSeveralFields(response, statusCode, responseTime, path);
    }

    @Test
    @DisplayName("Получение клиента по отрицательному id")
    @Description("Получение клиента по существующему id с добавлением минуса")
    public void getCustomerByNegativeId() {
        int requestId = -id;
        int statusCode = 404;
        String errorMessage = "Customer not found";

        Response response = RestAssured.get("/customers/" + requestId);

        ResponseValidationNegative.validateOneField(response, errorMessage, statusCode);
    }
}