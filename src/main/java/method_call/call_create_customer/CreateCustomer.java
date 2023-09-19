package method_call.call_create_customer;

import config.EndPoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import templates.get_request_specification.CreateCustomerRequestSpecification;

public class CreateCustomer {

    public static Response createCustomerResponse(String requestBody, String phoneNumber) {
        return RestAssured.given().spec(CreateCustomerRequestSpecification.setupRequestSpecification(requestBody, phoneNumber))
                .post(EndPoints.CREATE_CUSTOMERS.getPath());
    }
}
