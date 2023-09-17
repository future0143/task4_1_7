package test;

import config.ApiConfigSetup;
import config.DataProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import validator.ResponseValidationNegativeOneLine;
import validator.ResponseValidationNegativeSeveralLines;

import static method_call.call_create_customer.CreateCustomer.createCustomer;
import static method_call.call_delete_customer.DeleteCustomerById.deleteCustomer;
import static utils.GeneratorPhoneNumber.getPhoneNumber;

public class DeletingCustomerByIDTest implements ApiConfigSetup {

    private static int id;

    @BeforeEach
    void creatingCustomer() {
        String requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        String phoneNumber = getPhoneNumber();

        Response responseBody = createCustomer(requestBody, phoneNumber);

        id = responseBody.then().extract().path("id");
    }

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

        ResponseValidationNegativeOneLine.validateFields(responseBody, errorMessage, expectedStatusCode);
    }

    @Test
    @DisplayName("Удаление клиента по пустому полю id")
    @Description("Удаление клиента по пустому полю id")
    public void deleteCustomerByEmptyId() {
        int statusCode = 400;
        String path = "/customers/" + "null";

        Response response = RestAssured.delete("/customers/" + null);
        String responseTime = response.then().extract().path("timestamp").toString();

        ResponseValidationNegativeSeveralLines.validateFields(response, statusCode, responseTime, path);
    }

    @Test
    @DisplayName("Удаление клиента по отрицательному id")
    @Description("Удаление клиента по отрицательному id")
    public void deleteCustomerByNegativeId() {
        int negativeId = -id;
        int statusCode = 404;
        String errorMessage = "Customer not found";

        Response response = RestAssured.delete("/customers/{id}", negativeId);

        ResponseValidationNegativeOneLine.validateFields(response, errorMessage, statusCode);
    }
}