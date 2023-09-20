package validator.response_validator;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

public class ResponseValidationNegative {

    public static void validateOneField(Response responseBody, String errorMessage, int statusCode) {
        responseBody.then().statusCode(statusCode)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response_negative_schema.json"))
                .body("errors", Matchers.hasItem(errorMessage));
    }

    public static void validateSeveralFields(Response responseBody, int statusCode, String responseTime, String path) {
        responseBody.then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response_negative_schema.json"))
                .body("timestamp", Matchers.equalTo(responseTime))
                .body("status", Matchers.equalTo(statusCode))
                .body("error", Matchers.equalTo("Bad Request"))
                .body("path", Matchers.equalTo(path));
    }
}