package validator.response_validator;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

public class ResponseValidationNegativeSeveralLines {

    public static void validateFields(Response responseBody, int statusCode, String responseTime, String path) {
        responseBody.then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response_negative_schema.json"))
                .body("timestamp", Matchers.equalTo(responseTime))
                .body("status", Matchers.equalTo(statusCode))
                .body("error", Matchers.equalTo("Bad Request"))
                .body("path", Matchers.equalTo(path));
    }
}
