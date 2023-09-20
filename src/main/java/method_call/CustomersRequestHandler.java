package method_call;

import config.EndPoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import templates.get_request_specification.CreateCustomerRequestSpecification;

public class CustomersRequestHandler {

    public static Response createCustomerResponse(String requestBody, String phoneNumber) {
        return RestAssured.given().spec(CreateCustomerRequestSpecification.setupRequestSpecification(requestBody, phoneNumber))
                .post(EndPoints.CREATE_CUSTOMERS.getPath());
    }

    public static Response deleteCustomer(int id) {
        return RestAssured.delete(EndPoints.DELETE_CUSTOMERS.getPath(), id);
    }

    public static Response getCustomerById(int id) {
        return RestAssured.get(EndPoints.GET_CUSTOMERS_BY_ID.getPath(), id);
    }

    public static Response getCustomerByPhoneNumber(String phoneNumber) {
        return RestAssured.given().queryParam("phoneNumber", phoneNumber)
                .when().get(EndPoints.GET_CUSTOMERS_BY_PHONE_NUMBER.getPath());
    }
}
