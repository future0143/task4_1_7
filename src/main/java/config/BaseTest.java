package config;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static method_call.CustomersRequestHandler.createCustomerResponse;
import static utils.GeneratorPhoneNumber.getPhoneNumber;

public class BaseTest {

    protected static int id;
    protected static String requestBody;
    protected static String phoneNumber;
    protected static LocalDateTime nowTime;

    @BeforeEach
    public void creatingCustomer() {
        requestBody = DataProvider.getTestData("src/main/resources/request/post_request/create-customer-required-fields.json");
        phoneNumber = getPhoneNumber();

        nowTime = LocalDateTime.now();

        Response responseBody = createCustomerResponse(requestBody, phoneNumber);

        id = responseBody.then().extract().path("id");
    }
}