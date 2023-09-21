package test;

import config.ApiConfigSetup;
import config.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import validator.response_validator.ResponseValidationNegative;

import static method_call.CustomersRequestHandler.deleteCustomer;

public class DeletingCustomerByIDTest extends BaseTest implements ApiConfigSetup {

    @Test
    @DisplayName("Удаление клиента по существующему id")
    @Description("Удаление клиента по существующему id")
    public void deleteCustomerByExistingId() {
        int expectedStatusCode = 204;

        Response responseBody = deleteCustomer(id);

        responseBody.then().statusCode(expectedStatusCode);
    }

    @Test
    @DisplayName("Повторное удаление")
    @Description("Удаление клиента по одинаковому id")
    public void reDeleteCustomer() {
        int expectedStatusCode = 404;
        String errorMessage = "Customer not found";

        deleteCustomer(id);

        Response responseBody = deleteCustomer(id);

        ResponseValidationNegative.validateOneField(responseBody, errorMessage, expectedStatusCode);
    }

    @Test
    @DisplayName("Удаление клиента по пустому полю id")
    @Description("Удаление клиента по пустому полю id")
    public void deleteCustomerByEmptyId() {
        int statusCode = 400;
        String path = "/customers/" + "null";

        Response response = RestAssured.delete("/customers/" + null);
        String responseTime = response.then().extract().path("timestamp").toString();

        ResponseValidationNegative.validateSeveralFields(response, statusCode, responseTime, path);
    }

    @Test
    @DisplayName("Удаление клиента по отрицательному id")
    @Description("Удаление клиента по отрицательному id")
    public void deleteCustomerByNegativeId() {
        int negativeId = -id;
        int statusCode = 404;
        String errorMessage = "Customer not found";

        Response response = RestAssured.delete("/customers/{id}", negativeId);

        ResponseValidationNegative.validateOneField(response, errorMessage, statusCode);
    }
}