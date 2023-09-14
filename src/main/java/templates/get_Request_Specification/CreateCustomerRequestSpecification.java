package templates.get_Request_Specification;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class CreateCustomerRequestSpecification {

    public static RequestSpecification setupRequestSpecification(String requestBody, String phoneNumber) {
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON);

        requestSpecification.body(requestBody.replace("+78905927238", phoneNumber));
        return requestSpecification;
    }
}
