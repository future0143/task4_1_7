package validator;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

public class ResponseValidationNegativeOneLine {

    public static void validateFields(Response responseBody, String errorMessage, int statusCode) {
        responseBody.then().statusCode(statusCode)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response_negative_schema.json"))
                .body("errors", Matchers.hasItem(errorMessage));
    }
}
