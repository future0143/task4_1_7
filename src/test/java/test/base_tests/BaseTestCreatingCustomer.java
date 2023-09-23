package test.base_tests;

import config.DataProvider;
import config.TestProperties;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import java.time.LocalDateTime;

import static db.DatabaseManager.selectCountOfLinesInTableCustomer;
import static method_call.CustomersRequestHandler.createCustomerResponse;
import static utils.GeneratorPhoneNumber.getPhoneNumber;
import static validator.database_validator.DatabaseValidation.validateTableIsEmpty;

public class BaseTestCreatingCustomer {

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