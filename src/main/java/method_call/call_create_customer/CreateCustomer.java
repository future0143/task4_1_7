package method_call.call_create_customer;

import config.EndPoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import templates.get_Request_Specification.CreateCustomerRequestSpecification;

public class CreateCustomer {

    public static Response createCustomer(String requestBody, String phoneNumber) {
        Response responseBody = RestAssured.given().spec(CreateCustomerRequestSpecification.setupRequestSpecification(requestBody, phoneNumber))
                .post(EndPoints.CREATE_CUSTOMERS.getPath());
        return responseBody;
    }
}
